package org.prevayler.bubble.tests.fixtures;

import java.io.Serializable;

import org.prevayler.bubble.tests.fixtures.module1.SomeModule;
import org.prevayler.bubble.tests.fixtures.module1.SomeModuleImpl;
import org.prevayler.bubble.tests.fixtures.module2.SomeModule2;
import org.prevayler.bubble.tests.fixtures.module2.SomeModule2Impl;

public class SomeApplicationImpl implements SomeApplication, Serializable {

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
