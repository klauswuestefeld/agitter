package agitter.ui.presenter;

import infra.logging.LogInfra;

import java.net.URL;
import java.util.Map;
import java.util.logging.Level;

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
	private final Functor<EmailAddress, User> userSearch;


	public Presenter(Controller controller, AgitterView view) {
		this.controller = controller;
		this.view = view;
		userSearch = userSearch();
		
		openAuthentication();
	}

	public DownloadStream onRestInvocation(URL context, String relativeUri, Map<String, String[]> params) {
		try {
			tryRestInvocation(relativeUri, params);
		} catch (FriendlyException e) {
			warn(e.getMessage());
		} catch (Exception e) {
			LogInfra.getLogger(this).log(Level.SEVERE, "Rest error. Context: " + context + " relativeUri: " + relativeUri, e);
			warn("Erro processando requisição.");
		}
		return null;
	}

	private void tryRestInvocation(String relativeUri, Map<String, String[]> params) throws Refusal {
		String[] uri = relativeUri.split("/");
		if (uri.length == 0) return;

		String command = uri[0];

		if ("contactsDemo".equals(command)) { onContactsDemo(); }
		if ("unsubscribe".equals(command)) { onUnsubscribe(uri); }
		if ("signup".equals(command)) { onSignup(params); }
	}


	private void onContactsDemo() {
		SessionView sessionView = view.showSessionView();
		sessionView.show("DemoUser");
		sessionView.showContactsView();
		new ContactsDemoPresenter(sessionView.contactsView());
	}


	private Consumer<User> onAuthenticate() {
		return new Consumer<User>() { @Override public void consume(User user) {
			SessionView sessionView = view.showSessionView();
			new SessionPresenter(user, domain().contacts().contactsOf(user), domain().events(), userSearch, sessionView, warningDisplayer(), onLogout());
		}};
	}

	
	private Runnable onLogout() {
		return new Runnable() { @Override public void run() {
			openAuthentication();
		}};
	}
	

	private void openAuthentication() {
		new AuthenticationPresenter(domain().users(), view.loginView(), onAuthenticate(), controller.signups(), controller.emailSender(), warningDisplayer());
	}

	
	private void onSignup(Map<String, String[]> params) throws Refusal {
		controller.signups().onRestInvocation(params);
	}

	
	private void onUnsubscribe(String[] uri) {
		if(uri.length<2) {  return; }
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

	
	private Functor<EmailAddress, User> userSearch() {
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

