package guardachuva.agitos.shared;

import java.util.Date;

public interface Application {

	public abstract SessionToken createNewUser(String name, String userName,
			String password, String email) throws ValidationException, UserAlreadyExistsException;

	public abstract SessionToken authenticate(String email, String password)
			throws UnauthorizedBusinessException;

	public abstract EventDTO[] getEventsForMe(SessionToken session) throws UnauthorizedBusinessException;

	public abstract void createEvent(SessionToken session, String description, Date date)
			throws BusinessException;

	public abstract void removeEventForMe(SessionToken session, int id)
			throws BusinessException;

	public abstract void addContactsToMe(SessionToken session, String contact_mail,
			String linkToApplication) throws ValidationException, Exception;

	public abstract void deleteContactForMe(SessionToken session, String email) throws BusinessException;

	public abstract UserDTO[] getContactsForMe(SessionToken session) throws UnauthorizedBusinessException;

	public abstract void ignoreProducerForMe(SessionToken session, String email) throws ValidationException, BusinessException;

	public void logout(SessionToken session) throws UnauthorizedBusinessException;

	public abstract UserDTO getLoggedUserOn(SessionToken session) throws UnauthorizedBusinessException;

}