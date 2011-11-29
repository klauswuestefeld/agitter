package agitter.ui.view.session.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import agitter.ui.helper.HTMLFormatter;
import agitter.ui.view.session.events.EventListView.Boss;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;

public class EventListElement extends CssLayout {

	static private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private final Boss boss;

	public EventListElement(EventVO eventValues, Boss boss) {
		this.boss = boss;
		setData(eventValues.eventObject);
		addStyleName("a-event-view");
		addRemovalButton(eventValues);
		CssLayout texts = new CssLayout();
		addComponent(texts); texts.addStyleName("a-event-texts");
			Label label;
			label = new Label(dateFormat.format(new Date(eventValues.datetime)));
			label.setSizeUndefined();
			texts.addComponent(label); label.addStyleName("a-event-date");
			
			label = new Label(new HTMLFormatter().makeClickable(eventValues.description), Label.CONTENT_XHTML);
			label.setSizeUndefined();
			texts.addComponent(label); label.addStyleName("a-event-description");

			label = new Label(eventValues.owner);
			label.setSizeUndefined();
			texts.addComponent(label); label.addStyleName("a-event-owner");
	}

	private void addRemovalButton(EventVO event) {
		String style = event.isDeletable
			? "a-event-delete-button"
			: "a-event-remove-button";
		Button button = addRemovalButton(event, style);
		button.setDescription(
			event.isDeletable
			? "Excluir este Agito"
			: "Fica pra próxima"
		);
	}


	private Button addRemovalButton(final EventVO eventValues, String style) {
		NativeButton button = new NativeButton();
		button.setSizeUndefined();
		addComponent(button); button.addStyleName(style);
		button.addStyleName("a-default-nativebutton");
		button.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			System.out.println("Botao de remocao clicado.");
			boss.onEventRemoved(eventValues.eventObject);
		}});
		return button;
	}

	
	Object getEventObject() {
		return getData();
	}

}
