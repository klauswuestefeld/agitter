package agitter.ui.presenter;

import sneer.foundation.lang.Consumer;
import agitter.domain.Agitter;
import agitter.domain.AgitterSession;
import agitter.ui.view.AgitterView;
import agitter.ui.view.SessionView;

public class Presenter {

	private final Agitter _agitter;
	private final AgitterView _view;

	
	public Presenter(Agitter agitter, AgitterView view) {
		_agitter = agitter;
		_view = view;
		
		AgitterSession session = _agitter.signup("Ana", "ana@gmail.com", "ana123");
		
		SessionView sessionView = _view.showSessionView();
		new SessionPresenter(session, sessionView, errorDisplayer());
	}


	private Consumer<String> errorDisplayer() {
		return new Consumer<String>() {  @Override public void consume(String message) {
			_view.showErrorMessage(message);
		}};
	}

	
}

