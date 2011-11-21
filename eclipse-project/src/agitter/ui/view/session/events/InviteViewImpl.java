package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import vaadinutils.AutoCompleteChooser;
import agitter.ui.view.session.contacts.SelectableRemovableElementList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

class InviteViewImpl extends CssLayout implements InviteView {

	private final PopupDateField date = new PopupDateField();
	private final TextArea description = new TextArea();
	private String descriptionValue = " ";
	private final AutoCompleteChooser nextInvitee = new AutoCompleteChooser();
	private final SelectableRemovableElementList invitations = new SelectableRemovableElementList();

	private final Label readOnlyDate = new Label();
	private final Label readOnlyDescription = new Label();
	
	private boolean listenersActive = false;
	private final Runnable onInvite;
	
	
	InviteViewImpl(Predicate<String> newInviteeValidator, final Runnable onInvite) {
		this.onInvite = onInvite;
		
		date.setInputPrompt("Escolha uma data");
		description.setNullRepresentation("");
		description.setInputPrompt("Descreva o agito");
		nextInvitee.setInputPrompt("Convide amigos por email ou grupo");

		addStyleName("a-invite-view");

		addComponent(readOnlyDate);
		addComponent(readOnlyDescription);
		
		date.setResolution(DateField.RESOLUTION_MIN);
		date.setDateFormat("dd/MM/yyyy HH:mm");
		date.addListener(new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
			autosave();
		}});
		addComponent(date); date.addStyleName("a-invite-date");
		description.setSizeUndefined();
		description.addListener(new TextChangeListener() {  @Override public void textChange(TextChangeEvent event) {
			descriptionValue = event.getText();
			autosave();
		}});
		
		addComponent(description); description.addStyleName("a-invite-description");

		nextInvitee.setListener(newInviteeValidator, new Consumer<String>() { @Override public void consume(String invitee) {
			onNextInvitee(invitee);
			autosave();
		}});

		addComponent(nextInvitee); nextInvitee.addStyleName("a-invite-next-invitee");

		invitations.setRemoveListener(new Consumer<String>() { @Override public void consume(String value) {
			autosave();
		}});
		addComponent(invitations); invitations.addStyleName("a-invite-invitations");

		description.focus();
		
		listenersActive = true;
	}
	
	private void onNextInvitee(String invitee) {
		invitations.addElement(invitee);
	}

	
	@Override
	public String eventDescription() {
		return descriptionValue;
	}


	@Override
	public void clear() {
		showReadOnlyLabels(false);
		showEditFields(false);
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
		listenersActive = false;
		
		this.description.setValue(description);
		this.descriptionValue = description;
		this.date.setValue(datetime);
		invitations.removeAllElements();
		invitations.addElements(invitees);
		
		readOnlyDescription.setValue(description);
		readOnlyDate.setValue(datetime);

		listenersActive = true;
	}


	@Override
	public void enableEdit(boolean b) {
		//The methods below have to be called in this order or the readOnlyLabels will never be shown.
		showReadOnlyLabels(!b);
		showEditFields(b);
	}

	
	private void showEditFields(boolean b) {
		description.setVisible(b);
		date.setVisible(b);
		nextInvitee.setVisible(b);
		invitations.setVisible(b);
	}

	
	private void showReadOnlyLabels(boolean b) {
		readOnlyDescription.setVisible(b);
		readOnlyDate.setVisible(b);
	}

	
	private void autosave() {
		if (listenersActive) onInvite.run();
	}

}
