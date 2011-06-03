package agitter.domain.email;

import java.io.Serializable;

import sneer.foundation.lang.Immutable;
import sneer.foundation.lang.exceptions.Refusal;

public class EmailAddress extends Immutable implements Serializable {

	private final String emailAddress;

	
	public EmailAddress(String emailAddress) throws Refusal {
		AddressValidator.validateEmail(emailAddress);
		this.emailAddress = emailAddress;
	}

	
	@Override
	public String toString() {
		return emailAddress;
	}

}
