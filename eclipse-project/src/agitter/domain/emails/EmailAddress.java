package agitter.domain.emails;

import java.io.Serializable;

import sneer.foundation.lang.Immutable;
import sneer.foundation.lang.exceptions.Refusal;

public class EmailAddress extends Immutable implements Comparable<EmailAddress>, Serializable {

	public static EmailAddress email(String email) throws Refusal {
		return new EmailAddress(email);
	}
	public static EmailAddress certain(String email) {
		try {
			return email(email);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}


	private final String emailAddress;

	
	private EmailAddress(String emailAddress) throws Refusal {
		AddressValidator.validateEmail(emailAddress);
		this.emailAddress = emailAddress.toLowerCase();
	}

	
	@Override
	public String toString() {
		return emailAddress;
	}


	@Override
	public int compareTo(EmailAddress other) {
		return emailAddress.compareTo( other.emailAddress );
	}

}
