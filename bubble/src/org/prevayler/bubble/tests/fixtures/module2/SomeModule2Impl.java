package org.prevayler.bubble.tests.fixtures.module2;

import org.prevayler.bubble.tests.fixtures.module1.SomeModule;

public class SomeModule2Impl implements SomeModule2 {

	private int _itemCount;
	private final SomeModule _module1;

	
	public SomeModule2Impl(SomeModule module1) {
		_module1 = module1;
	}
	
	@Override
	public int recallItemCount() {
		return _itemCount;
	}

	
	@Override
	public void rememberItemCount() {
		_itemCount = _module1.itemCount();
	}

	
	@Override
	public void addItemToSomeModule(String name) {
		_module1.addItem(name);
	}

}
