package agitter.main;

import java.net.URL;

import agitter.domain.Agitter;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.impl.AgitterViewImpl;
import com.vaadin.Application;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.URIHandler;

public class AgitterVaadinApplication extends Application {

	private Presenter presenter;

	@Override
	public void init() {
		Agitter agitter = PrevaylerBootstrap.agitter();

		setTheme("agitter");
		AgitterViewImpl view = new AgitterViewImpl();
		setMainWindow(view);

		presenter = new Presenter(agitter, view);

		view.addURIHandler(new URIHandler() { public DownloadStream handleURI(URL context, String relativeUri) {
			return presenter.onRestInvocation(context, relativeUri);
		}});
	}

}
