package agitter.domain;




public class UserImpl implements User {

	private final String _name;
	private final String _email;
	private final String _password;

	
	public UserImpl(String name, String email, String password) {
		_name = name;
		_email = email;
		_password = password;
	}


	@Override
	public String name() {
		return _name;
	}


	@Override
	public String email() {
		return _email;
	}


	@Override
	public boolean isPassword(String password) {
		return _password.equals(password);
	}

}
