package agitter;

import com.vaadin.Application;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class AgitterUI extends Application {
	
	private final ComponentContainer agitosList = new VerticalLayout();

	@Override
	public void init() {
		Window mainWindow = new Window("Agitter");

		mainWindow.addComponent(agitarPanel());
		mainWindow.addComponent(agitosList);
		
		setMainWindow(mainWindow);
	}

	
	private AgitarPanel agitarPanel() {
		return new AgitarPanel(new AgitoAdder() { @Override public void add(Agito agito) {
			agitosList.addComponent(new AgitoPanel(agito));
		}});
	}

	private static final long serialVersionUID = 1L;
}
