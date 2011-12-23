package agitter.ui.view.session.events;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import vaadinutils.AutoCompleteChooser;
import vaadinutils.WidgetUtils;
import agitter.ui.helper.HTMLFormatter;
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

class EventViewImpl extends CssLayout implements EventView {

	static private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private final PopupDateField date = new PopupDateField();
	private final TextArea description = new TextArea();
	private final AutoCompleteChooser nextInvitee = new AutoCompleteChooser();
	private final Label invitationsHeader = WidgetUtils.createLabel();
	private final SelectableRemovableElementList invitations = new SelectableRemovableElementList();

	private final Label readOnlyDate = WidgetUtils.createLabelXHTML(""); 
	private final Label readOnlyDescription = WidgetUtils.createLabelXHTML("");
	private final Label readOnlyOwner = WidgetUtils.createLabel();
	private final Label readOnlyInviteesHeader = WidgetUtils.createLabel();
	private final Label readOnlyInviteesList = WidgetUtils.createLabelXHTML("");
	
	private Boss boss;
	private boolean saveListenersActive = false;
	
	EventViewImpl() {
		addStyleName("a-invite-view");
		
		addDateComponent();
		addDescriptionComponent();
		addNextInviteeComponent();
		addInvitationsComponents();
		
		addComponent(readOnlyDate); readOnlyDate.addStyleName("a-invite-readonly-date");
		addComponent(readOnlyDescription); readOnlyDescription.addStyleName("a-invite-readonly-description");
		addComponent(readOnlyOwner); readOnlyOwner.addStyleName("a-invite-readonly-owner");
		addComponent(readOnlyInviteesHeader); readOnlyInviteesHeader.addStyleName("a-invite-readonly-invitees-header");
		addComponent(readOnlyInviteesList); readOnlyInviteesList.addStyleName("a-invite-readonly-invitees-list");
		
		saveListenersActive = true;
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
	public void displayEditting(String description, Date datetime, List<String> invitees, int totalInviteesCount) {
		editAll(true);
		saveListenersActive = false;
	
		this.description.setValue(description);
		this.date.setValue(datetime);
		refreshInvitationsHeader(totalInviteesCount);
		invitations.removeAllElements();
		invitations.addElements(invitees);

		saveListenersActive = true;

		if (description.isEmpty())
			this.date.focus();
		else
			this.description.focus();
	}


	@Override
	public void refreshInvitationsHeader(int totalInviteesCount) {
		invitationsHeader.setValue(
				totalInviteesCount == 0 
					? "" 
					: totalInviteesCount + (totalInviteesCount > 1 ? " convidados:"	: " convidado:")
		);
	}
	

	@Override
	public void displayReadOnly(String owner, String description, Date datetime, List<String> knownInvitees, int totalInviteesCount) {
		editAll(false);
		readOnlyDescription.setValue(new HTMLFormatter().makeClickable(description));
		readOnlyDate.setValue(dateFormat.format(datetime));
		displayReadOnlyInvitees(owner, knownInvitees, totalInviteesCount);
	}

	
	private void displayReadOnlyInvitees(String owner, List<String> knownInvitees, int totalInviteesCount) {
		readOnlyOwner.setValue(owner);
		
		StringBuffer header = new StringBuffer("+ você");
		if (totalInviteesCount > 1) {
			header.append(" e " + (totalInviteesCount -1) + " convidado" );
			if (totalInviteesCount > 2)
				header.append("s" );
		}
			
		StringBuffer list = new StringBuffer();
		if (knownInvitees.size() > 0) {
			header.append(":" );
			list.append("<ul>");
			for (String invitee: knownInvitees)
				list.append("<li>" + invitee + "</li>");

			final int unkownInviteesCount = totalInviteesCount -1 - knownInvitees.size();
			if (unkownInviteesCount > 0) {
				list.append("<li>+ " + unkownInviteesCount + " pessoa");
				if (unkownInviteesCount > 1)
					list.append("s");
				list.append("</li>");
			}
		
			list.append("</ul>");
		}
		
		readOnlyInviteesHeader.setValue(header.toString());
		readOnlyInviteesList.setValue(list.toString());
	}
	
	
	private void editAll(boolean b) {
		showReadOnlyLabels(!b);
		showEditFields(b);
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
		invitationsHeader.setVisible(b);
		invitations.setVisible(b);
	}
	
	
	private void showReadOnlyLabels(boolean b) {
		readOnlyDescription.setVisible(b);
		readOnlyDate.setVisible(b);
		readOnlyOwner.setVisible(b);
		readOnlyInviteesHeader.setVisible(b);
		readOnlyInviteesList.setVisible(b);
	}

	
	private void addNextInviteeComponent() {
		nextInvitee.setInputPrompt("Convide amigos por email ou grupo");
		nextInvitee.setListener(new Predicate<String>() { @Override public boolean evaluate(String invitee) {
			return onNextInvitee(invitee);
		}});
		nextInvitee.setInputWidth("280px");
		addComponent(nextInvitee); nextInvitee.addStyleName("a-invite-next-invitee");
	}


	private void addInvitationsComponents() {
		addComponent(invitationsHeader); invitationsHeader.addStyleName("a-invite-invitations-header");
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
			if (!saveListenersActive) return;
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
		if (!saveListenersActive) return;
		boss.onDatetimeEdit((Date)date.getValue());
	}

}
