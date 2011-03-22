package guardachuva.agitos.server;

import guardachuva.agitos.server.crypt.Cryptor;
import guardachuva.agitos.server.domain.Event;
import guardachuva.agitos.server.domain.Events;
import guardachuva.agitos.server.domain.Sessions;
import guardachuva.agitos.server.domain.User;
import guardachuva.agitos.server.domain.Users;
import guardachuva.agitos.server.domain.comparators.EventDateTimeComparator;
import guardachuva.agitos.server.domain.comparators.UserEmailComparator;
import guardachuva.agitos.server.mailer.core.ScheduledMailsImpl;
import guardachuva.agitos.server.mailer.templates.ConviteAcessoTemplate;
import guardachuva.agitos.shared.Application;
import guardachuva.agitos.shared.BusinessException;
import guardachuva.agitos.shared.EventDTO;
import guardachuva.agitos.shared.Mail;
import guardachuva.agitos.shared.ScheduledEmails;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.UserDTO;
import guardachuva.agitos.shared.ValidationException;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ApplicationImpl implements Serializable, Application {
	
	private static final long serialVersionUID = 1L;

	private static Clock clock = SystemClock.getInstance();

	private static Application _Instance=null;
	
	private final Users _users = new Users();
	private final Events _events = new Events();
	private final ScheduledEmails _scheduledMails = new ScheduledMailsImpl();
	private final Sessions _sessions = new Sessions();
	
	public ApplicationImpl() {
		setupInstance();
	}

	@Override
	public SessionToken createNewUser(String name, String userName, String password, String email) throws ValidationException, UserAlreadyExistsException {
		User user;
		if (!_users.isKnownUser(email))
			user = _users.produceUser(name, userName, password, email);
		else // if user already exists, he is _probably_ a contact trying to get registered
			user = tryToRegisterUser(name, userName, password, email);
		
		return _sessions.create(user);
	}

	private User tryToRegisterUser(String name, String userName, String password,
			String email) throws ValidationException,
			UserAlreadyExistsException {
		User user = _users.produceUser(email);
		if (user.isRegistered())
			throw new UserAlreadyExistsException();
		
		user.registerMe(name, userName, password);
		return user;
	}

	@Override
	public SessionToken authenticate(String email, String password) throws UnauthorizedBusinessException {
		return _sessions.create(_users.authenticate(email, password));
	}

	@Override
	public EventDTO[] getEventsForMe(SessionToken session) throws UnauthorizedBusinessException {
		assertValidSession(session);

		User user = _sessions.getLoggedUserOn(session);

		long twoHours = 2*60*60*1000;
		Date nowMinusTwoHours = new Date(clock.millis() - twoHours);
		ArrayList<Event> eventsList = user.listEventsSince(nowMinusTwoHours);
		Event[] events = eventsList.toArray(new Event[eventsList.size()]);
		Arrays.sort(events, new EventDateTimeComparator());
		
		EventDTO[] eventsDTOArray = toEventsDTO(events);
		return eventsDTOArray;
	}


	@Override
	public void createEvent(SessionToken session, String description, Date date) throws BusinessException {
		assertValidSession(session);

		// TODO: my(Clock) deve ter a data correta da chamada original.
		// Não tem tanto problema usar a data atual porque eventos antigos não são exibidos.
		Date now = clock.date();
		boolean dateInPast = date.before(now);
		
		if (dateInPast)
			throw new BusinessException("Você não pode criar agitos no passado.");
		
		 _events.createFor(
			_sessions.getLoggedUserOn(session),
			description,
			date);
	}

	@Override
	public void removeEventForMe(SessionToken session, int id) throws BusinessException {
		assertValidSession(session);
		User user = _sessions.getLoggedUserOn(session);
		_events.removeFor(user, id);		
	}

	@Override
	public void addContactsToMe(SessionToken session, String contact_mail, String linkToApplication) throws Exception {
		assertValidSession(session);
		
		User user = _sessions.getLoggedUserOn(session);

		List<User> contacts = _users.produceMultipleUsers(contact_mail);
		
		for (User contact : contacts)
			sendInvite(user, contact.getEmail(), linkToApplication);
		
		user.addContacts(contacts);
	}

	private void sendInvite(User user, String contactMail, String linkToApplication) throws Exception {

		Mail mail = new Mail(contactMail,
				ConviteAcessoTemplate.class.getName());
		mail.set("from_mail", user.getEmail());
		mail.set("hash", URLEncoder.encode(new Cryptor().encode(contactMail), "UTF-8"));
		mail.set("app_link", linkToApplication!=null ? linkToApplication : "");
		
		scheduleMail(mail);
	}

	@Override
	public void deleteContactForMe(SessionToken session, String email) throws BusinessException {
		assertValidSession(session);

		User user = _sessions.getLoggedUserOn(session);

		if (!_users.isKnownUser(email))
				throw new BusinessException("Este usuário não está na sua lista de contatos.");
				
			User contact = _users.produceUser(email);
			user.removeContact(contact);
		}

		@Override
		public UserDTO[] getContactsForMe(SessionToken session) throws UnauthorizedBusinessException {
			assertValidSession(session);

			User loggedUser = _sessions.getLoggedUserOn(session);
			
			User[] users = loggedUser.getContacts().toArray(new User[loggedUser.getContacts().size()]);
			Arrays.sort(users, new UserEmailComparator());
			
			return toUsersDTO(users);
		}

		@Override
		public void ignoreProducerForMe(SessionToken session, String email) throws BusinessException {
			assertValidSession(session);
			User user = _sessions.getLoggedUserOn(session);		
			User producer = _users.produceUser(email);
			user.ignoreProducer(producer);
		}

		@Override
		public void logout(SessionToken session) throws UnauthorizedBusinessException {
			assertValidSession(session);
			_sessions.logout(session);
		}

		@Override
		public UserDTO getLoggedUserOn(SessionToken session) throws UnauthorizedBusinessException {
			assertValidSession(session);
			return toUserDTO(_sessions.getLoggedUserOn(session));
		}

		private UserDTO[] toUsersDTO(User[] users) {
			ArrayList<UserDTO> usersDTO = new ArrayList<UserDTO>();
			for (User user : users) {
				usersDTO.add(toUserDTO(user));
			}
			return usersDTO.toArray(new UserDTO[usersDTO.size()]);
		}

		private UserDTO toUserDTO(User user) {
			return new UserDTO(user.getName(), user.getUserName(), user.getEmail());
		}
		
		private EventDTO[] toEventsDTO(Event[] events) {
			ArrayList<EventDTO> eventsDTO = new ArrayList<EventDTO>();
			for (Event event : events) {
				eventsDTO.add(new EventDTO(event.getId(), event.getDescription(), 
						event.getDate(), toUserDTO(event.getModerator())));
			}
			return eventsDTO.toArray(new EventDTO[eventsDTO.size()]);
		}


		private void assertValidSession(SessionToken session)
				throws UnauthorizedBusinessException {
			if(!_sessions.isValid(session))
				throw new UnauthorizedBusinessException("A Sessão não é válida");
	}

	// API Scheduled Mails
	
	@Override
	public HashMap<String, Mail> getScheduledMails() {
		return _scheduledMails.getScheduledMails();
	}

	@Override
	public void deleteMail(String key) {
		_scheduledMails.deleteMail(key);
	}

	@Override
	public void scheduleMail(Mail mail) {
		_scheduledMails.scheduleMail(mail);
	}

	public static Application GetInstance() {
		if(_Instance==null)
			throw new IllegalStateException("Tentando usar singleton do ApplicationImpl antes de ser inicializado");
		return _Instance;
	}

	private void setupInstance() {
		/*
		if(_Instance!=null) {
			throw new IllegalStateException("ApplicationImpl sendo instanciada mais de uma vez");
		}/**/
		_Instance = this;
	}	
}
