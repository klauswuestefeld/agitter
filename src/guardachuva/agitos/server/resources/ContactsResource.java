package guardachuva.agitos.server.resources;

import guardachuva.agitos.crypt.Cryptor;
import guardachuva.agitos.domain.User;
import guardachuva.agitos.domain.comparators.UserEmailComparator;
import guardachuva.agitos.shared.BusinessException;
import guardachuva.mailer.core.Mail;
import guardachuva.mailer.templates.ConviteAcessoTemplate;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ContactsResource extends AuthenticatedBaseResource {

	@Override
	protected Object doGet() throws Exception {
		User[] contacts = (User[]) _user.getContacts().toArray(new User[_user.getContacts().size()]);
		Arrays.sort(contacts, new UserEmailComparator());
		return contacts;
	}

	@Override
	protected Object doPost() throws Exception {
		String contact_mail = _request.getParameter("contact_mail");
		List<User> contacts = _application.userHome().produceMultipleUsers(contact_mail);
		
		for (User contact : contacts)
			sendInvite(contact.getEmail());
		
		_user.addContacts(contacts);
		return null;
	}
	
	@Override
	protected void doDelete() throws Exception {
		if (!_application._userHome.isKnownUser(_request.getParameter("email")))
			throw new BusinessException("Este usuário não está na sua lista de contatos.");
			
		User contact = _application._userHome.produceUser(_request.getParameter("email"));
		_user.removeContact(contact);
	}
	

	private void sendInvite(String contactMail) throws Exception {
		Properties properties = new Properties();
		
		properties.put("from_mail", _user.getEmail());
		properties.put("hash", URLEncoder.encode(new Cryptor().encode(contactMail), "UTF-8"));
		properties.put("app_link", getLinkAplicacao());
		
		_application.scheduleMail(new Mail(contactMail,
				ConviteAcessoTemplate.class.getName(), properties));
	}

}
