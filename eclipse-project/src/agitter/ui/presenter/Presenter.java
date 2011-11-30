package agitter.ui.presenter;

import infra.logging.LogInfra;

import java.net.URL;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.FriendlyException;
import sneer.foundation.lang.exceptions.Refusal;
import vaadinutils.RestUtils.RestHandler;
import vaadinutils.SessionUtils;
import agitter.controller.Controller;
import agitter.domain.Agitter;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;
import agitter.domain.users.Users;
import agitter.domain.users.Users.InvalidAuthenticationToken;
import agitter.domain.users.Users.UserNotFound;
import agitter.ui.view.AgitterView;
import agitter.ui.view.session.SessionView;

public class Presenter implements RestHandler {
	
	public static String AUTHENTICATION_TOKEN_NAME = "AuthenticationToken";

	private final Controller controller;
	private final AgitterView view;
	private final HttpSession httpSession;
	private final String context;
	private final Functor<EmailAddress, User> userProducer;

	private HttpServletResponse currentResponse;

	public Presenter(Controller controller, AgitterView view, HttpServletRequest firstRequest) {
		this.controller = controller;
		this.view = view;
		this.httpSession = firstRequest.getSession();
		this.context = firstRequest.getRequestURL().toString();
		
		this.userProducer = UserUtils.userProducer(domain().users());

		SessionUtils.initParameters(httpSession, firstRequest.getParameterMap());
		
		authenticateUser(firstRequest);
	}


	private void authenticateUser(HttpServletRequest firstRequest) {
		try {
			attemptLoginWith(firstRequest.getCookies());
		} catch (UserNotFound e) {
			openAuthentication();
		} catch (InvalidAuthenticationToken e) {
			openAuthentication();
		}
	}
	

	@Override
	public void onRestInvocation(URL context, String relativeUri, Map<String, String[]> params) {
		try {
			tryRestInvocation(relativeUri, params);
		} catch (FriendlyException e) {
			warn(e.getMessage());
		} catch (RuntimeException e) {
			LogInfra.getLogger(this).log(Level.SEVERE, "Rest error. Context: " + context + " relativeUri: " + relativeUri, e);
			warn("Erro processando requisição.");
		}
	}
	
	public void setCurrentResponse(HttpServletResponse response) {
		currentResponse = response;
	}
	
	
	private void tryRestInvocation(String relativeUri, Map<String, String[]> params) throws Refusal {
		recoverFromRedirectWithoutBlink();
		String[] uri = relativeUri.split("/");
		if (uri.length == 0) return;

		String command = uri[0];

		if ("demo".equals(command)) { demo(); }
		if ("contacts-demo".equals(command)) { contactsDemo(); }
		if ("unsubscribe".equals(command)) { unsubscribe(uri); }
		if ("signup".equals(command)) { restSignup(params); }
		if ("oauth".equals(command)) { oAuthCallback(params); }
	}


	private void recoverFromRedirectWithoutBlink() {
		view.show();
	}


	private void contactsDemo() {
		SessionView sessionView = view.showSessionView();
		sessionView.init(new ContactsDemoNeeds());
		sessionView.showContactsView();
		new ContactsDemo(sessionView.contactsView());
	}


	private void demo() {
		onAuthenticate().consume(new DemoPreparation(domain()).user());
	}


	private Consumer<User> onAuthenticate() {
		return new Consumer<User>() { @Override public void consume(User user) {
			SessionView sessionView = view.showSessionView();
			updateAuthenticationTokenFor(user);
			new SessionPresenter(user, domain().contacts().contactsOf(user), domain().events(), userProducer, sessionView, warningDisplayer(), onLogout());
		}};
	}

	
	private Runnable onLogout() {
		return new Runnable() { @Override public void run() {
			clearAuthenticationToken();
			openAuthentication();
		}};
	}
	

	private void openAuthentication() {
		new AuthenticationPresenter(domain().users(), view.authenticationView(), onAuthenticate(), controller.signups(), controller.emailSender(), controller.oAuth(), warningDisplayer(), httpSession, context, urlBlankRedirector());
	}

	
	private Consumer<String> urlBlankRedirector() {
		return new Consumer<String>() {  @Override public void consume(String url) {
			view.hideToAvoidExtraBlinkAndRedirect(url);
		}};
	}


	private void restSignup(Map<String, String[]> params) throws Refusal {
		User user = controller.signups().onRestInvocation(params);
		onAuthenticate().consume(user);
	}

	
	private void oAuthCallback(Map<String, String[]> params) throws Refusal {
		try {
			User user = controller.oAuth().signinCallback(params, httpSession);
			onAuthenticate().consume(user);
			view.hideToAvoidExtraBlinkAndRedirect(context.toString());
		} catch (Exception e) {
			throw new Refusal("Erro de autenticação na rede social.");
		}
	}


	private void unsubscribe(String[] uri) {
		if (uri.length < 2) return;
		
		String userEncryptedInfo = uri[1];
		//TODO - Criar um presenter com uma telinha de info da unsubscribe
		//TODO - Acho que o unsubscribe deveria ter uma tela de login para confirmar o unsubscribe, ai nao precisava nem ter crypto na url
		try {
			domain().users().unsubscribe(userEncryptedInfo);
			view.showWarningMessage("Você não receberá mais emails do Agitter.");
		} catch(Users.UserNotFound userNotFound) {
			this.view.showWarningMessage(userNotFound.getMessage());
		}
	}

	
	private Consumer<String> warningDisplayer() {
		return new Consumer<String>() { @Override public void consume(String message) {
			warn(message);
		}};
	}

	
	private Agitter domain() {
		return controller.domain();
	}
	
	private void warn(String message) {
		view.showWarningMessage(message);
	}

	private void updateAuthenticationTokenFor(User user) {
		if( currentResponse == null ) { //Is this necessary? Check log in production.
			LogInfra.getLogger(this).info("It is possible for currentResponse to be null.");
			return;
		}
		//TODO: tell users to generate a token for user...
		setCookieForever( AUTHENTICATION_TOKEN_NAME, user.email().toString() );
	}
	
	private void clearAuthenticationToken() {
		setCookieForever( AUTHENTICATION_TOKEN_NAME, "" );
	};
	
	private void attemptLoginWith(Cookie[] cookies) throws InvalidAuthenticationToken, UserNotFound {
		String token = searchAuthenticationTokenIn(cookies);
		User user = domain().users().loginWithAuthenticationToken(token);
		onAuthenticate().consume(user);
	}
	
	private void setCookieForever(String name, String value) {
		final Cookie authenticationCookie = new Cookie( name, value );
		authenticationCookie.setMaxAge( Integer.MAX_VALUE );
		authenticationCookie.setPath( "/" );
		currentResponse.addCookie( authenticationCookie );		
	}
	

	private String searchAuthenticationTokenIn(Cookie[] cookies) {
		if (cookies == null) return null;
		for (Cookie c : cookies)
			if (Presenter.AUTHENTICATION_TOKEN_NAME.equals(c.getName()))
				return c.getValue();
		return null;
	}
	
}

