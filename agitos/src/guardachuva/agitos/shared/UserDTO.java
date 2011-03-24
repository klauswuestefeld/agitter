package guardachuva.agitos.shared;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;


public class UserDTO implements Serializable, IsSerializable  {

	private static final long serialVersionUID = 1L;
	private String _email;
	private String _name;
	private String _userName;
	
	@SuppressWarnings("unused")
	private UserDTO() {
	}
	
	public UserDTO(String name, String userName, String email) throws ValidationException {
		this._email = email;
		this._name = name;
		this._userName = userName;
		if (email == null || !Validations.validateEmail(email))
			throw new ValidationException("Usuario deve ter email valido");
	}

	public String getEmail() {
		return _email;
	}

	public String getName() {
		return _name != null ? _name : _email;
	}

	public String getUserName() {
		return _userName;
	}

	public static String[] errorsForConstruction(String name, String userName, String password, String email) {
		ArrayList<String> errors = new ArrayList<String>();
		
		if (name == null || !Validations.validateMinLength(name, 3))
			errors.add("O nome deve possuir no mínimo 3 caracteres.");
		
		if (userName == null || !Validations.validateMinLength(userName, 3))
			errors.add("O nome de usuário deve possuir no mínimo 3 caracteres.");
	
		if (email == null || !Validations.validateEmail(email))
			errors.add("O email não parece ser válido.");
		
		if (password != null && !Validations.validateMinLength(password, 3))
			errors.add("A senha deve possuir no mínimo 3 caracteres.");
		
		return errors.toArray(new String[errors.size()]);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_email == null) ? 0 : _email.hashCode());
		result = prime * result + ((_name == null) ? 0 : _name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		if (_email == null) {
			if (other._email != null)
				return false;
		} else if (!_email.equals(other._email))
			return false;
		if (_name == null) {
			if (other._name != null)
				return false;
		} else if (!_name.equals(other._name))
			return false;
		return true;
	}
	
}
