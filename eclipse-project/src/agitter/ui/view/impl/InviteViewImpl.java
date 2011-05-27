package agitter.ui.view.impl;

import java.util.Date;

import agitter.ui.view.AgitterTheme;
import agitter.ui.view.InviteView;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

public class InviteViewImpl extends CssLayout implements InviteView {

	private static final long serialVersionUID = 1L;

	private TextArea description = new TextArea("Qual é o agito?");
	private PopupDateField date = new PopupDateField("Quando?");
	private NativeButton invite = new NativeButton("Agitar!");

	public InviteViewImpl() {
		addStyleName("a-invite-view");
		description.setWidth("500px");
		description.setHeight("55px");
		addComponent(description); description.addStyleName("a-invite-description");
		date.setResolution(DateField.RESOLUTION_MIN);
		date.setDateFormat("dd/MM/yyyy HH:mm");
		addComponent(date); date.addStyleName("a-invite-date");
		addComponent(invite); invite.addStyleName("a-invite-send"); 
		invite.addStyleName(AgitterTheme.DEFAULT_NATIVE_BUTTON_CLASS);
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
