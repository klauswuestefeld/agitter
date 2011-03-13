package guardachuva.agitos.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserDTO implements IsSerializable {

	private static final long serialVersionUID = 1L;
	private String email;
	private String name;
	private String userName;
	
	@SuppressWarnings("unused")
	private UserDTO() {
	}
	
	public UserDTO(String name, String userName, String email) {
		this.email = email;
		this.name = name;
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getUserName() {
		return userName;
	}
	
}
