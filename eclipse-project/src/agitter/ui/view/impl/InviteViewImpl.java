package agitter.ui.view.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import agitter.ui.view.AgitterTheme;
import agitter.ui.view.InviteView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

public class InviteViewImpl extends CssLayout implements InviteView {

	private static final long serialVersionUID = 1L;

	private final TextArea description = new TextArea("Qual é o agito?");
	private final PopupDateField date = new PopupDateField("Quando?");
	private final NativeButton invite = new NativeButton("Agitar!");
	private final ComboBox nextInvitation = new ComboBox("Quem você quer convidar?");
	private final CssLayout invitations = new CssLayout();

	public InviteViewImpl() {
		addStyleName("a-invite-view");
		description.setSizeUndefined();
		addComponent(description); description.addStyleName("a-invite-description");
		date.setResolution(DateField.RESOLUTION_MIN);
		date.setDateFormat("dd/MM/yyyy HH:mm");
		addComponent(date); date.addStyleName("a-invite-date");
		
        nextInvitation.setNewItemsAllowed(true);
        nextInvitation.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        nextInvitation.setImmediate(true);
        nextInvitation.setNullSelectionAllowed(false);
        nextInvitation.addListener(new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
        	String value = event.getProperty().toString();
			if(value==null) return;
        	
        	InviteViewImpl.this.getWindow().showNotification("> " + value);
			NativeButton invitationButton = new NativeButton(value);
			invitationButton.addListener(new ClickListener() {  @Override public void buttonClick(ClickEvent event) {
				invitations.removeComponent(event.getButton());	
			}});
			invitations.addComponent(invitationButton); invitationButton.addStyleName("a-invite-invited");
			nextInvitation.setValue(null);
		}});
		nextInvitation.setWidth("245px");  // Impossible to set via CSS
        addComponent(nextInvitation); nextInvitation.addStyleName("a-invite-next-invitation");
		addComponent(invite); invite.addStyleName("a-invite-send");
		invite.addStyleName(AgitterTheme.DEFAULT_NATIVE_BUTTON_CLASS);

        addComponent(invitations); invitations.addStyleName("a-invite-invitations");
		
		description.focus();
	}

	
	@Override
	public String getEventDescription() {
		return (String)description.getValue();
	}


	@Override
	public void setContacts(List<String> contacts) {
		for (String c : contacts)
			nextInvitation.addItem(c);
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
		nextInvitation.setInputPrompt("Email...");
		invitations.removeAllComponents();
	}


	@Override
	public List<String> invitations() {
		List<String> result = new ArrayList<String>();
		Iterator<Component> it = invitations.getComponentIterator();
		while (it.hasNext()) {
			Component component = (Component) it.next();
			result.add(component.getCaption());			
		}
		return result;
	}

}
