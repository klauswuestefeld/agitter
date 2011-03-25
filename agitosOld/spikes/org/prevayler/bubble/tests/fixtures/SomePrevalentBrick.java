package org.prevayler.bubble.tests.fixtures;

import sneer.bricks.hardware.io.prevalence.nature.Prevalent;
import sneer.bricks.hardware.io.prevalence.nature.Transaction;
import sneer.foundation.brickness.Brick;
import sneer.foundation.lang.Closure;
import sneer.foundation.lang.Consumer;

@Brick(Prevalent.class)
public interface SomePrevalentBrick {

	void set(String string);
	String get();
	
	void addItem(String name);
	void removeItem(Item item);
	int itemCount();
	Item getItem(String name);
	
	@Transaction
	Item addItemAndReturnIt_AnnotatedAsTransaction(String name);
	
	Consumer<String> itemAdder_Idempotent();
	
	Closure removerFor(Item item);
}
