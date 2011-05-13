package agitter.domain.users;

import java.io.Serializable;

import sneer.foundation.lang.Immutable;

public class Credential extends Immutable implements Serializable {
	
	private final String email;
	private final String userName;
	private final String password;
	private String id;
	private static String EMAIL_SYMBOL = "@";
	
	public Credential(String id, String password){
		this.id = id;
		this.email = extractEmailFromId(id);
		this.userName = extractUserNameFromId(id);
		this.password = password;
	}

	public String getId() {
		return id;
	}
	
	public String getUserName() {
		return this.userName;
	}

	public String getEmail() {
		return this.email;
	}

	public String getPassword() {
		return this.password;
	}
	
	public Boolean isEmailProvided() {
		return IsStringAnEmail(getEmail());
	}
	
	private String extractUserNameFromId(String id) {
		String[] parts = splitId(id);
		return parts[0];
	}

	private String extractEmailFromId(String id) {
		if (IsStringAnEmail(id))
			return id;
		return "";
	}
	
	private Boolean IsStringAnEmail(String id){
		return id.contains(EMAIL_SYMBOL);
	}
	
	private String[] splitId(String id) {
		return id.split(EMAIL_SYMBOL);
	}
}
