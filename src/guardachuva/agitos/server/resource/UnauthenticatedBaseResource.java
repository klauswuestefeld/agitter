package guardachuva.agitos.server.resource;

import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.server.application.ApplicationImpl;
import guardachuva.agitos.shared.Application;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import httprevayler.BaseResource;

public class UnauthenticatedBaseResource extends BaseResource {
	
	protected Application _application = null;
	
	@Override
	protected void beforeService() throws UnauthorizedBusinessException {
		_application = my(ApplicationImpl.class);
	}
	
	protected String getLinkAplicacao() {
		String requestURL = _request.getRequestURL().toString();
		return requestURL.substring(0, requestURL.indexOf('/', 7));
	}

	protected String getParam(String paramName) {
		return _request.getParameter(paramName);
	}

}
