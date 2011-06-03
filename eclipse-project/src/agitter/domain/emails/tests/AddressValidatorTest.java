package agitter.domain.emails.tests;

import org.junit.Assert;
import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.emails.AddressValidator;

public class AddressValidatorTest extends Assert {
	
	@Test
	public void validateEmail() throws Refusal {
		AddressValidator.validateEmail("altiereslopes@gmail.com");
		AddressValidator.validateEmail("alt.lop@webgoal.com.br");
		AddressValidator.validateEmail("mao_ki@gmail.com");
		AddressValidator.validateEmail("altieres+lopes@gmail.com");
		
		try { AddressValidator.validateEmail("altieres"); fail(); } catch (Refusal expected) { }
		try { AddressValidator.validateEmail("altieres@asd"); fail(); } catch (Refusal expected) { }
	}
	
}
