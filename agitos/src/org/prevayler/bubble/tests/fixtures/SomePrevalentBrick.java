package org.prevayler.bubble.tests.fixtures;

import org.prevayler.bubble.Transaction;

import sneer.foundation.lang.Closure;
import sneer.foundation.lang.Consumer;

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
