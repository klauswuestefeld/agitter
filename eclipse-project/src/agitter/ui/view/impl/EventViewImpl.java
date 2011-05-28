package agitter.ui.view.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import agitter.ui.view.EventData;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;

public class EventViewImpl extends CssLayout {

	static private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private NativeButton removeButton = new NativeButton();

	public EventViewImpl(EventData event) {
		addStyleName("a-event-view");
		removeButton.setSizeUndefined();
		addComponent(removeButton); removeButton.addStyleName("a-event-remove-button");
		removeButton.addStyleName("a-default-nativebutton");
		CssLayout texts = new CssLayout();
		addComponent(texts); texts.addStyleName("a-event-texts");
			Label label = new Label(event.description);
			texts.addComponent(label); label.addStyleName("a-event-description");
			label = new Label(dateFormat.format(new Date(event.datetime)));
			texts.addComponent(label); label.addStyleName("a-event-date");
			label = new Label(event.owner);
			texts.addComponent(label); label.addStyleName("a-event-owner");
		
		onRemovePressed(event.onRemoveAction);
	}


	private void onRemovePressed(final Runnable action) {
		removeButton.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			action.run();
		}});
	}

}
