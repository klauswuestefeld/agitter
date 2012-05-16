package org.prevayler.bubble.tests.fixtures.module1;

class BoxImpl implements Box {

	private final Item item;

	BoxImpl(Item item) {
		this.item = item;
	}

	@Override
	public Item item() {
		return item;
	}

}
