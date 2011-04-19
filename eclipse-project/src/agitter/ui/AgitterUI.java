package agitter.ui;

import java.io.File;

import agitter.AgitterSystem;
import agitter.Event;
import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AgitterUI extends Application {

	private static final long serialVersionUID = 1L;

	private final ComponentContainer eventList = new VerticalLayout();

	@Override
	public void init() {
		initSystem();
		Window mainWindow = new Window("AgitterSystem");
		mainWindow.addComponent(invitePanel());
		mainWindow.addComponent(eventList);
		setMainWindow(mainWindow);



	}
	private void initSystem() {
		try {
			final File tmpFolder = new File("/agitterrepo"); //TODO
			if(!tmpFolder.exists()) { tmpFolder.mkdir(); }
			AgitterSystem.open(tmpFolder);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}


	private InvitePanel invitePanel() {
		return new InvitePanel(new EventAdder() {
			@Override
			public void add(String description, long datetime) {
				Event event = AgitterSystem.execution().events().create(description, datetime);
				eventList.addComponent(new EventPanel(event));
			}
		});
	}

}
