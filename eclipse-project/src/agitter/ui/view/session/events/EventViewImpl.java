package agitter.ui.view.session.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import sneer.foundation.lang.Consumer;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;

public class EventViewImpl extends CssLayout {

	static private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private final Consumer<Object> removedEventListener;

	public EventViewImpl(EventValues eventValues, Consumer<Object> removedEventListener) {
		this.removedEventListener = removedEventListener;
		setData(eventValues.eventObject);
		addStyleName("a-event-view");
		addRemovalButton(eventValues);
		CssLayout texts = new CssLayout();
		addComponent(texts); texts.addStyleName("a-event-texts");
			Label label = new Label(eventValues.description);
			label.setSizeUndefined();
			texts.addComponent(label); label.addStyleName("a-event-description");
			label = new Label(dateFormat.format(new Date(eventValues.datetime)));
			label.setSizeUndefined();
			texts.addComponent(label); label.addStyleName("a-event-date");
			label = new Label(eventValues.owner);
			label.setSizeUndefined();
			texts.addComponent(label); label.addStyleName("a-event-owner");
	}

	private void addRemovalButton(EventValues event) {
		String style = event.isDeletable
			? "a-event-delete-button"
			: "a-event-remove-button";
		addRemovalButton(event, style);
	}


	private void addRemovalButton(final EventValues eventValues, String style) {
		NativeButton button = new NativeButton();
		button.setSizeUndefined();
		addComponent(button); button.addStyleName(style);
		button.addStyleName("a-default-nativebutton");
		button.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			removedEventListener.consume(eventValues.eventObject);
		}});
	}

	
	Object getEventObject() {
		return getData();
	}

}
