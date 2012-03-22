package agitter.domain.users;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sneer.foundation.lang.Functor;
import utils.Encoders;
import agitter.domain.emails.EmailAddress;

class ConnectedAccount {
	String oauth_verifier;
	String oauth_token;
	String userName;
}

public class UserImpl implements User {

	private static final Functor<String, byte[]> HMAC = Encoders.hmacForKey("QualquerCoiSa675$#");
	
	private EmailAddress email;
	private String name;
	private byte[] passwordHash;
	private boolean hasUnsubscribedFromEmails = false;
	
	private Map<String, ConnectedAccount> linkedOAuthAccounts; 

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
	@Override public boolean hasName() { return name != null && !"".equals(name.trim()); }
	
	
	@Override
	public String screenName() {
		if (name() != null) return name(); 
		return email().toString();
	}

	
	@Override public boolean hasUnsubscribedFromEmails() { return hasUnsubscribedFromEmails; }
	@Override public void setUnsubscribedFromEmails(boolean unsubscribed) { hasUnsubscribedFromEmails = unsubscribed; }


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

	public Map<String, ConnectedAccount> linkedOAuthAccounts() {
		if (linkedOAuthAccounts == null) {
			linkedOAuthAccounts = new HashMap<String, ConnectedAccount>();
		}
		return linkedOAuthAccounts;
	}
	
	@Override
	public void linkAccount(String portal, String username, String oauth_verifier, String oauth_token) {
		ConnectedAccount c = new ConnectedAccount();
		c.userName = username;
		c.oauth_token = oauth_token;
		c.oauth_verifier = oauth_verifier;
		
		linkedOAuthAccounts().put(portal, c);
	}
	
	@Override
	public boolean linkedAccount(String string) {
		return linkedOAuthAccounts().containsKey(string);
	}
	@Override
	public String linkedAccountUsername(String string) {
		if (! linkedAccount(string)) return "";
		return linkedOAuthAccounts().get(string).userName;
	}
	@Override
	public void unlinkAccount(String portal) {
		linkedOAuthAccounts().remove(portal);
	}
}
