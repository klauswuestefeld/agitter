package agitter.ui.presenter;

import java.net.URL;

import agitter.domain.Agitter;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.ui.view.AgitterView;
import agitter.ui.view.SessionView;
import com.vaadin.terminal.DownloadStream;
import sneer.foundation.lang.Consumer;

public class Presenter {

	private final Agitter _agitter;
	private final AgitterView _view;


	public Presenter(Agitter agitter, AgitterView view) {
		_agitter = agitter;
		_view = view;

		openAuthentication();
	}

	public DownloadStream onRestInvocation(URL context, String relativeUri) {
		String[] uri = relativeUri.split("/");
		if(uri.length==0) { return null; }

		String command = uri[0];
		if("logout".equals(command)) { onLogout().run(); }
		if("unsubscribe".equals(command)) { onUnsubscribe(uri); }
		return null;
	}

	private Consumer<User> onAuthenticate() {
		return new Consumer<User>() {
			@Override
			public void consume(User user) {
				SessionView sessionView = _view.showSessionView();
				new SessionPresenter(_agitter.events(), user, sessionView, onLogout(), warningDisplayer());
				warningDisplayer().consume("Por enquanto, todos os agitos são públicos entao, pra não fazer spam, crie apenas agitos de software: dojos, horaextra, congressos, cursos, palestras, webcasts, etc. Valeu!");
			}
		};
	}

	private Runnable onLogout() {
		return new Runnable() {
			@Override
			public void run() {
				openAuthentication();
			}
		};
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
			_view.showWarningMessage("Você não mais receberá emails relativos a eventos públicos!");
		} catch(Users.UserNotFound userNotFound) {
			this._view.showWarningMessage(userNotFound.getMessage());
		}
	}

	private Consumer<String> warningDisplayer() {
		return new Consumer<String>() {
			@Override
			public void consume(String message) {
				_view.showWarningMessage(message);
			}
		};
	}

}

