package agitter.ui.view.impl;

import java.util.Date;

import agitter.ui.view.InviteView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.Reindeer;

public class InviteViewImpl extends Panel implements InviteView {

	private TextArea description = new TextArea("Qual é o agito?");
	private PopupDateField date = new PopupDateField("Quando?");
	private NativeButton invite = new NativeButton("Agitar!");

	public InviteViewImpl() {
		setScrollable(false);
		addStyleName(Reindeer.PANEL_LIGHT);
		addStyleName("a-invite-view");
		VerticalLayout content = (VerticalLayout)getContent();
		content.setSpacing(true);
			description.setWidth("500px");
			description.setHeight("55px");
			content.addComponent(description); description.addStyleName("a-invite-description");
			date.setResolution(DateField.RESOLUTION_MIN);
			date.setDateFormat("dd/MM/yyyy HH:mm");
			content.addComponent(date);
			invite.addStyleName("a-invite-button");
			content.addComponent(invite);
			description.focus();
	}

	
	@Override
	public String getEventDescription() {
		return (String)description.getValue();
	}


	@Override
	public void onInvite(final Runnable action) {
		invite.addListener(new ClickListener() { private static final long serialVersionUID = 1L; @Override public void buttonClick(ClickEvent ignored) {
			action.run();			
		}});
	}

	@Override
	public Date getDatetime() {
		Object result = date.getValue();
		return result == null
			? null
			: (Date)result;
	}

	@Override
	public void clearFields() {
		description.setValue(null);
		description.setNullRepresentation("");
		description.setInputPrompt("Descrição do agito...");
		date.setValue(null);
		date.setInputPrompt("Data do agito...");
	}

}
