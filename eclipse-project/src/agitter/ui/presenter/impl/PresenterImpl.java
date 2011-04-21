package agitter.ui.presenter.impl;

import agitter.domain.Agitter;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.AgitterView;

public class PresenterImpl implements Presenter {

	private final Agitter _agitter;

	public PresenterImpl(Agitter agitter) {
		_agitter = agitter;
	}

	
	@Override
	public void startSession(AgitterView view) {
		new PresenterSession(_agitter, view);
	}
}
