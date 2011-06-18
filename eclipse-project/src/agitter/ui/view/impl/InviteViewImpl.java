package agitter.ui.view.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import sneer.foundation.lang.Predicate;
import agitter.ui.view.AgitterTheme;
import agitter.ui.view.InviteView;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

class InviteViewImpl extends CssLayout implements InviteView {

	private final TextArea description = new TextArea("Qual é o agito?");
	private final PopupDateField date = new PopupDateField("Quando?");
	private final NativeButton invite = new NativeButton("Agitar!");
	private final ComboBox nextInvitation = new ComboBox("Quem você quer convidar?");
	private final VerticalLayout invitations = new VerticalLayout();

	private Predicate<String> newInviteeValidator;

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
		
        nextInvitation.setNewItemsAllowed(true);
        nextInvitation.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        nextInvitation.setImmediate(true);
        nextInvitation.setNullSelectionAllowed(false);
        nextInvitation.addListener(new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
        	String value = event.getProperty().toString();
			if (!newInviteeValidator.evaluate(value)) return;

			Button invitationButton = new Button(value);
			invitationButton.addListener(new ClickListener() {  @Override public void buttonClick(ClickEvent event) {
				invitations.removeComponent(event.getButton());	
			}});
			invitations.addComponent(invitationButton);
			nextInvitation.setValue(null);
		}});

		addComponent(nextInvitation);
		addComponent(invitations);
		
		description.focus();
	}

	
	@Override
	public String eventDescription() {
		return (String)description.getValue();
	}
	
	
	@Override
	public void reset(List<String> inviteesToChoose,	Predicate<String> newInviteeValidator, final Runnable onInvite) {
		clearFields();
		for (String invitee : inviteesToChoose)
			nextInvitation.addItem(invitee);
		this.newInviteeValidator = newInviteeValidator;
		invite.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			onInvite.run();			
		}});
	}


	@Override
	public Date datetime() {
		Object result = date.getValue();
		return result == null ? null : (Date)result;
	}

	
	private void clearFields() {
		description.setValue(null);
		description.setNullRepresentation("");
		description.setInputPrompt("Descrição do agito...");
		date.setValue(null);
		date.setInputPrompt("Data do agito...");
		invitations.removeAllComponents();
	}


	@Override
	public List<String> invitees() {
		List<String> result = new ArrayList<String>();
		Iterator<Component> it = invitations.getComponentIterator();
		while (it.hasNext())
			result.add(((Component)it.next()).getCaption());			
		return result;
	}

}
