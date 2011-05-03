package agitter.domain;

public interface User {

	public String name();

	public String email();

	public boolean isPassword(String password);

}
