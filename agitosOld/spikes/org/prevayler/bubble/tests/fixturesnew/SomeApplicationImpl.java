package org.prevayler.bubble.tests.fixturesnew;

import org.prevayler.bubble.tests.fixtures.SomePrevalentBrick;
import org.prevayler.bubble.tests.fixtures.impl.SomePrevalentBrickImpl;

public class SomeApplicationImpl implements SomeApplication {

	private SomePrevalentBrick _somePrevalentBrick = new SomePrevalentBrickImpl();

	@Override
	public SomePrevalentBrick somePrevalentBrick() {
		return _somePrevalentBrick;
	}

}
