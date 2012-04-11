package agitter.controller.mailing.tests;

import static agitter.domain.emails.EmailAddress.email;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.TestWithMocks;
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
		Map<String, String[]> params = map("email", "ana+1@mail.com", "expires", "-1", "code", "8B2185CF5384FD691EF4AE5CA7D8AF9D789012F2C957A0C7251E6318228C5FF5");  //Obtained by Regression
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
