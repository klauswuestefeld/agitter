package agitter.domain.emails.tests;

import org.junit.Test;

import basis.testsupport.CleanTestBase;

import agitter.domain.emails.EmailAddress;
import agitter.domain.emails.EmailExtractor;


public class EmailExtractorTest extends CleanTestBase {

	@Test
	public void mailExtraction() {
		final StringBuffer visited = new StringBuffer(); 
		EmailExtractor.extractAddresses(
				"to	Tatiana Monteiro <tatialmon@gmail.com>," +
				"Solano Esteche <esteche+bana@gmail.com>," +
				"Letícia Elpo <leticiaelpo@yahoo.com.br>," +
				"\"Oliveira, Pedro J\" <pedro.j.oliveira@exxonmobil.com>," +
				"nicole_cbr@hotmail.com," +
				"[Idle] Fulano de Tal <fulano@gmail.com>",
				new EmailExtractor.Visitor() {  @Override public void visit(String name, EmailAddress email) {
					visited.append(name + " <" + email + ">, ");
				}}
		);
		
		assertEquals("Tatiana Monteiro <tatialmon@gmail.com>, " +
				"Solano Esteche <esteche+bana@gmail.com>, " +
				"Letícia Elpo <leticiaelpo@yahoo.com.br>, " +
				"Pedro J <pedro.j.oliveira@exxonmobil.com>, " +
				"null <nicole_cbr@hotmail.com>, " +
				"Fulano de Tal <fulano@gmail.com>, ",
				visited.toString());
	}
	
}
