package agitter.domain.users.tests;

import static agitter.domain.emails.EmailAddress.email;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.prevayler.PrevaylerFactory;
import org.prevayler.bubble.PrevalentBubble;

import basis.lang.exceptions.Refusal;
import basis.testsupport.TestWithMocks;

import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;
import agitter.domain.users.User;
import agitter.domain.users.UserUtils;

public abstract class UsersTestBase extends TestWithMocks {

	{
		Logger.getLogger("").setLevel(Level.OFF);
	}

	public Agitter agitter = agitter();

	@After
	public void afterUserTestBase() {
		PrevalentBubble.pop();
	}

	
	protected User user(String email) throws Refusal {
		return UserUtils.produce(agitter.users(), email(email));
	}

	protected User user(String username, String email, String password) {
		try {
			return agitter.users().signup(email(email), password);
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
	}

	private Agitter agitter() {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configureTransientMode(true);
		factory.configureTransactionFiltering(false);
		factory.configurePrevalenceDirectory(tmpFolderName());
		try {
			return PrevalentBubble.wrap(new AgitterImpl(), factory);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}


}