package agitter.domain.users.tests;

import static agitter.domain.emails.EmailAddress.mail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.users.User;
import agitter.domain.users.Users;
import agitter.domain.users.Users.InvalidPassword;
import agitter.domain.users.Users.UserNotFound;
import agitter.domain.users.Users.UserNotActive;

public class UsersTest extends UsersTestBase {

	private final Users subject = agitter.users();

	@Before
	public void workaround() {
		Clock.setForCurrentThread(new GregorianCalendar(2011, Calendar.MAY, 28).getTimeInMillis());
	}

	@Test
	public void signup() throws Refusal {
		assertSignUp("ana@gmail.com", "ana123");
		assertSignUp("bruno@gmail.com", "brunox");
	}


	@Test(expected = InvalidPassword.class)
	public void loginWithInvalidPassword() throws Refusal {
		signUpAna();
		subject.loginWithEmail(mail("ana@gmail.com"), "ana000");
	}

	@Test(expected = UserNotActive.class)
	public void loginWithoutActivation() throws Refusal {
		subject.signup(mail("ana@gmail.com"), "ana123");
		subject.loginWithEmail(mail("ana@gmail.com"), "ana123");
	}


	@Test
	public void loginWithEmail() throws Refusal {
		signUpAna();
		User user = subject.loginWithEmail(mail("ana@gmail.com"), "ana123");
		assertAna(user);
	}

	@Test(expected = UserNotFound.class)
	public void loginWithUnknownEmail() throws Refusal {
		subject.loginWithEmail(mail("unknown_email@somewhere.com"), "irrelevant");
	}

	@Test
	public void findUserByEmail() throws Refusal {
		signUpAna();
		User user = subject.findByEmail(mail("ana@gmail.com"));
		assertEquals("ana@gmail.com", user.email().toString());
	}

	@Test(expected = UserNotFound.class)
	public void findUserByEmailWithNoResults() throws Refusal {
		subject.findByEmail(mail("unknown@somewhere.com"));
	}

	@Test(expected = Refusal.class)
	public void signupWithBlankPassword() throws Refusal {
		subject.signup(mail("myself@email.com"), "");
	}

	@Test
	public void signupTryingToCheat() throws Refusal {
		final String[][] cenarios = {
				{" ", "12345"},
				{"\t", "12345"},
				{"myself@email.com", null},
				{"myself@email.com", " "},
				{"myself@email.com", "\t"},
		};
		for(final String[] cenario : cenarios) {
			try {
				subject.signup(mail(cenario[0]), cenario[1]);
				Assert.fail("Expected Refusal trying to signup with: "+Arrays.toString(cenario));
			} catch(final Refusal refusal) {
				//expected
			}
		}
	}

	private void assertAna(User user) {
		assertEquals("ana@gmail.com", user.email().toString());
	}


	private void signUpAna() throws Refusal {
		User ana = signUp("ana@gmail.com", "ana123");
		ana.activate();
	}

	private void assertSignUp(String email, String password) throws Refusal {
		User user = signUp(email, password);
		assertEquals(email, user.email().toString());
	}

	private User signUp(String email, String password) throws Refusal {
		return subject.signup(mail(email), password);
	}

}
