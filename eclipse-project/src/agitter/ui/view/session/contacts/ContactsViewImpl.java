package agitter.ui.view.session.contacts;


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
