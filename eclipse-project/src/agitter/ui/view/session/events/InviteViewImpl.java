package agitter.ui.view.session.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import agitter.ui.view.AgitterVaadinUtils;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.*;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import vaadinutils.AutoCompleteChoosePanel;

class InviteViewImpl extends CssLayout implements InviteView {

	private final TextArea description = new TextArea("Qual é o agito?");
	private final PopupDateField date = new PopupDateField("Quando?");
	private final AutoCompleteChoosePanel nextInvitee = new AutoCompleteChoosePanel("Quem você quer convidar?");
	private final CssLayout invitations = new CssLayout();

	InviteViewImpl(Predicate<String> newInviteeValidator, final Runnable onInvite) {
		NativeButton invite = AgitterVaadinUtils.createDefaultNativeButton("Agitar!");
		invite.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent ignored) {
				onInvite.run();
			}
		});
		addStyleName("a-invite-view");
		description.setSizeUndefined();
		addComponent(description);
		description.addStyleName("a-invite-description");
		date.setResolution(DateField.RESOLUTION_MIN);
		date.setDateFormat("dd/MM/yyyy HH:mm");
		addComponent(date);
		date.addStyleName("a-invite-date");

		nextInvitee.setListener(newInviteeValidator, new Consumer<String>() { @Override public void consume(String invitee) {
			onNextInvitee(invitee);
		}});

		addComponent(nextInvitee);
		addComponent(invite);
		invite.addStyleName("a-invite-send");

		addComponent(invitations);
		invitations.addStyleName("a-invite-invitations");

		description.focus();
	}


	private void onNextInvitee(String invitee) {
		NativeButton invitationRemover = new NativeButton(invitee);
		invitationRemover.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			invitations.removeComponent(event.getButton());
		}});
		invitations.addComponent(invitationRemover);
		invitationRemover.addStyleName("a-invite-invited");
	}


	@Override
	public String eventDescription() {
		return (String) description.getValue();
	}


	@Override
	public void reset(List<String> inviteesToChoose) {
		clearFields();
		nextInvitee.setChoices(inviteesToChoose);
	}


	@Override
	public Date datetime() {
		Object result = date.getValue();
		return result==null ? null : (Date) result;
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
		while(it.hasNext()) { result.add(it.next().getCaption()); }
		return result;
	}

}
