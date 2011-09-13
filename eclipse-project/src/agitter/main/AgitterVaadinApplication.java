package agitter.main;

import java.net.URL;
import java.util.Map;

import vaadinutils.SessionUrlParameters;
import agitter.domain.Agitter;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.AgitterViewImpl;

import com.vaadin.Application;
import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.ParameterHandler;
import com.vaadin.terminal.URIHandler;

public class AgitterVaadinApplication extends Application {

	private Presenter presenter;

	@Override
	public void init() {
		Agitter agitter = PrevaylerBootstrap.agitter();

		setTheme("agitter");
		AgitterViewImpl view = new AgitterViewImpl();
		setMainWindow(view);

		SessionUrlParameters.handleForMainWindow(view);

		presenter = new Presenter(agitter, view);

		RestHandler restHandler = new RestHandler();

		view.addParameterHandler(restHandler);
		view.addURIHandler(restHandler);
	}

	private class RestHandler implements ParameterHandler, URIHandler {

		Map<String, String[]> params;

		@Override
		public void handleParameters(Map<String, String[]> params) {
			this.params = params;
		}
		@Override
		public DownloadStream handleURI(URL context, String relativeUri) {
			return presenter.onRestInvocation(context, relativeUri, params);
		}
	}

}
