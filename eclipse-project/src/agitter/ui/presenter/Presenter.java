package agitter.ui.presenter;

import java.net.URL;

import sneer.foundation.lang.Consumer;
import agitter.domain.Agitter;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.ui.view.AgitterView;
import agitter.ui.view.session.SessionView;

import com.vaadin.terminal.DownloadStream;

public class Presenter {

	private final Agitter _agitter;
	private final AgitterView _view;


	public Presenter(Agitter agitter, AgitterView view) {
		_agitter = agitter;
		_view = view;

		openAuthentication();
	}

	
	public DownloadStream onRestInvocation(URL context, String relativeUri) {
		System.out.println(relativeUri);
		String[] uri = relativeUri.split("/");
		if(uri.length==0) { return null; }

		String command = uri[0];
		System.out.println(command);

		if ("contactsDemo".equals(command)) { onContactsDemo(); }
		if ("unsubscribe".equals(command)) { onUnsubscribe(uri); }
		return null;
	}

	
	private void onContactsDemo() {
		SessionView sessionView = _view.showSessionView();
		sessionView.show("DemoUser");
		sessionView.showContactsView();
		//new ContactsDemoPresenter(_view.showSessionView().contactsView());
	}


	private Consumer<User> onAuthenticate() {
		return new Consumer<User>() { @Override public void consume(User user) {
			SessionView sessionView = _view.showSessionView();
			new SessionPresenter(user, _agitter.contacts().contactsOf(user), _agitter.events(), sessionView, warningDisplayer(), onLogout());
		}};
	}

	
	private Runnable onLogout() {
		return new Runnable() { @Override public void run() {
			openAuthentication();
		}};
	}
	

	private void openAuthentication() {
		new AuthenticationPresenter(_agitter.users(), _view.loginView(), onAuthenticate(), warningDisplayer());
	}

	
	private void onUnsubscribe(String[] uri) {
		if(uri.length<2) {  return; }
		String userEncryptedInfo = uri[1];
		//TODO - Criar um presenter com uma telinha de info da unsubscribe
		//TODO - Acho que o unsubscribe deveria ter uma tela de login para confirmar o unsubscribe, ai nao precisava nem ter crypto na url
		try {
			this._agitter.users().unsubscribe(userEncryptedInfo);
			_view.showWarningMessage("Você não receberá mais emails do Agitter.");
		} catch(Users.UserNotFound userNotFound) {
			this._view.showWarningMessage(userNotFound.getMessage());
		}
	}

	
	private Consumer<String> warningDisplayer() {
		return new Consumer<String>() { @Override public void consume(String message) {
			_view.showWarningMessage(message);
		}};
	}

}

