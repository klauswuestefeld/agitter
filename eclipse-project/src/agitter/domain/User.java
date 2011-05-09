package agitter.domain;

public interface User {

	public String username();

	public String email();

	public boolean isPassword(String password);

}
