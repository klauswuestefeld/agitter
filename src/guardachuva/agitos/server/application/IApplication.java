package guardachuva.agitos.server.application;

import guardachuva.agitos.domain.Event;
import guardachuva.agitos.domain.User;
import guardachuva.agitos.shared.BusinessException;
import guardachuva.agitos.shared.UnauthorizedBusinessException;
import guardachuva.agitos.shared.UserAlreadyExistsException;
import guardachuva.agitos.shared.ValidationException;
import guardachuva.mailer.core.Mail;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public interface IApplication {

	public abstract User createUser(String name, String userName,
			String password, String email) throws ValidationException, UserAlreadyExistsException;

	public abstract User authenticate(String email, String password)
			throws UnauthorizedBusinessException;

	public abstract Event[] getEventsFor(User user);

	public abstract Event createEvent(User user, String description, String date)
			throws ParseException, BusinessException;

	public abstract void removeEventFor(User user, int id)
			throws BusinessException;

	public abstract HashMap<String, Mail> getScheduledMails();

	public abstract void deleteMail(String key);

	public abstract void scheduleMail(Mail mail);

	public abstract List<User> addContactsTo(User user, String contact_mail,
			String linkToApplication) throws ValidationException, Exception;

	public abstract void deleteContactFor(User user, String email) throws BusinessException;

	public abstract User[] getContactsFor(User user);

	public abstract void ignoreProducerFor(User user, String email) throws ValidationException, BusinessException;

}