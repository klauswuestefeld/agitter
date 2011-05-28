package agitter.domain.email;

import java.io.Serializable;

import sneer.foundation.lang.Immutable;
import sneer.foundation.lang.exceptions.Refusal;

public class Address extends Immutable implements Serializable {

	private final String address;

	
	public Address(String address) throws Refusal {
		AddressValidator.validateEmail(address);
		this.address = address;
	}

	
	@Override
	public String toString() {
		return address;
	}

}
