package org.prevayler.bubble.tests.fixtures.brick2.impl;

import org.prevayler.bubble.tests.fixtures.SomePrevalentBrick;
import org.prevayler.bubble.tests.fixtures.brick2.PrevalentBrick2;

public class PrevalentBrick2Impl implements PrevalentBrick2 {

	private int _itemCount;
	private final SomePrevalentBrick _brick1;

	
	public PrevalentBrick2Impl(SomePrevalentBrick brick1) {
		_brick1 = brick1;
	}
	
	@Override
	public int recallItemCount() {
		return _itemCount;
	}

	
	@Override
	public void rememberItemCount() {
		_itemCount = _brick1.itemCount();
	}

	
	@Override
	public void addItemToSomePrevalentBrick(String name) {
		_brick1.addItem(name);
	}

}
