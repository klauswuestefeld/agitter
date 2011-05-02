package agitter.ui.presenter;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.Agitter;
import agitter.domain.User;
import agitter.ui.view.AgitterView;
import agitter.ui.view.SessionView;

public class Presenter {

	private final Agitter _agitter;
	private final AgitterView _view;

	
	public Presenter(Agitter agitter, AgitterView view) {
		_agitter = agitter;
		_view = view;
		
		User user;
		try {
			user = _agitter.signup("Ana", "ana@gmail.com", "ana123");
		} catch (Refusal e) {
			throw new sneer.foundation.lang.exceptions.NotImplementedYet(e);
		}
		
		SessionView sessionView = _view.showSessionView();
		new SessionPresenter(_agitter.events(), user, sessionView, errorDisplayer());
	}


	private Consumer<String> errorDisplayer() {
		return new Consumer<String>() {  @Override public void consume(String message) {
			_view.showErrorMessage(message);
		}};
	}

	
}

