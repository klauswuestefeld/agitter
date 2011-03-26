package guardachuva.agitos.server.socialauth;

import guardachuva.agitos.server.domain.ApplicationImpl;
import guardachuva.agitos.shared.Application;
import guardachuva.agitos.shared.rpc.RemoteApplication;

import javax.servlet.http.HttpServlet;

import com.gdevelop.gwt.syncrpc.SyncProxy;

public abstract class ApplicationAwareServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String AGITOS_URL = "http://www.vagaloom.com";
	private Application _application;

	public ApplicationAwareServlet() {
		super();
	}

	protected Application getApp() {
			if(_application==null) {
	//			_application = getRemote();
				_application = getLocalApplication();
			}
			return _application;
		}

	private Application getLocalApplication() {
		return ApplicationImpl.GetInstance();
	}

	@SuppressWarnings("unused")
	private RemoteApplication getRemote() {
		return (RemoteApplication) SyncProxy.newProxyInstance(
				RemoteApplication.class, AGITOS_URL + "/agitosweb/", "rpc");
	}

}