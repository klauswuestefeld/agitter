package agitter.domain.users;

import static agitter.common.Portal.Facebook;
import static agitter.common.Portal.Twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sneer.foundation.lang.Functor;
import sneer.foundation.lang.exceptions.Refusal;
import utils.Encoders;
import agitter.common.Portal;
import agitter.domain.emails.EmailAddress;

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
		String twitter = linkedAccountUsername(Twitter);
		String facebook = linkedAccountUsername(Facebook);
		
		String id = email().toString();
		if (twitter != null && !twitter.trim().isEmpty())
			id = "@" + twitter;
		else if (facebook != null && !facebook.trim().isEmpty())
			id = facebook;
		
		if (name() != null) 
			return name() + " (" + id + ")"; 
		
		return id;
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
	
	@Override
	public void attemptToSetPassword(String currentPasswordAttempt, String newPassword) 	throws Refusal {
		if (!isPasswordCorrect(currentPasswordAttempt))
			throw new Refusal("Senha atual não confere.");
		setPassword(newPassword);
	}


	static private byte[] hashOf(String password) {
		return HMAC.evaluate(password);
	}

	@Override
	public String picture() {
		for (LinkedAccount acc : linkedAccounts())
			if (acc.imageProfile != null) return acc.imageProfile;
		return null;
	}
	
	@Override
	public void linkAccount(Portal portal, String username, String oauthVerifier, String oauthToken, String imageProfile, String email) {
		LinkedAccount c = new LinkedAccount(portal.name(), username, oauthToken, oauthVerifier, imageProfile, email);
		
		unlinkAccount(portal);
		linkedAccounts().add(c);
	}
	
	private List<LinkedAccount> linkedAccounts() {
		if (linkedAccounts == null) linkedAccounts = new ArrayList<LinkedAccount>(0);
		return linkedAccounts;
	}


	@Override
	public boolean isAccountLinked(Portal portal) {
		return linkedAccountFor(portal) != null;
	}
	@Override
	public String linkedAccountUsername(Portal portal) {
		LinkedAccount acc = linkedAccountFor(portal);
		return acc == null ? "" : acc.userName;
	}
	@Override
	public String linkedAccountImage(Portal portal) {
		LinkedAccount acc = linkedAccountFor(portal);
		return acc == null ? null : acc.imageProfile;
	}
	@Override
	public void unlinkAccount(Portal portal) {
		linkedAccounts().remove(linkedAccountFor(portal));
	}
	private LinkedAccount linkedAccountFor(Portal portal) {
		for (LinkedAccount acc : linkedAccounts())
			if (portal.name().equals(acc.portal)) return acc;
		return null;
	}
	
}
