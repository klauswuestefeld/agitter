package guardachuva.agitos.shared;

import java.util.Date;
import java.util.HashMap;

public interface Application {

	SessionToken createNewUser(String name, String userName,	String password, String email) throws ValidationException, UserAlreadyExistsException;
	SessionToken authenticate(String email, String password) throws UnauthorizedBusinessException;
	UserDTO getLoggedUserOn(SessionToken session) throws UnauthorizedBusinessException;
	void logout(SessionToken session) throws UnauthorizedBusinessException;

	EventDTO[] getEventsForMe(SessionToken session) throws UnauthorizedBusinessException;
	void createEvent(SessionToken session, String description, Date date) throws BusinessException;
	void removeEventForMe(SessionToken session, int id) throws BusinessException;

	void addContactsToMe(SessionToken session, String contact_mail, String linkToApplication) throws ValidationException, Exception;
	UserDTO[] getContactsForMe(SessionToken session) throws UnauthorizedBusinessException;
	void deleteContactForMe(SessionToken session, String email) throws BusinessException;

	void ignoreProducerForMe(SessionToken session, String email) throws ValidationException, BusinessException;

	// ScheduledEmails
	public abstract void scheduleMail(Mail mail);

	public abstract HashMap<String, Mail> getScheduledMails();

	public abstract void deleteMail(String key);


}