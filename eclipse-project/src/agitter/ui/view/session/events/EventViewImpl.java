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
	private final SelectableRemovableElementList invitations = new SelectableRemovableElementList();

	private final Label readOnlyDate = WidgetUtils.createLabelXHTML(""); 
	private final Label readOnlyDescription = WidgetUtils.createLabelXHTML("");
	private final Label readOnlyInvited = WidgetUtils.createLabelXHTML("");
	
	private Boss boss;
	private boolean saveListenersActive = false;
	
	EventViewImpl() {
		addStyleName("a-invite-view");
		
		addComponent(readOnlyDate); readOnlyDate.addStyleName("a-invite-readonly-date");
		addDateComponent();
		
		addComponent(readOnlyDescription); readOnlyDescription.addStyleName("a-invite-readonly-description");
		addDescriptionComponent();
		
		addComponent(readOnlyInvited); readOnlyInvited.addStyleName("a-invite-readonly-invited");
		addNextInviteeComponent();
		addInvitationsComponent();
		
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
	public void display(String description, Date datetime, List<String> invitees, int unkownInvitees) {
		saveListenersActive = false;
	
		this.description.setValue(description);
		this.date.setValue(datetime);
		invitations.removeAllElements();
		invitations.addElements(invitees);
		
		displayReadOnly(description, datetime);
		displayInvitees(invitees, unkownInvitees);
		
		saveListenersActive = true;
	}

	private void displayInvitees(List<String> invitees, int unknownInvitees) {
		StringBuffer beautifulList = new StringBuffer();
		
		boolean someoneIsInvited = invitees.size()> 0 || unknownInvitees > 0;
		
		beautifulList.append((invitees.size() + unknownInvitees) + " convidados:");
				
		if (someoneIsInvited) {
			beautifulList.append("<ul>");
		}
		
		for (String invitee: invitees) {
			beautifulList.append("<li>" + invitee + "</li>");
		}

		if (unknownInvitees > 0) {
			beautifulList.append("<li>+ " + unknownInvitees + " pessoas</li>");
		}
		
		if (someoneIsInvited) {			
			beautifulList.append("</ul>");
		}
	
		readOnlyInvited.setValue(beautifulList.toString());
	}
	
	private void displayReadOnly(String description, Date datetime) {
		readOnlyDescription.setValue(new HTMLFormatter().makeClickable(description));
		readOnlyDate.setValue(dateFormat.format(datetime));
	}	
	
	@Override
	public void focusOnDescription() {
		this.description.focus();
	}
	
	
	@Override
	public void focusOnDate() {
		this.date.focus();
	}

	@Override
	public void editAll(boolean b) {
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
		invitations.setVisible(b);
	}
	
	
	private void showReadOnlyLabels(boolean b) {
		readOnlyDescription.setVisible(b);
		readOnlyDate.setVisible(b);
		readOnlyInvited.setVisible(b);
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
			if (!saveListenersActive) return;
			boss.onDescriptionEdit(event.getText());
			displayReadOnly(event.getText(), (Date)date.getValue());
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
		displayReadOnly((String)readOnlyDescription.getValue(), (Date)date.getValue());
	}

}
