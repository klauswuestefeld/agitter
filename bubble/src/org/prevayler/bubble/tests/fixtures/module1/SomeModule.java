package org.prevayler.bubble.tests.fixtures.module1;

import java.util.Collection;
import java.util.List;

import org.prevayler.bubble.Transaction;

import basis.lang.Closure;
import basis.lang.Consumer;


public interface SomeModule {

	void set(String string);
	String get();
	
	void addItem(String name);
	void removeItem(Item item);
	int itemCount();
	Item getItem(String name);
	List<Box> itemsInBoxes();
	
	@Transaction
	Item addItemAndReturnIt_AnnotatedAsTransaction(String name);
	
	Consumer<String> itemAdder_Idempotent();
	
	Closure removerFor(Item item);
	Closure removerFor(Collection<Item> collectionWithSingleItem);
	Closure removerFor(Item[] arrayWithSingleItem);
}
