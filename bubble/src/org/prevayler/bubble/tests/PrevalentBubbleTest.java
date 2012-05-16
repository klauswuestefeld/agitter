package org.prevayler.bubble.tests;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.prevayler.PrevaylerFactory;
import org.prevayler.bubble.PrevalentBubble;
import org.prevayler.bubble.tests.fixtures.SomeApplication;
import org.prevayler.bubble.tests.fixtures.SomeApplicationImpl;
import org.prevayler.bubble.tests.fixtures.module1.Box;
import org.prevayler.bubble.tests.fixtures.module1.Item;
import org.prevayler.bubble.tests.fixtures.module1.SomeModule;
import org.prevayler.foundation.serialization.XStreamSerializer;

import sneer.foundation.lang.Closure;
import sneer.foundation.testsupport.CleanTestBase;

public class PrevalentBubbleTest extends CleanTestBase {
	
	private SomeApplication _subject;
	
	
	private SomeApplication resetSubject() {
		if (_subject != null) PrevalentBubble.pop();
		
		try {
			return PrevalentBubble.wrap(new SomeApplicationImpl(), factory());
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}


	private PrevaylerFactory factory() {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalenceDirectory(tmpFolder().getAbsolutePath());
		factory.configureJournalSerializer(new XStreamSerializer());
		factory.configureTransactionFiltering(false);
		return factory;
	}


	@Before
	public void beforePrevalentBubbleTest() {
		_subject = resetSubject();
	}

	
	@After
	public void afterPrevalentBubbleTest() {
		PrevalentBubble.pop(); //Bursts the bubble, closing Prevayler.
	}
	
	
	@Test (timeout = 4000)
	public void stateIsPreserved() {
		_subject.module1().set("foo");
		assertEquals("foo", _subject.module1().get());

		_subject = resetSubject();
		assertEquals("foo", _subject.module1().get());
	}

	
	@Test (timeout = 2000)
	public void nullParameter() {
		_subject.module1().set("foo");
		_subject.module1().set(null);

		assertNull(_subject.module1().get());
	}

	
	@Test (timeout = 3000)
	public void multipleMethods() {
		_subject.module1().addItem("Foo");
		_subject.module2().rememberItemCount();
		
		_subject = resetSubject();
		assertEquals(1, _subject.module2().recallItemCount());
	}
	
	
	@Test (timeout = 3000)
	public void brickCommandCausesAnotherBrickInstantiation() {
		_subject.module2().rememberItemCount();
		
		_subject = resetSubject();
		assertEquals(0, _subject.module2().recallItemCount());
	}
	
	
	@Test (timeout = 3000)
	public void baptismProblem() {
		SomeModule brick = _subject.module1();
		brick.addItem("Foo");
		assertEquals(1, brick.itemCount());
		brick.addItem("Bar");
		assertEquals(2, brick.itemCount());
		
		Item item = brick.getItem("Foo");
		brick.removeItem(item);
		assertEquals(1, brick.itemCount());
		
		_subject = resetSubject();
		assertEquals(1, _subject.module1().itemCount());
	}
	
	
	@Test
	public void snapshot() throws IOException {
		SomeModule brick = _subject.module1();
		brick.addItem("Foo");
		PrevalentBubble.takeSnapshot();
		brick.addItem("Bar");
		brick.removeItem(brick.getItem("Bar"));
		
		_subject = resetSubject();
		assertEquals(1, _subject.module1().itemCount());
	}
	
	
	@Test (timeout = 3000)
	public void objectsReturnedFromTransactionsAreRegistered() {
		SomeModule brick = _subject.module1();
		Item foo = brick.addItemAndReturnIt_AnnotatedAsTransaction("Foo");
		brick.addItem("Bar");
		brick.removeItem(foo);
		assertEquals(1, brick.itemCount());
		
		_subject = resetSubject();
		brick = _subject.module1();
		assertEquals(1, brick.itemCount());
		assertEquals("Bar", brick.getItem("Bar").name());
	}
	
	
	@Test (timeout = 3000)
	public void mutableTransientObjects_LikeProprietaryCollections_CanBeReturnedIfNotUsedInTransactionPath() {
		SomeModule module = _subject.module1();
		module.addItem("item1");
		Item item = module.getItem("item1");
		Box box = module.itemsInBoxes().get(0);
		assertSame(item, box.item());
	}
	
	
	@Test (timeout = 3000)
	public void bubbleExpandsToQueriedValues() {
		SomeModule brick = _subject.module1();
		brick.addItem("Foo");
		
		Item item = brick.getItem("Foo");
		item.name("Bar");
			
		assertNull(brick.getItem("Foo"));
		assertSame(item, brick.getItem("Bar"));
		
		_subject = resetSubject();
		assertNotNull(_subject.module1().getItem("Bar"));
	}

	
	@Test (timeout = 3000)
	public void queriesThatReturnUnregisteredObjectsAreAssumedToBeIdempotent() {
		SomeModule brick = _subject.module1();
		brick.itemAdder_Idempotent().consume("Foo");
		
		_subject = resetSubject();
		assertNotNull(_subject.module1().getItem("Foo"));
	}

	
	@Test (timeout = 3000)
	public void transactionAnnotation() {
		SomeModule brick = _subject.module1();

		Item item = brick.addItemAndReturnIt_AnnotatedAsTransaction("Foo");
		item.name("Bar");
			
		assertNull(brick.getItem("Foo"));
		assertSame(item, brick.getItem("Bar"));
		
		_subject = resetSubject();
		assertNotNull(_subject.module1().getItem("Bar"));
	}

	
	@Test (timeout = 3000)
	public void invocationPathWithArgs() {
		_subject.module1().addItem("foo"); //Registers it.
		_subject.module1().addItem("bar");
		Item item = _subject.module1().getItem("foo"); 
		Closure remover = _subject.module1().removerFor(item);
		remover.run();

		_subject = resetSubject();
		assertEquals(1, _subject.module1().itemCount());
		assertNotNull(_subject.module1().getItem("bar"));
	}

	
	@Test (timeout = 3000)
	public void invocationPathWithArgsAsCollection() {
		_subject.module1().addItem("foo"); //Registers it.
		_subject.module1().addItem("bar");
		Item item = _subject.module1().getItem("foo");
		Closure remover = _subject.module1().removerFor(Arrays.asList(item));
		remover.run();

		_subject = resetSubject();
		assertEquals(1, _subject.module1().itemCount());
		assertNotNull(_subject.module1().getItem("bar"));
	}

	
	@Test //(timeout = 3000)
	public void invocationPathWithArgsAsArray() {
		_subject.module1().addItem("foo"); //Registers it.
		_subject.module1().addItem("bar");
		Item item = _subject.module1().getItem("foo"); 
		Closure remover = _subject.module1().removerFor(new Item[]{item});
		remover.run();

		_subject = resetSubject();
		assertEquals(1, _subject.module1().itemCount());
		assertNotNull(_subject.module1().getItem("bar"));
	}

	
	@Test (timeout = 3000)
	public void transactionMethodCallingTransactionMethod() {
		_subject.module2().addItemToSomeModule("foo");
		assertNotNull(_subject.module1().getItem("foo"));

		_subject = resetSubject();
		assertNotNull(_subject.module1().getItem("foo"));
	}

	@Test (timeout = 2000)
	public void readOnlyMode() {
		_subject.module1().set("foo");
		
		PrevalentBubble.enterReadOnlyMode( "System being updated..." );
		try {
			_subject.module1().set(null);
			fail();
		} catch (RuntimeException re) {
			assertEquals( "System being updated...", re.getMessage() );
		}
		assertEquals( "foo", _subject.module1().get() );

		PrevalentBubble.leaveReadOnlyMode();
		_subject.module1().set(null);
		assertEquals( null, _subject.module1().get() );
		
	}

}
