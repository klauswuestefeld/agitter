package agitter.domain.users;

import java.util.Arrays;

import sneer.foundation.lang.Functor;
import utils.Encoders;
import agitter.domain.emails.EmailAddress;


public class UserImpl implements User {

	private static final Functor<String, byte[]> HMAC = Encoders.hmacForKey("QualquerCoiSa675$#");
	
	private EmailAddress email;
	private String name;
	@Deprecated	private String password = OLD_PASSWORD_FOR_MIGRATION_TEST; //Deprecated 2012-03-07 Klaus
	@Deprecated public static String OLD_PASSWORD_FOR_MIGRATION_TEST; //Deprecated 2012-03-07 Klaus
	private byte[] passwordHash;
	private boolean isSubscribedToEmails = true;

	public UserImpl(EmailAddress email, String password) {
		this.email = email;
		if (password != null)
			setPassword(password);
	}


	@Override
	public EmailAddress email() {
		return email;
	}


	@Override public String name() { return name; }
	@Override public void setName(String name) { this.name = name; }
	
	
	@Override
	public String screenName() {
		if (name() != null) return name(); 
		return email().toString();
	}

	
	@Override public boolean isSubscribedToEmails() { return isSubscribedToEmails; }
	@Override public void setSubscribedToEmails(boolean subscribed) { isSubscribedToEmails = subscribed; }


	@Override
	public String toString() {
		return screenName();
	}

	
	@Override
	public boolean hasSignedUp() {
		return passwordHash != null;
	}


	@Override
	public boolean isPasswordCorrect(String passwordAttempt) {
		return hasSignedUp() && Arrays.equals(passwordHash, hashOf(passwordAttempt));
	}
	
	
	@Override
	public void setPassword(String password) {
		passwordHash = hashOf(password);
	}


	static private byte[] hashOf(String password) {
		return HMAC.evaluate(password);
	}
	
	
	void migrateSchemaIfNecessary() {
		if (passwordHash != null) return;
		if (password == null) return;
		setPassword(password);
	}


	public void setOldPasswordForMigrationTest(String password) {
		this.password = password;
	}

}
