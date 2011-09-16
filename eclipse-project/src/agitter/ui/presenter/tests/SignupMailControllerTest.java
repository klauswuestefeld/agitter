package agitter.ui.presenter.tests;

import static agitter.domain.emails.EmailAddress.email;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sneer.foundation.testsupport.TestWithMocks;
import agitter.domain.users.Users;
import agitter.ui.mailing.tests.MailSenderMock;
import agitter.ui.presenter.SignupMailController;

public class SignupMailControllerTest extends TestWithMocks {

	private final MailSenderMock sender = new MailSenderMock();
	private final Users users = mock(Users.class);
	
	private final SignupMailController subject = new SignupMailController(sender, users);
	
	
	@Test (timeout = 2000)
	public void signupViaMail() throws Exception {
		checking(new Expectations(){{
			exactly(1).of(users).signup(email("ana@mail.com"), "password123"); inSequence();
		}});
		
		subject.initiateSignup(email("ana@mail.com"), "password123");
		assertTrue(sender.body().contains("email=ana@mail.com")); //Obtained by Regression
		
		Map<String, String[]> params = map("email", "ana@mail.com");
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
