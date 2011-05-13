package agitter.domain.users;


public class UserImpl implements User {

	private final String _username;
	private final String _email;
	private final String _password;

	
	public UserImpl(String username, String email, String password) {
		_username = username;
		_email = email;
		_password = password;
	}


	@Override
	public String username() {
		return _username;
	}


	@Override
	public String email() {
		return _email;
	}


	@Override
	public boolean isPassword(String password) {
		return _password.equals(password);
	}


	@Override
	public String password() {
		return _password;
	}

}
