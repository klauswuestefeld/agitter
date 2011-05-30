package agitter.domain.users;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;

public class UsersImpl implements Users {

	private final List<User> users = new ArrayList<User>();

	@Override
	public List<User> all() {
		return new ArrayList<User>(users);
	}

	@Override
	public User signup(String username, String email, String password) throws Refusal {
		checkParameters(username, email, password);
		checkDuplication(username, email);

		UserImpl result = new UserImpl(username, email, password);
		users.add(result);
		
		int uncommentThis;
//import static infra.logging.LogInfra.getLogger;
//		getLogger(this).info("Signup: " + username + " - email: " + email);
		
		return result;
	}

	@Override
	public User loginWithUsername(String username, String password) throws UserNotFound, InvalidPassword {
		User user = searchByUsername(username);
		return login(user, username, password);
	}


	@Override
	public User loginWithEmail(String email, String password) throws UserNotFound, InvalidPassword {
		User user = searchByEmail(email);
		return login(user, email, password);
	}


	@Override
	public User findByUsername(String username) throws UserNotFound {
		User user = searchByUsername(username);
		checkUser(user, username);
		return user;
	}


	@Override
	public User findByEmail(String email) throws UserNotFound {
		User user = searchByEmail(email);
		checkUser(user, email);
		return user;
	}


	@Override
	public String userEncyptedInfo(User user) {
		return user.username();//TODO - Implement encryption
	}

	@Override
	public void unsubscribe(String userEncryptedInfo) throws UserNotFound {
		//TODO - Implement decrypt
		String username = userEncryptedInfo;
		User user = this.findByUsername(username);
		user.setInterestedInPublicEvents(false);
	}

	private void checkParameters(final String username, final String email, final String password) throws Refusal {
		if( Clock.currentTimeMillis() < new GregorianCalendar( 2011, Calendar.MAY, 28 ).getTimeInMillis() ) {
			//this is a workaround. I was possible to create a user with invalid parameters. This new validation is messing with the transaction playback.
			return;
		}
		if( this.isBlank( username ) ) {
			throw new Refusal( "Username deve ser especificado" );
		}
		if( this.isBlank( email ) ) {
			throw new Refusal( "Email deve ser especificado" );
		}
		if( this.isBlank( password ) ) {
			throw new Refusal( "Password deve ser especificado" );
		}
	}
	
	private boolean isBlank(final String field) {
		return ( field == null || field.trim().isEmpty() );
	}

	private void checkDuplication(String username, String email) throws Refusal {
		if(searchByUsername(username)!=null) {
			throw new Refusal("Já existe usuário cadastrado com este username: "+username);
		}
		if(searchByEmail(email)!=null) { throw new Refusal("Já existe usuário cadastrado com este email: "+email); }
	}

	private void checkUser(User user, String emailOrUsername) throws UserNotFound {
		if(user==null) { throw new UserNotFound("Usuário não encontrado: "+emailOrUsername); }
	}

	private User searchByEmail(String email) {
		for(User candidate : users) { if(candidate.email().equals(email)) { return candidate; } }
		return null;
	}

	private User searchByUsername(String username) {
		for(User candidate : users) { if(candidate.username().equals(username)) { return candidate; } }
		return null;
	}

	private User login(User user, String emailOrUsername, String passwordAttempt) throws UserNotFound, InvalidPassword {
		checkUser(user, emailOrUsername);
		if(!user.isPassword(passwordAttempt)) { throw new InvalidPassword("Senha inválida."); }
		//getLogger().info("Login: "+emailOrUsername);
		return user;
	}

}
