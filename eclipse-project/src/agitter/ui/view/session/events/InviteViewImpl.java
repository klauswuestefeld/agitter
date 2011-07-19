package agitter.ui.view.session.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import vaadinutils.AutoCompleteComboBox;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

class InviteViewImpl extends CssLayout implements InviteView {

	private final TextArea description = new TextArea("Qual é o agito?");
	private final PopupDateField date = new PopupDateField("Quando?");
	private final NativeButton invite = AgitterVaadinUtils.createDefaultNativeButton("Agitar!");
	private final AutoCompleteComboBox nextInvitee = new AutoCompleteComboBox("Quem você quer convidar?");
	private final CssLayout invitations = new CssLayout();

	public InviteViewImpl(Predicate<String> newInviteeValidator, final Runnable onInvite) {
		invite.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			onInvite.run();			
		}});
		addStyleName("a-invite-view");
		description.setSizeUndefined();
		addComponent(description); description.addStyleName("a-invite-description");
		date.setResolution(DateField.RESOLUTION_MIN);
		date.setDateFormat("dd/MM/yyyy HH:mm");
		addComponent(date); date.addStyleName("a-invite-date");
		
        nextInvitee.addListener(newInviteeValidator, new Consumer<String>() {  @Override public void consume(String invitee) {
			onNextInvitee(invitee);
		}});
		nextInvitee.setWidth("245px");  // Impossible to set via CSS
        addComponent(nextInvitee); nextInvitee.addStyleName("a-invite-next-invitee");
		addComponent(invite); invite.addStyleName("a-invite-send");

        addComponent(invitations); invitations.addStyleName("a-invite-invitations");
		
		description.focus();
	}


	private void onNextInvitee(String invitee) {
		NativeButton invitationRemover = new NativeButton(invitee);
		invitationRemover.addListener(new ClickListener() {  @Override public void buttonClick(ClickEvent event) {
			invitations.removeComponent(event.getButton());	
		}});
		invitations.addComponent(invitationRemover); invitationRemover.addStyleName("a-invite-invited");
	}

	
	@Override
	public String eventDescription() {
		return (String)description.getValue();
	}
	
	
	@Override
	public void reset(List<String> inviteesToChoose) {
		clearFields();
		for (String invitee : inviteesToChoose)
			nextInvitee.addItem(invitee);
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
		nextInvitee.setInputPrompt("Email...");
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
