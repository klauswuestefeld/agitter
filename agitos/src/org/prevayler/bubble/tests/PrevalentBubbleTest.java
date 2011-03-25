package org.prevayler.bubble.tests;

import org.junit.After;
import org.junit.Test;
import org.prevayler.bubble.Bubble;
import org.prevayler.bubble.PrevalentContext;
import org.prevayler.bubble.tests.fixtures.Item;
import org.prevayler.bubble.tests.fixtures.SomePrevalentBrick;
import org.prevayler.bubble.tests.fixturesnew.SomeApplication;
import org.prevayler.bubble.tests.fixturesnew.SomeApplicationImpl;

import sneer.foundation.lang.Closure;
import sneer.foundation.testsupport.CleanTestBase;

public class PrevalentBubbleTest extends CleanTestBase {
	
	private SomeApplication _subject = resetSubject();
	
	
	@After
	public void afterPrevalentBubbleTest() {
		PrevalentContext.closeSession();
	}
	
	
	@Test (timeout = 4000)
	public void stateIsPreserved() {
		_subject.somePrevalentBrick().set("foo");
		assertEquals("foo", _subject.somePrevalentBrick().get());

		_subject = resetSubject();
		assertEquals("foo", _subject.somePrevalentBrick().get());
	}

	
	private SomeApplication resetSubject() {
		PrevalentContext.startSession(new SomeApplicationImpl(), tmpFolder());
		SomeApplication prevalentSystem = (SomeApplication)PrevalentContext.prevalentSystem();
		return Bubble.wrapped(prevalentSystem, null);
	}


	@Test (timeout = 2000)
	public void nullParameter() {
		_subject.somePrevalentBrick().set("foo");
		_subject.somePrevalentBrick().set(null);

		assertNull(_subject.somePrevalentBrick().get());
	}

	
	@Test //(timeout = 3000)
	public void multipleBricks() {
		_subject.somePrevalentBrick().addItem("Foo");
		_subject.somePrevalentBrick2().rememberItemCount();
		
		_subject = resetSubject();
		assertEquals(1, _subject.somePrevalentBrick2().recallItemCount());
	}
	
	
	@Test (timeout = 3000)
	public void brickCommandCausesAnotherBrickInstantiation() {
		_subject.somePrevalentBrick2().rememberItemCount();
		
		_subject = resetSubject();
		assertEquals(0, _subject.somePrevalentBrick2().recallItemCount());
	}
	
	
	@Test (timeout = 3000)
	public void baptismProblem() {
		SomePrevalentBrick brick = _subject.somePrevalentBrick();
		brick.addItem("Foo");
		assertEquals(1, brick.itemCount());
		brick.addItem("Bar");
		assertEquals(2, brick.itemCount());
		
		Item item = brick.getItem("Foo");
		brick.removeItem(item);
		assertEquals(1, brick.itemCount());
		
		_subject = resetSubject();
		assertEquals(1, _subject.somePrevalentBrick().itemCount());
	}
	
	
	@Test (timeout = 3000)
	public void objectsReturnedFromTransactionsAreAutomaticallyRegistered() {
		SomePrevalentBrick brick = _subject.somePrevalentBrick();
		Item foo = brick.addItemAndReturnIt_AnnotatedAsTransaction("Foo");
		brick.addItem("Bar");
		brick.removeItem(foo);
		assertEquals(1, brick.itemCount());
		
		_subject = resetSubject();
		brick = _subject.somePrevalentBrick();
		assertEquals(1, brick.itemCount());
		assertEquals("Bar", brick.getItem("Bar").name());
	}
	
	
	@Test (timeout = 3000)
	public void bubbleExpandsToQueriedValues() {
		SomePrevalentBrick brick = _subject.somePrevalentBrick();
		brick.addItem("Foo");
		
		Item item = brick.getItem("Foo");
		item.name("Bar");
			
		assertNull(brick.getItem("Foo"));
		assertSame(item, brick.getItem("Bar"));
		
		_subject = resetSubject();
		assertNotNull(_subject.somePrevalentBrick().getItem("Bar"));
	}

	
	@Test (timeout = 3000)
	public void queriesThatReturnUnregisteredObjectsAreAssumedToBeIdempotent() {
		SomePrevalentBrick brick = _subject.somePrevalentBrick();
		brick.itemAdder_Idempotent().consume("Foo");
		
		_subject = resetSubject();
		assertNotNull(_subject.somePrevalentBrick().getItem("Foo"));
	}

	
	@Test (timeout = 3000)
	public void transactionAnnotation() {
		SomePrevalentBrick brick = _subject.somePrevalentBrick();

		Item item = brick.addItemAndReturnIt_AnnotatedAsTransaction("Foo");
		item.name("Bar");
			
		assertNull(brick.getItem("Foo"));
		assertSame(item, brick.getItem("Bar"));
		
		_subject = resetSubject();
		assertNotNull(_subject.somePrevalentBrick().getItem("Bar"));
	}

	
	@Test (timeout = 3000)
	public void objectsReturnedByTransactionsAreRegistered() {
		Item item = _subject.somePrevalentBrick().addItemAndReturnIt_AnnotatedAsTransaction("Foo");
		assertTrue("Item should be registered.", PrevalentContext.idMap().isRegistered(item));
	}

	
	@Test (timeout = 3000)
	public void invocationPathWithArgs() {
		_subject.somePrevalentBrick().addItem("foo"); //Registers it.
		_subject.somePrevalentBrick().addItem("bar");
		Item item = _subject.somePrevalentBrick().getItem("foo"); 
		Closure remover = _subject.somePrevalentBrick().removerFor(item);
		remover.run();

		_subject = resetSubject();
		assertEquals(1, _subject.somePrevalentBrick().itemCount());
		assertNotNull(_subject.somePrevalentBrick().getItem("bar"));
	}

	
	@Test (timeout = 3000)
	public void transactionMethodCallingTransactionMethod() {
		_subject.somePrevalentBrick2().addItemToSomePrevalentBrick("foo");
		assertNotNull(_subject.somePrevalentBrick().getItem("foo"));

		_subject = resetSubject();
		assertNotNull(_subject.somePrevalentBrick().getItem("foo"));
	}

}
