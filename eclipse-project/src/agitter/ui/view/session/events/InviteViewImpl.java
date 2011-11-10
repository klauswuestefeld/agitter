package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import vaadinutils.AutoCompleteChooser;
import agitter.ui.view.AgitterVaadinUtils;
import agitter.ui.view.session.contacts.SelectableRemovableElementList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

class InviteViewImpl extends CssLayout implements InviteView {

	private final TextArea description = new TextArea("Qual é o agito?");
	private String descriptionValue = " ";
	
	private final PopupDateField date = new PopupDateField("Quando?");
	private final AutoCompleteChooser nextInvitee = new AutoCompleteChooser("Quem você quer convidar?");
	private final SelectableRemovableElementList invitations = new SelectableRemovableElementList();

	private final Label descriptionLabel = new Label();
	private final Label dateLabel = new Label();	
	
	private boolean listenersActive = false;
	
	InviteViewImpl(Predicate<String> newInviteeValidator, final Runnable onInvite) {
		ValueChangeListener autosave = new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
				if (listenersActive) onInvite.run();
			}
		};
		
		addStyleName("a-invite-view");

		addComponent(dateLabel);
		addComponent(descriptionLabel);
		
		date.setResolution(DateField.RESOLUTION_MIN);
		date.setDateFormat("dd/MM/yyyy HH:mm");
		date.addListener(autosave);
		addComponent(date); date.addStyleName("a-invite-date");
		description.setSizeUndefined();
		description.addListener(new TextChangeListener() {  @Override public void textChange(TextChangeEvent event) {
			descriptionValue = event.getText();
			if (listenersActive) onInvite.run();
		}});
		
		addComponent(description); description.addStyleName("a-invite-description");

		nextInvitee.setListener(newInviteeValidator, new Consumer<String>() { @Override public void consume(String invitee) {
			onNextInvitee(invitee);
			if (listenersActive) onInvite.run();
		}});

		addComponent(nextInvitee); nextInvitee.addStyleName("a-invite-next-invitee");

		invitations.setRemoveListener(new Consumer<String>() { @Override public void consume(String value) {
			onInviteeRemoved(value);
			if (listenersActive) onInvite.run();
		}});
		addComponent(invitations); invitations.addStyleName("a-invite-invitations");

		description.focus();
		
		listenersActive = true;
	}
	
	private void onNextInvitee(String invitee) {
		invitations.addElement(invitee);
	}

	
	private void onInviteeRemoved(String invitee) {
		invitations.removeEement(invitee);
	}

	
	@Override
	public String eventDescription() {
		return descriptionValue;
		//return (String) description.getValue();
	}


	@Override
	public void reset() {
		listenersActive = false;
		
		description.setValue(null);
		descriptionValue = " ";
		description.setNullRepresentation("");
		description.setInputPrompt("Descrição do agito...");
		date.setValue(null);
		date.setInputPrompt("Data do agito...");
		nextInvitee.setInputPrompt("Email...");
		invitations.removeAllElements();
		
		listenersActive = true;
	}


	@Override
	public void refreshInviteesToChoose(List<String> inviteesToChoose) {
		nextInvitee.setChoices(inviteesToChoose);
	}


	@Override
	public Date datetime() {
		Object result = date.getValue();
		return result==null ? null : (Date) result;
	}


	@Override
	public List<String> invitees() {
		return invitations.getElements();
	}


	@Override
	public void display(String description, Date datetime, List<String> invitees) {
		this.description.setValue(description);
		this.descriptionValue = description;
		this.date.setValue(datetime);
		invitations.removeAllElements();
		invitations.addElements(invitees);
		
		descriptionLabel.setValue(description);
		dateLabel.setValue(datetime);
	}


	@Override
	public void enableEdit(boolean b) {
		description.setVisible(b);
		date.setVisible(b);
		nextInvitee.setVisible(b);
		invitations.setVisible(b);

		descriptionLabel.setVisible(!b);
		dateLabel.setVisible(!b);
	}

}
