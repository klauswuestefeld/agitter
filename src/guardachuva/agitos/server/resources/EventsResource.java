package guardachuva.agitos.server.resources;


public class EventsResource extends AuthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		return _application.getEventsFor(_user);
	}

	@Override
	protected Object doPost() throws Exception {
		_application.createEvent(_user, 
				getParam("description"), 
				getParam("date"));
		return null;
	}

	@Override
	protected void doDelete() throws Exception {
		int id = Integer.parseInt(getParam("id"));
		_application.removeEventFor(_user, id);
	}

}
