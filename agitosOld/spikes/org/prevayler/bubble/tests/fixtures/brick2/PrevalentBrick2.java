package org.prevayler.bubble.tests.fixtures.brick2;

import sneer.bricks.hardware.io.prevalence.nature.Prevalent;
import sneer.foundation.brickness.Brick;

@Brick(Prevalent.class)
public interface PrevalentBrick2 {

	void rememberItemCount();

	int recallItemCount();
	
	void addItemToSomePrevalentBrick(String name);

}
