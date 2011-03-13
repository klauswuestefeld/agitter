package guardachuva.agitos.server.resources;


public class ContactsResource extends AuthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		return _application.getContactsFor(_user);
	}

	@Override
	protected Object doPost() throws Exception {
		_application.addContactsTo(_user, getParam("contact_mail"), this.getLinkAplicacao());
		return null;
	}
	
	@Override
	protected void doDelete() throws Exception {
		_application.deleteContactFor(_user, getParam("email"));
	}
	
}
