package agitter.domain.users;

public interface User {

	public String username();

	public String email();

	public boolean isPassword(String password);

	public String password();

}
