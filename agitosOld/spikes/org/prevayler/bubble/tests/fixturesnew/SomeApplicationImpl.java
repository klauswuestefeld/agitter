package org.prevayler.bubble.tests.fixturesnew;

import org.prevayler.bubble.tests.fixtures.SomePrevalentBrick;
import org.prevayler.bubble.tests.fixtures.brick2.PrevalentBrick2;
import org.prevayler.bubble.tests.fixtures.brick2.impl.PrevalentBrick2Impl;
import org.prevayler.bubble.tests.fixtures.impl.SomePrevalentBrickImpl;

public class SomeApplicationImpl implements SomeApplication {

	private SomePrevalentBrick _somePrevalentBrick = new SomePrevalentBrickImpl();
	private PrevalentBrick2 _somePrevalentBrick2 = new PrevalentBrick2Impl(_somePrevalentBrick);

	@Override
	public SomePrevalentBrick somePrevalentBrick() {
		return _somePrevalentBrick;
	}

	@Override
	public PrevalentBrick2 somePrevalentBrick2() {
		return _somePrevalentBrick2;
	}

}
