package agitter.domain.users;

public interface User {

	public String username();
	public String email();
	public String fullName();

	public String password();
	public boolean isPassword(String password);

	public boolean isInterestedInPublicEvents();
	public void setInterestedInPublicEvents(boolean interested);

}
