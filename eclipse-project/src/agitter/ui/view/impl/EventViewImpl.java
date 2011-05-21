package agitter.ui.view.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import agitter.ui.view.EventData;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class EventViewImpl extends Panel {

	static private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private NativeButton removeButton = new NativeButton("X");

	public EventViewImpl(EventData event) {
		setScrollable(false);
		addStyleName(Reindeer.PANEL_LIGHT);
		addStyleName("a-event-view");
		HorizontalLayout content = new HorizontalLayout();
		setContent(content);
		content.setMargin(true);
		content.setSizeFull();
		VerticalLayout texts = new VerticalLayout();
		content.addComponent(texts); 
		content.setExpandRatio(texts, 1);
			Label label = new Label(event.description);
			texts.addComponent(label); label.addStyleName("a-event-description");
			label = new Label(dateFormat.format(new Date(event.datetime)));
			texts.addComponent(label); label.addStyleName("a-event-date");
			label = new Label(event.owner);
			texts.addComponent(label); label.addStyleName("a-event-owner");
		content.addComponent(removeButton); removeButton.addStyleName("a-event-remove-button");
		content.setComponentAlignment(removeButton,	Alignment.TOP_RIGHT);
		content.setExpandRatio(removeButton, 0);
		
		onRemovePressed(event.onRemoveAction);
	}


	private void onRemovePressed(final Runnable action) {
		removeButton.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			action.run();
		}});
	}

}
