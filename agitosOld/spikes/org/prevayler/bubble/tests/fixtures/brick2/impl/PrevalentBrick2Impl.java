package org.prevayler.bubble.tests.fixtures.brick2.impl;

import static sneer.foundation.environments.Environments.my;

import org.prevayler.bubble.tests.fixtures.SomePrevalentBrick;
import org.prevayler.bubble.tests.fixtures.brick2.PrevalentBrick2;

class PrevalentBrick2Impl implements PrevalentBrick2 {

	private int _itemCount;

	
	@Override
	public int recallItemCount() {
		return _itemCount;
	}

	
	@Override
	public void rememberItemCount() {
		_itemCount = my(SomePrevalentBrick.class).itemCount();
	}

	
	@Override
	public void addItemToSomePrevalentBrick(String name) {
		System.out.println("impl:" + my(SomePrevalentBrick.class));
		my(SomePrevalentBrick.class).addItem(name);
	}

}
