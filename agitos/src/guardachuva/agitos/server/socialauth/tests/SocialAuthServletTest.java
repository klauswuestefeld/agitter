package guardachuva.agitos.server.socialauth.tests;

import guardachuva.agitos.server.socialauth.SocialAuthServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SocialAuthServletTest {

	
	private Mockery mockingContext;
	private HttpServletRequest req;
	private HttpServletResponse res;
	protected HttpSession session;

	@Before
	public void setup() {
		mockingContext = new Mockery();
	}
	
	@Test
	@Ignore
	public void testDoGetHttpServletRequestHttpServletResponse() throws ServletException, IOException {
		req = mockingContext.mock(HttpServletRequest.class);
		res = mockingContext.mock(HttpServletResponse.class);
		session = mockingContext.mock(HttpSession.class);
		

		mockingContext.checking( new Expectations() {
			{
				one(req).getMethod();
				will(returnValue("GET"));

				one(req).getSession();
				will(returnValue(session));

				one(req).getParameter("id");
				will(returnValue(null));

				one(req).getParameter("status");
				will(returnValue(null));
			}
		});
				
		SocialAuthServlet servlet = new SocialAuthServlet();
		servlet.service(req, res);
	}

}
