package agitter.ui.presenter;


public class Credential {
	
	private static final String EMAIL_SYMBOL = "@";

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
	
	
	public String username() {
		return !isEmailProvided()
			? emailOrUsername
			: "";
	}

	
	public String email() {
		return isEmailProvided()
			? emailOrUsername
			:"";
	}

	
	public String password() {
		return password;
	}
	
	
	public Boolean isEmailProvided() {
		return isEmail(emailOrUsername);
	}
	
	
	private Boolean isEmail(String string){
		return string.contains(EMAIL_SYMBOL);
	}


	public String suggestedUserName() {
		return isEmailProvided() 
				? emailOrUsername.split(EMAIL_SYMBOL)[0]
				: username();
	}
}
