
package agitter.domain.emails;

import sneer.foundation.lang.Immutable;

public class EmailDestination extends Immutable {

	private final String name;
	private final EmailAddress emailAddress;
	
	public EmailDestination(EmailAddress emailAddress) {
		this.name = emailAddress.toString(); 
		this.emailAddress = emailAddress;
	}
	
	public EmailDestination(final String name, EmailAddress emailAddress) {
		this.name = name; 
		this.emailAddress = emailAddress;
	}
	
	public String getName() {
		return name;
	}
	
	public EmailAddress getEmailAddress() {
		return emailAddress;
	}

}
