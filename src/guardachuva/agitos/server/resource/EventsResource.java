package guardachuva.agitos.server.resource;

import guardachuva.agitos.server.DateTimeUtilsServer;



public class EventsResource extends AuthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		return _application.getEventsForMe(_session);
	}

	@Override
	protected Object doPost() throws Exception {
		_application.createEvent(_session, 
				getParam("description"), 
				DateTimeUtilsServer.strToDate(getParam("date")));
		return null;
	}

	@Override
	protected void doDelete() throws Exception {
		int id = Integer.parseInt(getParam("id"));
		_application.removeEventForMe(_session, id);
	}

}
