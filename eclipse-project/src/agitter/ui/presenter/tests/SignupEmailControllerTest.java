package agitter.ui.presenter.tests;

import static agitter.domain.emails.EmailAddress.email;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import sneer.foundation.lang.exceptions.Refusal;
import sneer.foundation.testsupport.TestWithMocks;
import agitter.controller.mailing.SignupEmailController;
import agitter.controller.mailing.tests.EmailSenderMock;
import agitter.domain.users.Users;

public class SignupEmailControllerTest extends TestWithMocks {

	private final EmailSenderMock sender = new EmailSenderMock();
	private final Users users = mock(Users.class);
	
	private final SignupEmailController subject = new SignupEmailController(sender, users);
	

	@Test (timeout = 2000)
	public void signupViaEmail() throws Exception {
		checking(new Expectations(){{
			allowing(users).searchByEmail(email("ana@mail.com"));
			exactly(1).of(users).signup(email("ana@mail.com"), "password123");
		}});
		
		subject.initiateSignup(email("ana@mail.com"), "password123");
		assertTrue(sender.body().contains("email=ana@mail.com"));
		
		Map<String, String[]> params = map("email", "ana@mail.com", "code", "E752D24A27601AA686AFEDA0D3991CA35C99E062CFA929B030ED8F2E473010D0");  //Obtained by Regression
		subject.onRestInvocation(params);
	}


	@Test (timeout = 2000, expected = Refusal.class)
	public void invalidRequest() throws Exception {
		Map<String, String[]> params = map("email", "ana@mail.com", "code", "InvalidCode:P");
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
