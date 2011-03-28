package guardachuva.agitos.server.socialauth.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import guardachuva.agitos.server.socialauth.SocialAuthServlet;
import guardachuva.agitos.server.utils.Flash;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebRequest;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

public class SocialAuthServletTest {

	private ServletRunner runner;
	private ServletUnitClient client;

	@Before
	public void setup() {
	}

	@After
	public void teardown() {
		runner.shutDown();
	}

	@Test
	public void testInvalidProviderRequested() throws Exception {

		givenThereIsAnAuthSocialServletRunning();

		whenSomeoneRequestToAuthenticateAt("InvalidProvider");

		thenUserShouldHaveBeenRedirectedToAgitos();

		andThereShouldBeAnErrorAboutInvalidProvider();
	}

	@Test
	@Ignore
	public void testSucessfullGoogleContactImport() throws Exception {

		givenThereIsAnAuthSocialServletRunning();
		whenSomeoneRequestToAuthenticateAt("google");
		thenTheUserShouldBeAskToAuthenticatedHimselfAtTheServiceSite();

		thenUserShouldHaveBeenRedirectedToAgitos();
		assertThat(flash().errors().size(), equalTo(0));
		// whenTheUserAuthenticatesSucessfully();
		// thenTheUsersContactsAtTheServiceShouldBeImpoted();
		// andTheUserShouldBe
	}

	private void givenThereIsAnAuthSocialServletRunning() throws IOException,
			SAXException {
		runner = new ServletRunner();
		runner.registerServlet("/agitosweb/social_auth",
				SocialAuthServlet.class.getName());
		runner.registerServlet("/*", AgitosServlet.class.getName());
		runner.registerServlet("/accounts/o8/ud",
				GoogleAuthServlet.class.getName());
	}

	private void whenSomeoneRequestToAuthenticateAt(final String service)
			throws Exception {
		client = runner.newClient();
		/*
		 * client.addClientListener(new WebClientListener() {
		 * 
		 * @Override public void responseReceived(WebClient arg0, WebResponse
		 * arg1) { System.out.println("Response received: " + arg1); }
		 * 
		 * @Override public void requestSent(WebClient arg0, WebRequest arg1) {
		 * System.out.println("Request sent: " + arg1); } }); /*
		 */
		WebRequest request = new GetMethodWebRequest(
				"http://test.vagaloom.com/agitosweb/social_auth");
		request.setParameter("id", service);
		client.getResponse(request);
	}

	private Flash flash() {
		return Flash.flash(client.getSession(false));
	}

	private void andThereShouldBeAnErrorAboutInvalidProvider() {
		assertThat(
				flash().errors().get(0),
				equalTo("Problema tentando autenticar-se usando InvalidProvider"));
	}

	private void thenUserShouldHaveBeenRedirectedToAgitos() {
		assertThat(client.getCurrentPage().getURL().toString(),
				equalTo("http://test.vagaloom.com/index.html?#meus_agitos"));
	}

	private void thenTheUserShouldBeAskToAuthenticatedHimselfAtTheServiceSite() {
		// TODO Auto-generated method stub

	}

}
