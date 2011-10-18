package agitter.ui.view.session.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;

public class EventViewImpl extends CssLayout {

	static private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private long eventId;

	public EventViewImpl(EventData event) {
		addStyleName("a-event-view");
		addRemovalButton(event);
		CssLayout texts = new CssLayout();
		addComponent(texts); texts.addStyleName("a-event-texts");
			Label label = new Label(event.description);
			label.setSizeUndefined();
			texts.addComponent(label); label.addStyleName("a-event-description");
			label = new Label(dateFormat.format(new Date(event.datetime)));
			label.setSizeUndefined();
			texts.addComponent(label); label.addStyleName("a-event-date");
			label = new Label(event.owner);
			label.setSizeUndefined();
			texts.addComponent(label); label.addStyleName("a-event-owner");
		eventId = event.id;
	}

	public long getEventId() {
		return this.eventId;
	}

	private void addRemovalButton(EventData event) {
		String style = event.isDeletable
			? "a-event-delete-button"
			: "a-event-remove-button";
		addRemovalButton(event, style);
	}


	private void addRemovalButton(final EventData event, String style) {
		NativeButton button = new NativeButton();
		button.setSizeUndefined();
		addComponent(button); button.addStyleName(style);
		button.addStyleName("a-default-nativebutton");
		button.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			event.onRemoveAction.run();
		}});
	}

}
