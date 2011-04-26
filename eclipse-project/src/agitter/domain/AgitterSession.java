package agitter.domain;

public interface AgitterSession {

	String userName();
	void logout();
	
	Events events();

}
