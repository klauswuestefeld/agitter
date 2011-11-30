package agitter.ui.helper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HTMLFormatterTest {

	HTMLFormatter formatter = new HTMLFormatter();
	
	public void equals(String formatted, String test) {
		assertEquals(formatted, formatter.makeClickable(test));
	} 
	
	@Test
	public void testSimpleReplacement() {
		equals("<a href='http://teste.com.br' target='_new'>http://teste.com.br</a>", 
				"http://teste.com.br");
		
		equals("Hi this <a href='http://teste.com.br' target='_new'>http://teste.com.br</a> is a test.", 
			   "Hi this http://teste.com.br is a test.");		
	}


	@Test
	public void testHTTPS() {
		equals("Hi this <a href='https://teste.com' target='_new'>https://teste.com</a>.",
			   "Hi this https://teste.com."); 
	}
	
	@Test
	public void testFTP() {
		equals("Hi this <a href='ftp://teste.com' target='_new'>ftp://teste.com</a>.",
			   "Hi this ftp://teste.com."); 
	}
	
	@Test
	public void testDoubleURL() {
		equals("Hi this <a href='http://teste.com' target='_new'>http://teste.com</a> is a test <a href='http://teste.com.br' target='_new'>http://teste.com.br</a>.",
			   "Hi this http://teste.com is a test http://teste.com.br."); 
	}
	
	@Test
	public void testMail() {
		equals("Hi this <a href='mailto:vitor@vitorpamplona.com'>vitor@vitorpamplona.com</a>.",
			   "Hi this vitor@vitorpamplona.com."); 
	}
	
	@Test
	public void testHTMLedMail() {
		equals("Hi this <a href='mailto:vitor@vitorpamplona.com'>vitor@vitorpamplona.com</a>.",
			   "Hi this <a href='mailto:vitor@vitorpamplona.com'>vitor@vitorpamplona.com</a>."); 
	}
	
	@Test
	public void testWWW() {
		equals("Hi this <a href='http://www.teste.com' target='_new'>www.teste.com</a>.",
			   "Hi this www.teste.com."); 
	}
	
	@Test
	public void testNpWWW() {
		equals("Hi this <a href='http://teste.com' target='_new'>teste.com</a>.",
			   "Hi this teste.com."); 
	}
	
	@Test
	public void testNothing() {
		equals("Hi this teste. com. Hey you should do nothing here",
			   "Hi this teste. com. Hey you should do nothing here"); 
	}	

	
	@Test
	public void testReplaceAllOutsideHTML() {
		assertEquals("<a href='teste.com.br'>teste</a>", 
				formatter.replaceOutsideATAG(
						"<a href='teste.com.br'>teste</a>", "teste", "oi"));
		
		assertEquals("ddd oi <a href='teste.com.br'>teste</a>", 
				formatter.replaceOutsideATAG(
						"ddd teste <a href='teste.com.br'>teste</a>", "teste", "oi"));
		
		assertEquals("ddd oi <a href='teste.com.br'>teste</a> oi", 
				formatter.replaceOutsideATAG(
						"ddd teste <a href='teste.com.br'>teste</a> teste", "teste", "oi"));		
	}
	
	
}
