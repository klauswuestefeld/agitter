package agitter.ui.presenter;

import infra.logging.LogInfra;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import agitter.controller.AuthenticationToken;
import agitter.controller.Controller;
import agitter.domain.Agitter;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;
import agitter.domain.users.Users;
import agitter.domain.users.Users.InvalidAuthenticationToken;
import agitter.domain.users.Users.UserNotActive;
import agitter.domain.users.Users.UserNotFound;
import agitter.ui.presenter.hacks.AnnePreparation;
import agitter.ui.presenter.hacks.ContactsDemo;
import agitter.ui.presenter.hacks.ContactsDemoNeeds;
import agitter.ui.presenter.hacks.DemoPreparation;
import agitter.ui.view.AgitterView;
import agitter.ui.view.session.SessionView;

// TODO Verificar com o Klaus se já não existe algo no foundation para fazer isso.
// Caso não tenha, mover a classe para um lugar melhor.
class Notifier<T> {
	private final List<Consumer<T>> consumers = new ArrayList<Consumer<T>>();
	private T lastValue;
	synchronized public void notify(T value) {
		for(Consumer<T> consumer : consumers)
			consumer.consume(value);
		lastValue = value;
	}
	synchronized public void addConsumer(Consumer<T> consumer) {
		consumers.add(consumer);
	}
	public void addConsumerAndNotifyLastValue(Consumer<T> consumer) {
		addConsumer(consumer);
		consumer.consume(lastValue);
	}
}

public class Presenter implements RestHandler {
	
	public static String AUTHENTICATION_TOKEN_NAME = "AuthenticationToken";

	private final Controller controller;
	private final AgitterView view;
	private final HttpSession httpSession;
	private final String context;
	private final Functor<EmailAddress, User> userProducer;
	private final Notifier<String> urlRestPathNotifier; 
	
	private SessionPresenter loggedSession;

	private HttpServletResponse currentResponse;

	public Presenter(Controller controller, AgitterView view, HttpServletRequest firstRequest, HttpServletResponse firstResponse) {
		this.controller = controller;
		this.view = view;
		this.httpSession = firstRequest.getSession();
		this.context = firstRequest.getRequestURL().toString();
		
		this.userProducer = UserUtils.userProducer(domain().users());
		
		this.urlRestPathNotifier = new Notifier<String>();
		
		setCurrentResponse(firstResponse);
		
		SessionUtils.initParameters(httpSession, firstRequest.getParameterMap());
		
		authenticateUser(firstRequest);
	}


	private void authenticateUser(HttpServletRequest firstRequest) {
		try {
			attemptLoginWith(firstRequest.getCookies());
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
		boolean processed = false;
		if ("demo".equals(command)) { demo(); processed = true; }
		if ("anne".equals(command)) { anne(); processed = true; }
		if ("contacts-demo".equals(command)) { contactsDemo(); processed = true; }
		if ("unsubscribe".equals(command)) { unsubscribe(uri); processed = true; }
		if ("signup".equals(command)) { restSignup(params); processed = true; }
		if ("auth".equals(command)) { restAuth(params); processed = true; }
		if ("oauth".equals(command)) { oAuthCallback(params); processed = true; }
		if ("link".equals(command)) { oAuthLinkCallback(params); processed = true; }
		if(!processed)
			urlRestPathNotifier.notify(relativeUri);
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

	private void anne() {
		onAuthenticate().consume(new AnnePreparation(domain()).user());
	}

	private Consumer<User> onAuthenticate() {
		return new Consumer<User>() { @Override public void consume(User user) {
			SessionView sessionView = view.showSessionView();
			updateAuthenticationTokenFor(user);
			loggedSession = new SessionPresenter(user, domain().contacts().contactsOf(user), domain().events(), domain().comments(), userProducer, sessionView, warningDisplayer(), onLogout(), 
							controller.oAuth(), httpSession, context, urlBlankRedirector(), urlRestPathNotifier);
		}};
	}

	
	private Runnable onLogout() {
		return new Runnable() { @Override public void run() {
			clearAuthenticationToken();
			openAuthentication();
		}};
	}
	

	private void openAuthentication() {
		new AuthenticationPresenter(domain().users(), view.authenticationView(), onAuthenticate(), controller.signups(), controller.emailSender(), controller.oAuth(), warningDisplayer(), javascriptExecutor(), httpSession, context, urlBlankRedirector());
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

	
	private void restAuth(Map<String, String[]> params) throws Refusal {
		AuthenticationToken token = new AuthenticationToken(params);
		User user = userProducer.evaluate(token.email());
		onAuthenticate().consume(user);
	}

	
	private void oAuthCallback(Map<String, String[]> params) throws Refusal {
		try {
			User user = controller.oAuth().signinCallback(params, httpSession);
			onAuthenticate().consume(user);
			view.hideToAvoidExtraBlinkAndRedirect(context.toString());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Refusal("Erro de autenticação na rede social.");
		}
	}
	
	private void oAuthLinkCallback(Map<String, String[]> params) throws Refusal {
		try {
			controller.oAuth().linkAccountCallback(loggedSession.loggedUser(), domain(), params, httpSession);
			loggedSession.refresh();
			view.hideToAvoidExtraBlinkAndRedirect(context.toString());
		} catch (Exception e) {
			e.printStackTrace();
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

	private Consumer<String> javascriptExecutor() {
		return new Consumer<String>() { @Override public void consume(String javascript) {
			executeJavascript(javascript);
		}};
	}
	
	private Agitter domain() {
		return controller.domain();
	}
	
	private void warn(String message) {
		view.showWarningMessage(message);
	}

	private void executeJavascript(String javascript) {
		view.executeJavascript(javascript);
	}

	private void updateAuthenticationTokenFor(User user) {
		setCookieForever( AUTHENTICATION_TOKEN_NAME, new AuthenticationToken( user.email() ).asSecureURI() );
	}
	
	private void clearAuthenticationToken() {
		setCookieForever( AUTHENTICATION_TOKEN_NAME, "" );
	};
	
	private void attemptLoginWith(Cookie[] cookies) throws InvalidAuthenticationToken {
		String token = searchAuthenticationTokenIn(cookies);
		EmailAddress email = getEmail(token);
		User user;
		try {
			user = domain().users().loginWithEmail(email);
		} catch (UserNotActive e) {
			throw new InvalidAuthenticationToken("User not active");
		} catch (UserNotFound e) {
			throw new InvalidAuthenticationToken("User not found");
		}
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

	
	private EmailAddress getEmail(String cookie) throws InvalidAuthenticationToken {
		try {
			AuthenticationToken req = new AuthenticationToken(cookie);
			return req.email();
		} catch (Exception e) {
			throw new InvalidAuthenticationToken(e.getMessage());
		}
	}
	
}

