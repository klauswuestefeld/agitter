package guardachuva.agitos.server.resource;


public class ContactsResource extends AuthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		return _application.getContactsForMe(_session);
	}

	@Override
	protected Object doPost() throws Exception {
		_application.addContactsToMe(_session, getParam("contact_mail"), this.getLinkAplicacao());
		return null;
	}
	
	@Override
	protected void doDelete() throws Exception {
		_application.deleteContactForMe(_session, getParam("email"));
	}
	
}
