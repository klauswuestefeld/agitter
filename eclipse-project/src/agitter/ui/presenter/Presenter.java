package agitter.ui.presenter;

import infra.logging.LogInfra;

import java.net.URL;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.http.HttpSession;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.FriendlyException;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.controller.Controller;
import agitter.domain.Agitter;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;
import agitter.domain.users.Users;
import agitter.ui.view.AgitterView;
import agitter.ui.view.session.SessionView;

import com.vaadin.terminal.DownloadStream;

public class Presenter {

	private final Controller controller;
	private final AgitterView view;
	private final Functor<EmailAddress, User> userProducer;
	private final HttpSession httpSession;
	private final URL context;


	public Presenter(Controller controller, AgitterView view, HttpSession httpSession, URL context) {
		this.controller = controller;
		this.view = view;
		this.httpSession = httpSession;
		this.context = context;
		userProducer = userProducer();
		
		openAuthentication();
	}
	

	public DownloadStream onRestInvocation(URL context, String relativeUri, Map<String, String[]> params) {
		try {
			tryRestInvocation(relativeUri, params);
		} catch (FriendlyException e) {
			warn(e.getMessage());
		} catch (RuntimeException e) {
			LogInfra.getLogger(this).log(Level.SEVERE, "Rest error. Context: " + context + " relativeUri: " + relativeUri, e);
			warn("Erro processando requisição.");
		}
		return null;
	}

	
	private void tryRestInvocation(String relativeUri, Map<String, String[]> params) throws Refusal {
		String[] uri = relativeUri.split("/");
		if (uri.length == 0) return;

		String command = uri[0];

		if ("demo".equals(command)) { onDemo(); }
		if ("contacts-demo".equals(command)) { onContactsDemo(); }
		if ("unsubscribe".equals(command)) { onUnsubscribe(uri); }
		if ("signup".equals(command)) { onRestSignup(params); }
		if ("oauth".equals(command)) { onOAuthSignin(params); }
	}


	private void onContactsDemo() {
		SessionView sessionView = view.showSessionView();
		sessionView.init(new ContactsDemoNeeds());
		sessionView.showContactsView();
		new ContactsDemo(sessionView.contactsView());
	}


	private void onDemo() {
		onAuthenticate().consume(new DemoPreparation(domain()).user());
	}


	private Consumer<User> onAuthenticate() {
		return new Consumer<User>() { @Override public void consume(User user) {
			SessionView sessionView = view.showSessionView();
			new SessionPresenter(user, domain().contacts().contactsOf(user), domain().events(), userProducer, sessionView, warningDisplayer(), onLogout());
		}};
	}

	
	private Runnable onLogout() {
		return new Runnable() { @Override public void run() {
			openAuthentication();
		}};
	}
	

	private void openAuthentication() {
		new AuthenticationPresenter(domain().users(), view.authenticationView(), onAuthenticate(), controller.signups(), controller.emailSender(), controller.oAuth(), warningDisplayer(), httpSession, context);
	}

	
	private void onRestSignup(Map<String, String[]> params) throws Refusal {
		User user = controller.signups().onRestInvocation(params);
		onAuthenticate().consume(user);
	}

	
	private void onOAuthSignin(Map<String, String[]> params) throws Refusal {
		try {
			User user = controller.oAuth().signinCallback(params, httpSession);
			onAuthenticate().consume(user);
		} catch (Exception e) {
			throw new Refusal("Erro de autenticação na rede social.");
		}
	}


	private void onUnsubscribe(String[] uri) {
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

	
	private Functor<EmailAddress, User> userProducer() {
		return new Functor<EmailAddress, User>() {  @Override public User evaluate(EmailAddress email) {
			return UserUtils.produce(domain().users(), email);
		}};
	}

	
	private Agitter domain() {
		return controller.domain();
	}

	
	private void warn(String message) {
		view.showWarningMessage(message);
	}

}

