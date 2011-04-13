package agitter.shared.rpc;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import agitter.shared.EventDTO;
import agitter.shared.Mail;
import agitter.shared.SessionToken;
import agitter.shared.UserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteApplicationAsync {

	void addContactsToMe(SessionToken session, String contact_mail,
			AsyncCallback<Void> callback);
	
	void importContactsFromService(SessionToken session, 
			List<UserDTO> contactsToImport, String service, AsyncCallback<Void> callback);

	void authenticate(String email, String password,
			AsyncCallback<SessionToken> callback);

	void createEvent(SessionToken session, String description, Date date,
			AsyncCallback<Void> callback);

	void createNewUser(String name, String userName, String password,
			String email, AsyncCallback<SessionToken> asyncCallback);

	void deleteContactForMe(SessionToken session, String email, 
			AsyncCallback<Void> callback);

	void getContactsForMe(SessionToken session, AsyncCallback<List<UserDTO>> callback);

	void getEventsForMe(SessionToken session, AsyncCallback<List<EventDTO>> callback);

	void ignoreProducerForMe(SessionToken session, String email, 
			AsyncCallback<Void> callback);

	void removeEventForMe(SessionToken session, int id, 
			AsyncCallback<Void> callback);

	void logout(SessionToken session, AsyncCallback<Void> callback);
	
	void getLoggedUserOn(SessionToken session, AsyncCallback<UserDTO> callback);
	
	// ScheduledEmails
	void scheduleMail(Mail mail, AsyncCallback<Void> callback);

	void getScheduledMails( AsyncCallback<HashMap<String, Mail>> callback);

	void deleteMail(String key, AsyncCallback<Void> callback);

}
