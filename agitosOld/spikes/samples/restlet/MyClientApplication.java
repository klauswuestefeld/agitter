package samples.restlet;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

public class MyClientApplication {

	public static void main(String[] args) throws ResourceException, IOException {
		ClientResource cr = new  ClientResource("http://localhost:8182/users/1");

		UserResource resource = cr.wrap(UserResource.class);
		User user = resource.getUser();
		 
		if (user != null) {
		    System.out.println("nome: " + user.getNome());
		    System.out.println("idade: " + user.getIdade());
		}
		
		cr.get(MediaType.APPLICATION_JSON).write(System.out);
	}
	
}
