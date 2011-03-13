package guardachuva.agitos.server.resources;

import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.server.application.Application;
import guardachuva.agitos.server.application.IApplication;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import httprevayler.BaseResource;

public class UnauthenticatedBaseResource extends BaseResource {
	
	protected IApplication _application = null;
	
	@Override
	protected void beforeService() throws UnauthorizedBusinessException {
		_application = my(Application.class);
	}
	
	protected String getLinkAplicacao() {
		String requestURL = _request.getRequestURL().toString();
		return requestURL.substring(0, requestURL.indexOf('/', 7));
	}

}
