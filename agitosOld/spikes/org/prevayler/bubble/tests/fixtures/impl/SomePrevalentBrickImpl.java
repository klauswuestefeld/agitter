package org.prevayler.bubble.tests.fixtures.impl;

import static sneer.foundation.environments.Environments.my;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.prevayler.bubble.tests.fixtures.Item;
import org.prevayler.bubble.tests.fixtures.SomePrevalentBrick;

import sneer.bricks.hardware.cpu.lang.contracts.WeakContract;
import sneer.bricks.hardware.io.prevalence.map.PrevalenceMap;
import sneer.bricks.pulp.reactive.Register;
import sneer.bricks.pulp.reactive.Signals;
import sneer.foundation.lang.Closure;
import sneer.foundation.lang.Consumer;

public class SomePrevalentBrickImpl implements SomePrevalentBrick {

	private static final String INITIAL_VALUE = "INITIAL_VALUE";


	static final class ItemImpl implements Item {
		private String _name;

		public ItemImpl(String name) {
			_name = name;
		}

		@Override
		public String name() {
			return _name;
		}

		@Override
		public void name(String value) {
			_name = value;
		}
	}

	
	private String _string;
	private List<Item> _items = new ArrayList<Item>();
	private Set<WeakContract> _refToAvoidGC = new HashSet<WeakContract>();

	
	@Override
	public String get() {
		return _string;
	}

	
	@Override
	public void set(String string) {
		_string = string;
	}

	
	@Override
	public void addItem(String name) {
		ItemImpl item = addItemWithoutRegistering(name);
		my(PrevalenceMap.class).register(item);
	}


	private ItemImpl addItemWithoutRegistering(String name) {
		ItemImpl result = new ItemImpl(name);
		_items.add(result);
		return result;
	}
	
	@Override
	public Item addItemAndReturnIt_AnnotatedAsTransaction(String name) {
		return addItemWithoutRegistering(name);
	}

	
	@Override
	public int itemCount() {
		return _items.size();
	}

	
	@Override
	public void removeItem(Item item) {
		_items.remove(item);
	}

	
	@Override
	public Item getItem(String name) {
		for (Item item : _items)
			if (item.name().equals(name)) return item;
		return null;
	}

	
	@Override
	public Consumer<String> itemAdder_Idempotent() {
		return new Consumer<String>() { @Override public void consume(String name) {
			if (INITIAL_VALUE.equals(name))
				return;
			addItem(name);
		}};
	}


	@Override
	public Register<String> itemAdder_Idempotent_Transitive() {
		final Register<String> register = my(Signals.class).newRegister(INITIAL_VALUE);
		_refToAvoidGC.add(register.output().addReceiver(itemAdder_Idempotent()));
		return register;
	}


	@Override
	public Closure removerFor(final Item item) {
		return new Closure() { @Override public void run() {
			if (!_items.contains(item))
				throw new IllegalStateException();
			removeItem(item);
		}};
	}
	
}