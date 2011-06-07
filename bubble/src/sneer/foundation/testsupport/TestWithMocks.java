package sneer.foundation.testsupport;

import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.api.Invocation;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.internal.ReturnDefaultValueAction;
import org.junit.After;

import sneer.foundation.util.concurrent.Latch;


public abstract class TestWithMocks extends CleanTestBase {

	public class Expectations extends org.jmock.Expectations {
		private final Sequence defaultSequence = _mockery.sequence("Default Sequence");
		
		/** Used to indicate methods have to be called in the correct sequence. */
		protected void inSequence() {
			inSequence(defaultSequence);
		}

		protected void open(final Latch latch) {
			will(new ReturnDefaultValueAction() { @Override public Object invoke(Invocation invocation) throws Throwable {
				latch.open();
				return super.invoke(invocation);
			}});
		}
	}

	protected TestWithMocks() {
		super();
	}
	
	private final Mockery _mockery = new JUnit4Mockery();

	
	@After
	public void afterTestWithMocks() {
		_mockery.assertIsSatisfied();
	}
	
	protected <T> T mock(Class<T> type) {
		return _mockery.mock(type);
	}

	protected <T> T mock(String name, Class<T> type) {
		return _mockery.mock(type, name);
	}

	protected void checking(Expectations behavior) {
		_mockery.checking(behavior);
	}

}