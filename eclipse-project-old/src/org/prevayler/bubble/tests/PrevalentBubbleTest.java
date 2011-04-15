package org.prevayler.bubble.tests;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.prevayler.bubble.Bubble;
import org.prevayler.bubble.PrevalentContext;
import org.prevayler.bubble.tests.fixtures.Item;
import org.prevayler.bubble.tests.fixtures.SomeModule;
import org.prevayler.bubble.tests.fixturesnew.SomeApplication;

import sneer.foundation.lang.Closure;
import sneer.foundation.testsupport.CleanTestBase;

@Ignore
public class PrevalentBubbleTest extends CleanTestBase {
	
	private SomeApplication _subject = resetSubject();
	
	
	@After
	public void afterPrevalentBubbleTest() {
		PrevalentContext.closeSession();
	}
	
	
	@Test (timeout = 4000)
	public void stateIsPreserved() {
		_subject.module1().set("foo");
		assertEquals("foo", _subject.module1().get());

		_subject = resetSubject();
		assertEquals("foo", _subject.module1().get());
	}

	
	private SomeApplication resetSubject() {
		//PrevalentContext.startSession(new SomeApplicationImpl(), tmpFolder());
		SomeApplication prevalentSystem = (SomeApplication)PrevalentContext.prevalentSystem();
		return Bubble.wrapped(prevalentSystem, null);
	}


	@Test (timeout = 2000)
	public void nullParameter() {
		_subject.module1().set("foo");
		_subject.module1().set(null);

		assertNull(_subject.module1().get());
	}

	
	@Test //(timeout = 3000)
	public void multipleBricks() {
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
	
	
	@Test (timeout = 3000)
	public void objectsReturnedFromTransactionsAreAutomaticallyRegistered() {
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
	public void objectsReturnedByTransactionsAreRegistered() {
		Item item = _subject.module1().addItemAndReturnIt_AnnotatedAsTransaction("Foo");
		assertTrue("Item should be registered.", PrevalentContext.idMap().isRegistered(item));
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
	public void transactionMethodCallingTransactionMethod() {
		_subject.module2().addItemToSomeModule("foo");
		assertNotNull(_subject.module1().getItem("foo"));

		_subject = resetSubject();
		assertNotNull(_subject.module1().getItem("foo"));
	}

}
