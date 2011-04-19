package agitter.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class EventPanel extends CustomComponent {

	private static final long serialVersionUID = 1L;

	private DateFormat _dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private Panel mainPanel;

	public EventPanel(agitter.Event event) {
		mainPanel = new Panel();
		mainPanel.addComponent(new Label(event.description()));
		mainPanel.addComponent(new Label("everybody@aggiter.com"));
		mainPanel.addComponent(new Label(this._dateFormat.format(new Date(event.datetime()))));
		mainPanel.addComponent(removeButton());
		setCompositionRoot(mainPanel);
	}


	private Button removeButton() {
		Button result = new Button("X");
		result.addListener(new ClickListener() {  private static final long serialVersionUID = 1L;  @Override public void buttonClick(ClickEvent event) {
			System.out.println("Event to be removed or hid?");
		}});
		return result;
	}

}
