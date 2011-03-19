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
	
	public UserDTO(String name, String userName, String email) {
		this._email = email;
		this._name = name;
		this._userName = userName;
	}

	public String getEmail() {
		return _email;
	}

	public String getName() {
		return _name;
	}

	public String getUserName() {
		return _userName;
	}

	public static String[] errorsForConstruction(String name, String userName, String password, String email) {
		ArrayList<String> errors = new ArrayList<String>();
		
		if (name != null && !Validations.validateMinLength(name, 3))
			errors.add("O nome deve possuir no mínimo 3 caracteres.");
		
		if (userName != null && !Validations.validateMinLength(userName, 3))
			errors.add("O nome de usuário deve possuir no mínimo 3 caracteres.");
	
		if (password != null && !Validations.validateMinLength(password, 3))
			errors.add("A senha deve possuir no mínimo 3 caracteres.");
	
		if (email != null && !Validations.validateEmail(email))
			errors.add("O email não parece ser válido.");
		
		return errors.toArray(new String[errors.size()]);
	}
	
}
