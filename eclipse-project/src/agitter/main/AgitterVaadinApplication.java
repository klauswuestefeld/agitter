package agitter.main;

import java.net.URL;
import java.util.Map;

import vaadinutils.RestUtils;
import vaadinutils.RestUtils.RestHandler;
import agitter.domain.Agitter;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.AgitterViewImpl;

import com.vaadin.Application;

public class AgitterVaadinApplication extends Application {

	private Presenter presenter;

	@Override
	public void init() {
		Agitter agitter = PrevaylerBootstrap.agitter();

		setTheme("agitter");
		AgitterViewImpl view = new AgitterViewImpl();
		setMainWindow(view);

		presenter = new Presenter(agitter, view);
		RestUtils.addRestHandler(view, new RestHandler() { @Override public void onRestInvocation(URL context, String relativeUri, Map<String, String[]> params) {
			presenter.onRestInvocation(context, relativeUri, params);
		}});
	}

}
