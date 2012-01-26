package agitter.main.spike;

import com.vaadin.Application;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Window;

public class MultiTabApplication extends Application {

	@Override
	public void init() {
		MultiTabWindow mainWindow = new MultiTabWindow();
		this.setMainWindow(mainWindow);
		System.out.println("Main window name:" + mainWindow.getName());
	}
	
	@Override
	public Window getWindow(String name) {
		System.out.println("App URL: " + getURL() + "  " + System.currentTimeMillis());
		System.out.println("Window name: " + name);
		
		// If the window is identified by name, we are good to go
		Window w = super.getWindow(name);

		// If not, we must create a new window for this new browser window/tab
		if (w == null) {
			w = new MultiTabWindow();

			// Use the random name given by the framework to identify this
			// window in future
			w.setName("foo" + name);
			addWindow(w);

			// Move to the url to remember the name in the future
			w.open(new ExternalResource(w.getURL()));
		}

		return w;
	}

	public static SystemMessages getSystemMessages() {
		CustomizedSystemMessages messages = new CustomizedSystemMessages();
		messages.setOutOfSyncNotificationEnabled(false);
		messages.setSessionExpiredNotificationEnabled(false);
		return messages;
	}
}
