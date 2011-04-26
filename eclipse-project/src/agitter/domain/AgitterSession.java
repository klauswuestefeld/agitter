package agitter.domain;

public interface AgitterSession {

	void logout();
	Object userName();
	
	Events events();

}
