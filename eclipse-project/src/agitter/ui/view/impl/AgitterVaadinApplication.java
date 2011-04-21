package agitter.ui.view.impl;

import agitter.ui.presenter.Presenter;

import com.vaadin.Application;

public class AgitterVaadinApplication extends Application {

	private static final long serialVersionUID = 1L;
	private static Presenter _presenter;

	public static void staticInit(Presenter presenter) {
		_presenter = presenter;
	}

	
	@Override
	public void init() {
		AgitterMainWindow window = new AgitterMainWindow(_presenter);
		setMainWindow(window);
	}
	
}
