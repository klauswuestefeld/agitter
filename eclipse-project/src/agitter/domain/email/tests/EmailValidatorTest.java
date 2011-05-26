package agitter.domain.email.tests;



import org.junit.Assert;
import org.junit.Test;

import agitter.domain.email.EmailValidator;



public class EmailValidatorTest extends Assert {
	
	@Test
	public void validateEmail() {
		assertTrue(EmailValidator.validateEmail("altiereslopes@gmail.com"));
		assertTrue(EmailValidator.validateEmail("alt.lop@webgoal.com.br"));
		assertTrue(EmailValidator.validateEmail("mao_ki@gmail.com"));
		assertTrue(EmailValidator.validateEmail("altieres+lopes@gmail.com"));
		
		assertFalse(EmailValidator.validateEmail("altieres"));
		assertFalse(EmailValidator.validateEmail("altieres@asd"));
	}
	
}
