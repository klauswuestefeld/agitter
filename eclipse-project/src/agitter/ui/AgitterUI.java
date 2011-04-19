package agitter.ui;

import agitter.Event;

import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AgitterUI extends Application {
	
	private final ComponentContainer eventList = new VerticalLayout();

	@Override
	public void init() {
		Window mainWindow = new Window("Agitter");

		mainWindow.addComponent(invitePanel());
		mainWindow.addComponent(eventList);
		
		setMainWindow(mainWindow);
	}

	
	private InvitePanel invitePanel() {
		return new InvitePanel(new EventAdder() { @Override public void add(Event event) {
			eventList.addComponent(new EventPanel(event));
		}});
	}

	private static final long serialVersionUID = 1L;
}
