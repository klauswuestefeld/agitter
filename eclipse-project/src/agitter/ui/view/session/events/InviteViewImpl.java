package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import vaadinutils.AutoCompleteChooser;
import agitter.ui.view.session.contacts.SelectableRemovableElementList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
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
	private final AutoCompleteChooser nextInvitee = new AutoCompleteChooser();
	private final SelectableRemovableElementList invitations = new SelectableRemovableElementList();

	private final Label readOnlyDate = new Label();
	private final Label readOnlyDescription = new Label();
	
	private Boss boss;
	private boolean listenersActive = false;
	
	
	InviteViewImpl() {
		addStyleName("a-invite-view");
		
		addComponent(readOnlyDate);
		addComponent(readOnlyDescription);
		addDateComponent();
		addDescriptionComponent();
		addNextInviteeComponent();
		addInvitationsComponent();
	
		listenersActive = true;
	}


	@Override
	public void startReportingTo(Boss boss) {
		if (this.boss != null) throw new IllegalStateException();
		this.boss = boss;
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
	public void display(String description, Date datetime, List<String> invitees) {
		listenersActive = false;
	
		this.description.setValue(description);
		this.date.setValue(datetime);
		invitations.removeAllElements();
		invitations.addElements(invitees);
		
		readOnlyDescription.setValue(description);
		readOnlyDate.setValue(datetime);

		listenersActive = true;
	}

	public void focusOnDescription() {
		this.description.focus();
	}
	
	public void focusOnDate() {
		this.date.focus();
	}

	@Override
	public void enableEdit(boolean b) {
		//The methods below have to be called in this order or the readOnlyLabels will never be shown.
		showReadOnlyLabels(!b);
		showEditFields(b);
	}

	@Override
	public void enableNewEvent() {
		date.setVisible(true);
		description.setVisible(false);
		nextInvitee.setVisible(false);
		invitations.setVisible(false);
	}
	
	private boolean onNextInvitee(String invitee) {
		boolean result = boss.approveInviteeAdd(invitee);
		if (result)
			invitations.addElement(invitee);
		return result;
	}
	
	
	private void showEditFields(boolean b) {
		date.setVisible(b);
		description.setVisible(b);
		nextInvitee.setVisible(b);
		invitations.setVisible(b);
	}
	
	
	private void showReadOnlyLabels(boolean b) {
		readOnlyDescription.setVisible(b);
		readOnlyDate.setVisible(b);
	}

	
	private void addNextInviteeComponent() {
		nextInvitee.setInputPrompt("Convide amigos por email ou grupo");
		nextInvitee.setListener(new Predicate<String>() { @Override public boolean evaluate(String invitee) {
			return onNextInvitee(invitee);
		}});
		addComponent(nextInvitee); nextInvitee.addStyleName("a-invite-next-invitee");
	}


	private void addInvitationsComponent() {
		invitations.setRemoveListener(new Consumer<String>() { @Override public void consume(String invitee) {
			boss.onInviteeRemoved(invitee);
		}});
		addComponent(invitations); invitations.addStyleName("a-invite-invitations");
	}
	
	
	private void addDescriptionComponent() {
		description.setNullRepresentation("");
		description.setInputPrompt("Descreva o agito");
		description.setSizeUndefined();
		description.addListener(new TextChangeListener() {  @Override public void textChange(TextChangeEvent event) {
			if (!listenersActive) return;
			boss.onDescriptionEdit(event.getText());
		}});
		addComponent(description); description.addStyleName("a-invite-description");
	}
	
	
	private void addDateComponent() {
		date.setInputPrompt("Escolha uma data");
		date.setResolution(DateField.RESOLUTION_MIN);
		date.setDateFormat("dd/MM/yyyy HH:mm");
		date.addListener(new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
			onDatetimeEdit();
		}});
		date.addListener(new BlurListener() { @Override public void blur(BlurEvent event) { 
			onDatetimeEdit();
		}});
		
		addComponent(date); date.addStyleName("a-invite-date");
	}


	private void onDatetimeEdit() {
		if (!listenersActive) return;
		boss.onDatetimeEdit((Date)date.getValue());
	}

}
