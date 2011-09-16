package agitter.ui.presenter.tests;

import static agitter.domain.emails.EmailAddress.email;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import sneer.foundation.testsupport.TestWithMocks;
import agitter.controller.mailing.SignupEmailController;
import agitter.controller.mailing.tests.EmailSenderMock;
import agitter.domain.users.Users;

public class SignupEmailControllerTest extends TestWithMocks {

	private final EmailSenderMock sender = new EmailSenderMock();
	private final Users users = mock(Users.class);
	
	private final SignupEmailController subject = new SignupEmailController(sender, users);
	
	@Ignore
	@Test (timeout = 2000)
	public void signupViaEmail() throws Exception {
		checking(new Expectations(){{
			exactly(1).of(users).searchByEmail(email("ana@mail.com"));
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
