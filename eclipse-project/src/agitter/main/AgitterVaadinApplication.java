package agitter.main;

import static agitter.controller.Controller.CONTROLLER;

import java.net.URL;
import java.util.Map;

import vaadinutils.RestUtils;
import vaadinutils.RestUtils.RestHandler;
import vaadinutils.SessionUrlParameters;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.AgitterViewImpl;

import com.vaadin.Application;

public class AgitterVaadinApplication extends Application {

	private Presenter presenter;

	@Override
	public void init() {
		setTheme("agitter");
		final AgitterViewImpl view = new AgitterViewImpl();
		setMainWindow(view);

		SessionUrlParameters.handleForMainWindow(view);

		RestUtils.addRestHandler(view, new RestHandler() { @Override public void onRestInvocation(URL context, String relativeUri, Map<String, String[]> params) {
			if (presenter == null)
				presenter = new Presenter(CONTROLLER, view);
			presenter.onRestInvocation(context, relativeUri, params);
		}});
	}

}
