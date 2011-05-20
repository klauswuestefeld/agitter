package agitter.ui.view.impl;

import com.vaadin.ui.Component;

public class VaadinUtils {

	static public void focusOrder(Component.Focusable... components) {
		for(int i = 0; i < components.length; ++i)
			components[ i ].setTabIndex( i + 1 );
	}

}
