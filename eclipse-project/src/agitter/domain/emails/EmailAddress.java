package agitter.domain.emails;

import java.io.Serializable;

import sneer.foundation.lang.Immutable;
import sneer.foundation.lang.exceptions.Refusal;

public class EmailAddress extends Immutable implements Comparable<EmailAddress>, Serializable {

	private final String emailAddress;

	
	public EmailAddress(String emailAddress) throws Refusal {
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
