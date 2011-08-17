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

public class UsersTest extends UsersTestBase {

	private final Users subject = agitter.users();

	@Before
	public void workaround() {
		Clock.setForCurrentThread(new GregorianCalendar(2011, Calendar.MAY, 28).getTimeInMillis());
	}

	@Test
	public void signup() throws Refusal {
		assertSignUp("ana", "ana@gmail.com", "ana123");
		assertSignUp("bruninho", "bruno@gmail.com", "brunox");
	}

	@Test
	public void loginWithUsername() throws Refusal {
		signUpAna();
		User user = subject.loginWithUsername("ana", "ana123");
		assertAna(user);
	}

	@Test(expected = UserNotFound.class)
	public void loginWithUnknownUserName() throws Refusal {
		subject.loginWithUsername("unknown_username", "irrelevant");
	}

	@Test(expected = InvalidPassword.class)
	public void loginWithInvalidPassword() throws Refusal {
		signUpAna();
		subject.loginWithUsername("ana", "ana000");
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
	public void findUserByUsername() throws Refusal {
		signUpAna();
		User user = subject.findByUsername("ana");
		assertEquals("ana@gmail.com", user.email().toString());
	}

	@Test(expected = UserNotFound.class)
	public void findUserByUsernameWithNoResults() throws Refusal {
		subject.findByUsername("unknown");
	}

	@Test
	public void findUserByEmail() throws Refusal {
		signUpAna();
		User user = subject.findByEmail(mail("ana@gmail.com"));
		assertEquals("ana", user.username());
	}

	@Test(expected = UserNotFound.class)
	public void findUserByEmailWithNoResults() throws Refusal {
		subject.findByEmail(mail("unknown@somewhere.com"));
	}

	@Test(expected = Refusal.class)
	public void signupWithBlankUsername() throws Refusal {
		subject.signup("", mail("myself@email.com"), "$ecret");
	}

	@Test(expected = Refusal.class)
	public void signupWithBlankPassword() throws Refusal {
		subject.signup("myself", mail("myself@email.com"), "");
	}

	@Test
	public void signupTryingToCheat() throws Refusal {
		final String[][] cenarios = {
				{null, "myself@email.com", "12345"},
				{" ", "myself@email.com", "12345"},
				{"		", "myself@email.com", "12345"},
				{"user", " ", "12345"},
				{"user", "		", "12345"},
				{"user", "myself@email.com", null},
				{"user", "myself@email.com", " "},
				{"user", "myself@email.com", "		"},
		};
		for(final String[] cenario : cenarios) {
			try {
				subject.signup(cenario[0], mail(cenario[1]), cenario[2]);
				Assert.fail("Expected Refusal trying to signup with: "+Arrays.toString(cenario));
			} catch(final Refusal refusal) {
				//expected
			}
		}
	}

	private void assertAna(User user) {
		assertEquals("ana", user.username());
		assertEquals("ana@gmail.com", user.email().toString());
	}


	private void signUpAna() throws Refusal {
		signUp("ana", "ana@gmail.com", "ana123");
	}

	private void assertSignUp(String username, String email, String password) throws Refusal {
		User user = signUp(username, email, password);
		assertEquals(username, user.username());
	}

	private User signUp(String username, String email, String password) throws Refusal {
		return subject.signup(username, mail(email), password);
	}

}
