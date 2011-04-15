package org.prevayler.bubble.tests.fixturesnew;

import org.prevayler.bubble.tests.fixtures.SomeModule;
import org.prevayler.bubble.tests.fixtures.brick2.SomeModule2;
import org.prevayler.bubble.tests.fixtures.brick2.impl.SomeModule2Impl;
import org.prevayler.bubble.tests.fixtures.impl.SomeModuleImpl;

public class SomeApplicationImpl implements SomeApplication {

	private SomeModule _somePrevalentBrick = new SomeModuleImpl();
	private SomeModule2 _somePrevalentBrick2 = new SomeModule2Impl(_somePrevalentBrick);

	@Override
	public SomeModule module1() {
		return _somePrevalentBrick;
	}

	@Override
	public SomeModule2 module2() {
		return _somePrevalentBrick2;
	}

}
