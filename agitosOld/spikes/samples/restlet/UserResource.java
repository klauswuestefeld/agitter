package samples.restlet;

import org.restlet.resource.Get;

public interface UserResource {
	@Get
	public User getUser();
}
