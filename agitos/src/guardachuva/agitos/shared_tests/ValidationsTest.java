package guardachuva.agitos.shared_tests;


import guardachuva.agitos.shared.Validations;

import org.junit.Assert;
import org.junit.Test;


public class ValidationsTest extends Assert {
	
	@Test
	public void validateEmail() {
		assertTrue(Validations.validateEmail("altiereslopes@gmail.com"));
		assertTrue(Validations.validateEmail("alt.lop@webgoal.com.br"));
		assertTrue(Validations.validateEmail("mao_ki@gmail.com"));
		assertTrue(Validations.validateEmail("altieres+lopes@gmail.com"));
		
		assertFalse(Validations.validateEmail("altieres"));
		assertFalse(Validations.validateEmail("altieres@asd"));
		
	}
	
	@Test
	public void validateEmailAndOptinalName() {
		assertTrue(Validations.validateEmailAndOptinalName("altiereslopes@gmail.com"));
		assertTrue(Validations.validateEmailAndOptinalName("\"Altieres Lopes\" <alt.lop@webgoal.com.br>"));
		assertTrue(Validations.validateEmailAndOptinalName("\"Maoki\" <mao_ki@gmail.com>"));
		assertTrue(Validations.validateEmailAndOptinalName("altieres+lopes@gmail.com"));
		
		assertFalse(Validations.validateEmailAndOptinalName("altieres"));
		assertFalse(Validations.validateEmailAndOptinalName("altieres@asd"));
		assertFalse(Validations.validateEmailAndOptinalName("\"Altieres Lopes\" alt.lop@webgoal.com.br"));
		assertFalse(Validations.validateEmailAndOptinalName("\"Maoki\"mao_ki@gmail.com"));
		assertFalse(Validations.validateEmailAndOptinalName("Maoki mao_ki@gmail.com"));
	}
	
	@Test
	public void validateMultipleEmailAndOptinalName() {
		assertTrue(Validations.validateMultipleEmailAndOptinalName("altiereslopes@gmail.com"));
		assertTrue(Validations.validateMultipleEmailAndOptinalName("\"Altieres Lopes\" <alt.lop@webgoal.com.br>"));
		assertTrue(Validations.validateMultipleEmailAndOptinalName("\"Maoki\" <mao_ki@gmail.com>, altieres+lopes@gmail.com"));
		assertTrue(Validations.validateMultipleEmailAndOptinalName("altiereslopes@gmail.com, \"Altieres Lopes\" <alt.lop@webgoal.com.br>, \"Maoki\" <mao_ki@gmail.com>"));
		
		assertFalse(Validations.validateMultipleEmailAndOptinalName("\"Maoki\" <mao_ki@gmail.com>altieres+lopes@gmail.com"));
		assertTrue(Validations.validateMultipleEmailAndOptinalName("altiereslopes@gmail.com,\"Altieres Lopes\" <alt.lop@webgoal.com.br>"));
	}

	@Test
	public void validateMinLength() {
		assertTrue(Validations.validateMinLength("Altieres Lopes", 10));
		assertTrue(Validations.validateMinLength("Alti @ + - . eres", 5));
		assertTrue(Validations.validateMinLength("Al", 2));
		assertTrue(Validations.validateMinLength("  Altie  ", 5));
		
		assertFalse(Validations.validateMinLength("  Alt  ", 4));
		assertFalse(Validations.validateMinLength("Al+  ", 4));
	}
	
	@Test
	public void validateDate() {
		assertTrue(Validations.validateDate("25/12/1981"));
		assertTrue(Validations.validateDate("12/1/1990"));
		assertTrue(Validations.validateDate("31/07/2008"));
		assertTrue(Validations.validateDate("01/01/2011"));
		
		assertFalse(Validations.validateDate("01/0a/2009"));
		assertFalse(Validations.validateDate("0003/2009"));
		assertFalse(Validations.validateDate("hoje"));
	}
	
	@Test
	public void validateTime() {
		assertTrue(Validations.validateTime("10:30"));
		assertTrue(Validations.validateTime("0:01"));
		
		assertFalse(Validations.validateTime("10:a3"));
		assertFalse(Validations.validateTime("1030"));
		assertFalse(Validations.validateTime("agora"));
	}

}
