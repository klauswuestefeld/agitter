package agitter.ui.view.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import agitter.ui.view.EventData;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class EventViewImpl extends CustomComponent {

	static private DateFormat _dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private Button _removeButton = new Button("X");

	
	public EventViewImpl(EventData event) {
		Panel mainPanel = new Panel();
		mainPanel.addComponent(new Label(event._description));
		mainPanel.addComponent(new Label(event._ownerEmail));
		mainPanel.addComponent(new Label(_dateFormat.format(new Date(event._datetime))));
		mainPanel.addComponent(_removeButton);
		onRemovePressed(event._onRemoveAction);
		
		setCompositionRoot(mainPanel);
	}


	private void onRemovePressed(final Runnable action) {
		_removeButton.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			action.run();
		}});
	}

}
