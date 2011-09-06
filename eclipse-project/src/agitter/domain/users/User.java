package agitter.domain.users;

import agitter.domain.emails.EmailAddress;

public interface User {

	public EmailAddress email();
	public String screenName();

	public String password();
	public boolean isPassword(String attempt);

	public boolean isInterestedInPublicEvents();
	public void setInterestedInPublicEvents(boolean interested);

}
