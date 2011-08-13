package agitter.domain.users;

import agitter.domain.emails.EmailAddress;

public interface User {

	public String username();
	public EmailAddress email();
	public String fullName();

	public String password();
	public boolean isPassword(String attempt);

	public boolean isInterestedInPublicEvents();
	public void setInterestedInPublicEvents(boolean interested);

}
