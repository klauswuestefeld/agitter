package agitter.ui;

import java.io.File;

import agitter.util.PrevaylerBootstrap;

import com.vaadin.Application;

public class AgitterApplication extends Application {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		initSystem();
		AgitterPresenterImpl presenter = new AgitterPresenterImpl(PrevaylerBootstrap.execution());
		AgitterMainWindow window = new AgitterMainWindow(presenter);
		setMainWindow(window);
	}
	private void initSystem() {
		try {
			final File tmpFolder = new File("/agitterrepo"); //TODO
			if(!tmpFolder.exists()) { tmpFolder.mkdir(); }
			PrevaylerBootstrap.open(tmpFolder);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
