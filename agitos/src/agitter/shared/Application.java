package agitter.shared;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.prevayler.bubble.Transaction;

public interface Application {

	@Transaction
	SessionToken createNewUser(String name, String userName, String password, String email) throws ValidationException, UserAlreadyExistsException;

	@Transaction
	SessionToken authenticate(String email, String password) throws UnauthorizedBusinessException;

	UserDTO getLoggedUserOn(SessionToken session) throws UnauthorizedBusinessException;

	void logout(SessionToken session) throws UnauthorizedBusinessException;

	List<EventDTO> getEventsForMe(SessionToken session) throws UnauthorizedBusinessException;
	
	void createEvent(SessionToken session, String description, Date date) throws BusinessException;

	void removeEventForMe(SessionToken session, int id) throws BusinessException;

	void addContactsToMe(SessionToken session, String contact_mail) throws ValidationException, Exception;
	List<UserDTO> getContactsForMe(SessionToken session) throws UnauthorizedBusinessException;
	void deleteContactForMe(SessionToken session, String email) throws BusinessException;

	void importContactsFromService(SessionToken session, List<UserDTO> contactsToImport, String service)  throws UnauthorizedBusinessException, ValidationException;

	void ignoreProducerForMe(SessionToken session, String email) throws ValidationException, BusinessException;
 
	HashMap<String, Mail> getScheduledMails();

	void deleteMail(String key);

}