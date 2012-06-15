package agitter.controller.mailing.tests;

import static agitter.domain.emails.EmailAddress.email;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import basis.lang.exceptions.Refusal;
import basis.testsupport.TestWithMocks;

import agitter.controller.mailing.SignupEmailController;
import agitter.domain.users.Users;

public class SignupEmailControllerTest extends TestWithMocks {

	private final EmailSenderMock sender = new EmailSenderMock();
	private final Users users = mock(Users.class);
	
	private final SignupEmailController subject = new SignupEmailController(sender, users);
	

	@Test(timeout = 2000)
	public void signupViaEmail() throws Exception {
		checking(new Expectations(){{
			allowing(users).searchByEmail(email("ana+1@mail.com"));
		}});
		subject.initiateSignup(email("ana+1@mail.com"), "password123");
		assertTrue(sender.body().contains("email=ana%2B1%40mail.com"));
		
		checking(new Expectations(){{
			exactly(1).of(users).signup(email("ana+1@mail.com"), "password123");
		}});
		Map<String, String[]> params = map("email", "ana+1@mail.com", "expires", "-1", "code", "22479E7E30281D6519635B0F7641A6EF420E241D9AA271130F593894FBDC1D31");  //Obtained by Regression
		subject.onRestInvocation(params);
	}


	@Test (timeout = 2000, expected = Refusal.class)
	public void invalidRequest() throws Exception {
		Map<String, String[]> params = map("email", "ana@mail.com", "expires", "-1", "code", "InvalidCode:P");
		subject.onRestInvocation(params);
	}

	
	private Map<String, String[]> map(String... keysAndValues) {
		Map<String, String[]> result = new HashMap<String, String[]>();
		int i = 0;
		while (i < keysAndValues.length) {
			String key = keysAndValues[i++];
			String value = keysAndValues[i++];
			result.put(key, new String[]{value});
		}
		return result;
	}

}
