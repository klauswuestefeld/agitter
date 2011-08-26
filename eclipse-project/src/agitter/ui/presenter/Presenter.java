package agitter.ui.presenter;

import java.net.URL;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Functor;
import agitter.domain.Agitter;
import agitter.domain.emails.EmailAddress;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;
import agitter.domain.users.Users;
import agitter.ui.view.AgitterView;
import agitter.ui.view.session.SessionView;

import com.vaadin.terminal.DownloadStream;

public class Presenter {

	private final Agitter agitter;
	private final AgitterView view;
	private final Functor<EmailAddress, User> userSearch;


	public Presenter(Agitter agitter, AgitterView view) {
		this.agitter = agitter;
		this.view = view;
		userSearch = userSearch();
		
		openAuthentication();
	}

	
	public DownloadStream onRestInvocation(URL context, String relativeUri) {
		String[] uri = relativeUri.split("/");
		if(uri.length==0) { return null; }

		String command = uri[0];

		if ("contactsDemo".equals(command)) { onContactsDemo(); }
		if ("unsubscribe".equals(command)) { onUnsubscribe(uri); }
		return null;
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
			new SessionPresenter(user, agitter.contacts().contactsOf(user), agitter.events(), userSearch, sessionView, warningDisplayer(), onLogout());
		}};
	}

	
	private Runnable onLogout() {
		return new Runnable() { @Override public void run() {
			openAuthentication();
		}};
	}
	

	private void openAuthentication() {
		new AuthenticationPresenter(agitter.users(), view.loginView(), onAuthenticate(), warningDisplayer());
	}

	
	private void onUnsubscribe(String[] uri) {
		if(uri.length<2) {  return; }
		String userEncryptedInfo = uri[1];
		//TODO - Criar um presenter com uma telinha de info da unsubscribe
		//TODO - Acho que o unsubscribe deveria ter uma tela de login para confirmar o unsubscribe, ai nao precisava nem ter crypto na url
		try {
			this.agitter.users().unsubscribe(userEncryptedInfo);
			view.showWarningMessage("Você não receberá mais emails do Agitter.");
		} catch(Users.UserNotFound userNotFound) {
			this.view.showWarningMessage(userNotFound.getMessage());
		}
	}

	
	private Consumer<String> warningDisplayer() {
		return new Consumer<String>() { @Override public void consume(String message) {
			view.showWarningMessage(message);
		}};
	}

	
	private Functor<EmailAddress, User> userSearch() {
		return new Functor<EmailAddress, User>() {  @Override public User evaluate(EmailAddress email) {
			return UserUtils.produce(agitter.users(), email);
		}};
	}

}

