package guardachuva.agitos.shared;

import java.io.Serializable;

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
	
}
