package org.prevayler.bubble.tests;

import static sneer.foundation.environments.Environments.my;

import org.junit.Ignore;
import org.junit.Test;
import org.prevayler.bubble.Bubble;
import org.prevayler.bubble.PrevalenceMap;
import org.prevayler.bubble.PrevaylerHolder;
import org.prevayler.bubble.tests.fixtures.Item;
import org.prevayler.bubble.tests.fixtures.SomePrevalentBrick;
import org.prevayler.bubble.tests.fixtures.brick2.PrevalentBrick2;
import org.prevayler.bubble.tests.fixturesnew.SomeApplication;
import org.prevayler.bubble.tests.fixturesnew.SomeApplicationImpl;

import sneer.bricks.software.folderconfig.FolderConfig;
import sneer.bricks.software.folderconfig.testsupport.BrickTestBase;
import sneer.foundation.environments.Environments;
import sneer.foundation.lang.Closure;

@Ignore
public class PrevalentNatureTest extends BrickTestBase {
	
	private final SomeApplication _subject = createSubject();
	
	@Test (timeout = 3000)
	public void stateIsPreserved() {
		_subject.somePrevalentBrick().set("foo");
		assertEquals("foo", _subject.somePrevalentBrick().get());

		runInNewTestEnvironment(new Closure() { @Override public void run() {
			assertEquals("foo", _subject.somePrevalentBrick().get());
		}});
	}

	
	private SomeApplication createSubject() {
		PrevaylerHolder.start(new SomeApplicationImpl(), tmpFolder());
		SomeApplication prevalentSystem = (SomeApplication)PrevaylerHolder.prevalentSystem();
		PrevalenceMap.register(prevalentSystem);
		return Bubble.wrapped(prevalentSystem, null);
	}


	@Test (timeout = 2000)
	public void nullParameter() {
		my(SomePrevalentBrick.class).set("foo");
		my(SomePrevalentBrick.class).set(null);

		assertNull(my(SomePrevalentBrick.class).get());
	}

	
	@Test (timeout = 3000)
	public void multipleBricks() {
		my(SomePrevalentBrick.class).addItem("Foo");
		my(PrevalentBrick2.class).rememberItemCount();
		
		runInNewTestEnvironment(new Closure() { @Override public void run() {
			assertEquals(1, my(PrevalentBrick2.class).recallItemCount());
		}});
	}
	
	
	@Test (timeout = 3000)
	public void brickCommandCausesAnotherBrickInstantiation() {
		my(PrevalentBrick2.class).rememberItemCount();
		
		runInNewTestEnvironment(new Closure() { @Override public void run() {
			assertEquals(0, my(PrevalentBrick2.class).recallItemCount());
		}});
	}
	
	
	@Test (timeout = 3000)
	public void baptismProblem() {
		SomePrevalentBrick brick = my(SomePrevalentBrick.class);
		brick.addItem("Foo");
		assertEquals(1, brick.itemCount());
		brick.addItem("Bar");
		assertEquals(2, brick.itemCount());
		
		Item item = brick.getItem("Foo");
		brick.removeItem(item);
		assertEquals(1, brick.itemCount());
		
		runInNewTestEnvironment(new Closure() { @Override public void run() {
			assertEquals(1, my(SomePrevalentBrick.class).itemCount());
		}});
	}
	
	@Test (timeout = 3000)
	public void objectsReturnedFromTransactionsAreAutomaticallyRegistered() {
		runInNewTestEnvironment(new Closure() { @Override public void run() {
			SomePrevalentBrick brick = my(SomePrevalentBrick.class);
			Item foo = brick.addItemAndReturnIt_AnnotatedAsTransaction("Foo");
			brick.addItem("Bar");
			brick.removeItem(foo);
			assertEquals(1, brick.itemCount());
		}});
		
		runInNewTestEnvironment(new Closure() { @Override public void run() {
			SomePrevalentBrick brick = my(SomePrevalentBrick.class);
			assertEquals(1, brick.itemCount());
			assertEquals("Bar", brick.getItem("Bar").name());
		}});
	}
	
	
	@Test (timeout = 3000)
	public void bubbleExpandsToQueriedValues() {
		SomePrevalentBrick brick = my(SomePrevalentBrick.class);
		brick.addItem("Foo");
		
		Item item = brick.getItem("Foo");
		item.name("Bar");
			
		assertNull(brick.getItem("Foo"));
		assertSame(item, brick.getItem("Bar"));
		
		runInNewTestEnvironment(new Closure() { @Override public void run() {
			assertNotNull(my(SomePrevalentBrick.class).getItem("Bar"));
		}});
	}

	
	@Test (timeout = 3000)
	public void queriesThatReturnUnregisteredObjectsAreAssumedToBeIdempotent() {
		SomePrevalentBrick brick = my(SomePrevalentBrick.class);
		brick.itemAdder_Idempotent().consume("Foo");
		
		runInNewTestEnvironment(new Closure() { @Override public void run() {
			assertNotNull(my(SomePrevalentBrick.class).getItem("Foo"));
		}});
	}
	
	@Test (timeout = 3000)
	public void idempotencyIsTransitive() {
		SomePrevalentBrick brick = my(SomePrevalentBrick.class);
		brick.itemAdder_Idempotent_Transitive().setter().consume("Foo");
		
		runInNewTestEnvironment(new Closure() { @Override public void run() {
			assertNotNull(my(SomePrevalentBrick.class).getItem("Foo"));
		}});
	}

	
	@Test (timeout = 3000)
	public void transactionAnnotation() {
		SomePrevalentBrick brick = my(SomePrevalentBrick.class);

		Item item = brick.addItemAndReturnIt_AnnotatedAsTransaction("Foo");
		item.name("Bar");
			
		assertNull(brick.getItem("Foo"));
		assertSame(item, brick.getItem("Bar"));
		
		runInNewTestEnvironment(new Closure() { @Override public void run() {
			assertNotNull(my(SomePrevalentBrick.class).getItem("Bar"));
		}});
	}

	
	@Test (timeout = 3000)
	public void objectsReturnedByTransactionsAreRegistered() {
		Item item = my(SomePrevalentBrick.class).addItemAndReturnIt_AnnotatedAsTransaction("Foo");
		assertTrue("Item should be registered.", PrevalenceMap.isRegistered(item));
	}

	
	@Test (timeout = 3000)
	public void invocationPathWithArgs() {
		my(SomePrevalentBrick.class).addItem("foo"); //Registers it.
		my(SomePrevalentBrick.class).addItem("bar");
		Item item = my(SomePrevalentBrick.class).getItem("foo"); 
		Closure remover = my(SomePrevalentBrick.class).removerFor(item);
		remover.run();

		runInNewTestEnvironment(new Closure() { @Override public void run() {
			assertEquals(1, my(SomePrevalentBrick.class).itemCount());
			assertNotNull(my(SomePrevalentBrick.class).getItem("bar"));
		}});
	}

	
	@Ignore
	@Test (timeout = 3000)
	public void transactionMethodCallingTransactionMethod() {
		my(PrevalentBrick2.class).addItemToSomePrevalentBrick("foo");
		assertNotNull(my(SomePrevalentBrick.class).getItem("foo"));

		runInNewTestEnvironment(new Closure() { @Override public void run() {
			System.out.println("test: " + my(SomePrevalentBrick.class));
			assertNotNull(my(SomePrevalentBrick.class).getItem("foo"));
		}});
	}

	
	private void runInNewTestEnvironment(Closure closure) {
		Environments.runWith(newTestEnvironment(my(FolderConfig.class)), closure);
	}

}
