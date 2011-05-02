package agitter.domain;


public interface User {

	String name();

	String email();

	boolean isPassword(String password);

}
