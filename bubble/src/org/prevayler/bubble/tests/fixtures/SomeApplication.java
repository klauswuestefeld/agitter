package org.prevayler.bubble.tests.fixtures;

import org.prevayler.bubble.tests.fixtures.module1.SomeModule;
import org.prevayler.bubble.tests.fixtures.module2.SomeModule2;

public interface SomeApplication {

	SomeModule module1();

	SomeModule2 module2();

}
