package agitter.ui.view.impl;

import agitter.ui.view.ContactsView;

import com.vaadin.ui.ComponentContainer;

public class ContactsViewImpl implements ContactsView {

	private final ComponentContainer container;
	
	public ContactsViewImpl(ComponentContainer container) {
		this.container = container;
	}

	@Override
	public void show() {
		container.removeAllComponents();
	}

}
