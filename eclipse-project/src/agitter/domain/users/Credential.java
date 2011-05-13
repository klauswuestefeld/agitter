package agitter.domain.users;

import java.io.Serializable;

import sneer.foundation.lang.Immutable;

public class Credential extends Immutable implements Serializable {
	
	private static String EMAIL_SYMBOL = "@";

	private final String emailOrUsername;
	private final String password;
	
	
	public Credential(String emailOrUsername, String password){
		this.emailOrUsername = emailOrUsername;
		this.password = password;
	}

	
	@Override
	public String toString() {
		return emailOrUsername;
	}
	
	
	public String getUserName() {
		String[] parts = emailOrUsername.split(EMAIL_SYMBOL);
		return parts[0];
	}

	
	public String getEmail() {
		return isEmailProvided()
			? emailOrUsername
			:"";
	}

	
	public String getPassword() {
		return password;
	}
	
	
	public Boolean isEmailProvided() {
		return isEmail(emailOrUsername);
	}
	
	
	private Boolean isEmail(String string){
		return string.contains(EMAIL_SYMBOL);
	}
}
