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

	static private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public EventViewImpl(EventData event) {
		addStyleName("a-event-view");
		addNotInterestedButtonIfNecessary(event);
		CssLayout texts = new CssLayout();
		addComponent(texts); texts.addStyleName("a-event-texts");
			Label label = new Label(event.description);
			texts.addComponent(label); label.addStyleName("a-event-description");
			label = new Label(dateFormat.format(new Date(event.datetime)));
			texts.addComponent(label); label.addStyleName("a-event-date");
			label = new Label(event.owner);
			texts.addComponent(label); label.addStyleName("a-event-owner");
	}


	private void addNotInterestedButtonIfNecessary(final EventData event) {
		if (event.onRemoveAction == null) return;

		NativeButton button = new NativeButton();
		button.setSizeUndefined();
		addComponent(button); button.addStyleName("a-event-remove-button");
		button.addStyleName("a-default-nativebutton");
		button.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			event.onRemoveAction.run();
		}});
	}

}
