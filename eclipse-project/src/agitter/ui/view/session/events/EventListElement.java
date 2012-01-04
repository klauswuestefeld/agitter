package agitter.ui.view.session.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import vaadinutils.WidgetUtils;
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

			CssLayout participants = new CssLayout();
			texts.addComponent(participants); participants.addStyleName("a-event-participants");
			
				label = WidgetUtils.createLabel(ownerText(eventValues));
				participants.addComponent(label); label.addStyleName("a-event-owner");
			
				label = WidgetUtils.createLabel(inviteesText(eventValues));
				participants.addComponent(label); label.addStyleName("a-event-invitees");
	}

	private String ownerText(EventVO eventValues) {
		return eventValues.isEditable ? "você" : eventValues.owner;
	}

	private String inviteesText(EventVO eventValues) {
		
		if (eventValues.totalInviteesCount == 0)
			return "(nenhum convidado)";
		
		StringBuffer result = new StringBuffer("+ ");
		String convidadosText = eventValues.totalInviteesCount > 1 ? " convidados" : " convidado";
				
		if (eventValues.isEditable && eventValues.uniqueGroupOrUserInvited != null) {
			result.append(eventValues.uniqueGroupOrUserInvited);
			if (!eventValues.isUniqueUserInvited)
				result.append(" (").append(eventValues.totalInviteesCount).append(convidadosText).append(")");
		} else {
			if (eventValues.isUniqueUserInvited)
				result.append("você");
			else
				result.append(eventValues.totalInviteesCount).append(convidadosText);
		}			

		return result.toString();
	}
		
	
	public void setSelected(boolean selected) {
		if (selected)
			addStyleName("a-event-view-selected");
		else 
			removeStyleName("a-event-view-selected");
	}
	
	
	private void addRemovalButton(EventVO event) {
		String style = event.isEditable
			? "a-event-delete-button"
			: "a-event-remove-button";
		Button button = addRemovalButton(event, style);
		button.setDescription(
			event.isEditable
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
