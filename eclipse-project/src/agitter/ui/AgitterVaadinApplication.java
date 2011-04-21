package agitter.ui;

import agitter.domain.Agitter;

import com.vaadin.Application;

public class AgitterVaadinApplication extends Application {

	private static final long serialVersionUID = 1L;
	private static Agitter _agitter;

	public static void init(Agitter agitter) {
		_agitter = agitter;
	}

	
	@Override
	public void init() {
		AgitterPresenterImpl presenter = new AgitterPresenterImpl(_agitter);
		AgitterMainWindow window = new AgitterMainWindow(presenter);
		setMainWindow(window);
	}
	
}
