package agitter.main;

import agitter.domain.Agitter;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.impl.AgitterMainWindow;

import com.vaadin.Application;

public class AgitterVaadinApplication extends Application {

	@Override
	public void init() {
		Agitter agitter = PrevaylerBootstrap.agitter();
		AgitterMainWindow window = new AgitterMainWindow();
		new Presenter(agitter, window);
		setMainWindow(window);
	}
	
}
