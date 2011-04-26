package agitter.main;

import agitter.domain.Agitter;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.impl.AgitterViewImpl;

import com.vaadin.Application;

public class AgitterVaadinApplication extends Application {

	@Override
	public void init() {
		Agitter agitter = PrevaylerBootstrap.agitter();
		AgitterViewImpl view = new AgitterViewImpl();
		new Presenter(agitter, view);
		setMainWindow(view);
	}
	
}