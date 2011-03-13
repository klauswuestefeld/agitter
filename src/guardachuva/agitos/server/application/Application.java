package guardachuva.agitos.server.application;

import static sneer.foundation.environments.Environments.my;
import guardachuva.agitos.crypt.Cryptor;
import guardachuva.agitos.domain.Event;
import guardachuva.agitos.domain.User;
import guardachuva.agitos.domain.comparators.EventDateTimeComparator;
import guardachuva.agitos.domain.comparators.UserEmailComparator;
import guardachuva.agitos.server.application.homes.EventHome;
import guardachuva.agitos.server.application.homes.UserHome;
import guardachuva.agitos.shared.BusinessException;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.ValidationException;
import guardachuva.mailer.core.Mail;
import guardachuva.mailer.core.ScheduledMails;
import guardachuva.mailer.templates.ConviteAcessoTemplate;

import java.io.Serializable;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import sneer.bricks.hardware.clock.Clock;


public class Application implements Serializable, IApplication {
	
	private static final long serialVersionUID = 1L;
	
	private final UserHome _users = new UserHome();
	private final EventHome _events = new EventHome();
	private final ScheduledMails _scheduledMails = new ScheduledMails();
	
	/* (non-Javadoc)
	 * @see guardachuva.agitos.server.application.IApplication#createUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public User createUser(String name, String userName, String password, String email) throws ValidationException, UserAlreadyExistsException {
		if(_users.isKnownUser(email))
			throw new UserAlreadyExistsException("Usuário já existente.");
		return _users.produceUser(name, userName, password, email);
	}

	/* (non-Javadoc)
	 * @see guardachuva.agitos.server.application.IApplication#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public User authenticate(String email, String password) throws UnauthorizedBusinessException {
		return _users.authenticate(email, password);
	}

	/* (non-Javadoc)
	 * @see guardachuva.agitos.server.application.IApplication#getEventsFor(guardachuva.agitos.domain.User)
	 */
	@Override
	public Event[] getEventsFor(User user) {
		long twoHours = 2*60*60*1000;
		Date nowMinusTwoHours = new Date(my(Clock.class).time().currentValue() - twoHours);
		System.out.println(nowMinusTwoHours.toString());
		ArrayList<Event> eventsList = user.listEventsSince(nowMinusTwoHours);
		Event[] eventsArray = (Event[])  eventsList.toArray(new Event[eventsList.size()]);
		
		Arrays.sort(eventsArray, new EventDateTimeComparator());
		return eventsArray;
	}

	/* (non-Javadoc)
	 * @see guardachuva.agitos.server.application.IApplication#createEvent(guardachuva.agitos.domain.User, java.lang.String, java.lang.String)
	 */
	@Override
	public Event createEvent(User user, String description, String date) throws ParseException, BusinessException {
		Date date1 = DateTimeUtils.strToDate(date);
		// TODO: my(Clock) deve ter a data correta da chamada original.
		// Não tem tanto problema usar a data atual porque eventos antigos não são exibidos.
		Date now = new Date(my(Clock.class).time().currentValue());
		System.out.println(now.toString() + " / " + date1.toString());
		boolean dateInPast = date1.before(now);
		
		if (dateInPast)
			throw new BusinessException("Você não pode criar agitos no passado.");
		
		return _events.createFor(
			user,
			description,
			date1);
	}

	/* (non-Javadoc)
	 * @see guardachuva.agitos.server.application.IApplication#removeEventFor(guardachuva.agitos.domain.User, int)
	 */
	@Override
	public void removeEventFor(User user, int id) throws BusinessException {
		_events.removeFor(user, id);		
	}

	// API Scheduled Mails
	
	/* (non-Javadoc)
	 * @see guardachuva.agitos.server.application.IApplication#getScheduledMails()
	 */
	@Override
	public HashMap<String, Mail> getScheduledMails() {
		return _scheduledMails.getScheduledMails();
	}

	/* (non-Javadoc)
	 * @see guardachuva.agitos.server.application.IApplication#deleteMail(java.lang.String)
	 */
	@Override
	public void deleteMail(String key) {
		_scheduledMails.deleteMail(key);
	}

	/* (non-Javadoc)
	 * @see guardachuva.agitos.server.application.IApplication#scheduleMail(guardachuva.mailer.core.Mail)
	 */
	@Override
	public void scheduleMail(Mail mail) {
		_scheduledMails.scheduleMail(mail);
	}

	@Override
	public List<User> addContactsTo(User user, String contact_mail, String linkToApplication) throws Exception {
		List<User> contacts = _users.produceMultipleUsers(contact_mail);
		
		for (User contact : contacts)
			sendInvite(user, contact.getEmail(), linkToApplication);
		
		user.addContacts(contacts);
		return contacts;
	}

	private void sendInvite(User user, String contactMail, String linkToApplication) throws Exception {
		Properties properties = new Properties();
		
		properties.put("from_mail", user.getEmail());
		properties.put("hash", URLEncoder.encode(new Cryptor().encode(contactMail), "UTF-8"));
		properties.put("app_link", linkToApplication);
		
		scheduleMail(new Mail(contactMail,
				ConviteAcessoTemplate.class.getName(), properties));
	}

	@Override
	public void deleteContactFor(User user, String email) throws BusinessException {
		if (!_users.isKnownUser(email))
			throw new BusinessException("Este usuário não está na sua lista de contatos.");
			
		User contact = _users.produceUser(email);
		user.removeContact(contact);
	}

	@Override
	public User[] getContactsFor(User user) {
		User[] contacts = (User[]) user.getContacts().toArray(new User[user.getContacts().size()]);
		Arrays.sort(contacts, new UserEmailComparator());
		return contacts;
	}

	@Override
	public void ignoreProducerFor(User user, String email) throws BusinessException {
		User producer = _users.produceUser(email);
		user.ignoreProducer(producer);
	}

}

