package agitter.domain.users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sneer.foundation.lang.Functor;
import utils.Encoders;
import agitter.domain.emails.EmailAddress;

class LinkedAccount {
	String oauth_verifier;
	String oauth_token;
	String userName;
	String portal;
}

public class UserImpl implements User {

	private static final Functor<String, byte[]> HMAC = Encoders.hmacForKey("QualquerCoiSa675$#");
	
	private EmailAddress email;
	private String name;
	private byte[] passwordHash;
	private boolean hasUnsubscribedFromEmails = false;
	
	private List<LinkedAccount> linkedAccounts;

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

	
	@Override
	public void linkAccount(String portal, String username, String oauthVerifier, String oauthToken) {
		LinkedAccount c = new LinkedAccount();
		c.userName = username;
		c.oauth_token = oauthToken;
		c.oauth_verifier = oauthVerifier;

		unlinkAccount(portal);
		linkedAccounts().add(c);
	}
	
	private List<LinkedAccount> linkedAccounts() {
		if (linkedAccounts == null) linkedAccounts = new ArrayList<LinkedAccount>(0);
		return linkedAccounts;
	}


	@Override
	public boolean isAccountLinked(String portal) {
		return linkedAccountFor(portal) != null;
	}
	@Override
	public String linkedAccountUsername(String portal) {
		LinkedAccount acc = linkedAccountFor(portal);
		return acc == null ? "" : acc.userName;
	}
	@Override
	public void unlinkAccount(String portal) {
		linkedAccounts().remove(linkedAccountFor(portal));
	}
	private LinkedAccount linkedAccountFor(String portal) {
		for (LinkedAccount acc : linkedAccounts())
			if (acc.portal.equals(portal)) return acc;
		return null;
	}
	
}
