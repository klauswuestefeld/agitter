package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Pair;
import sneer.foundation.lang.Predicate;
import vaadinutils.AutoCompleteChooser;
import vaadinutils.AutoCompleteChooser.FullFeaturedItem;
import vaadinutils.MultipleDatePopup;
import vaadinutils.WidgetUtils;
import agitter.ui.helper.AgitterDateFormatter;
import agitter.ui.helper.HTMLFormatter;
import agitter.ui.view.session.contacts.SelectableRemovablePairList;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;

class EventViewImpl extends CssLayout implements EventView {
	
	static private final AgitterDateFormatter dateFormat = new AgitterDateFormatter();

	private final MultipleDatePopup multipleDate = new MultipleDatePopup();
	private final TextArea description = new TextArea();
	private final AutoCompleteChooser nextInvitee = new AutoCompleteChooser();
	private final Label invitationsHeader = WidgetUtils.createLabel();
	private final SelectableRemovablePairList invitations = new SelectableRemovablePairList();
	
	private final Button deleteButton;
	private final Button blockButton;
	
	private final Label readOnlyDates = WidgetUtils.createLabelXHTML(""); 
	private final Label readOnlyDescription = WidgetUtils.createLabelXHTML("");
	private final Label readOnlyOwner = WidgetUtils.createLabelXHTML("");
	private final Label readOnlyInviteesHeader = WidgetUtils.createLabelXHTML("");
	private final Label readOnlyInviteesList = WidgetUtils.createLabelXHTML("");
	
	private Boss boss;
	private boolean saveListenersActive = false;

	private final CommentsViewImpl commentsView = new CommentsViewImpl();

	NativeButton buttonOnlyInvitedSign;
	NativeButton buttonPublicSign;
	CssLayout opennessLine;
	
	EventViewImpl() {
		addStyleName("a-invite-view");

		deleteButton = addRemoveButton("Excluir este Agito"); deleteButton.addStyleName("a-event-delete-button");
		blockButton = addRemoveButton("Bloquear este Agito"); blockButton.addStyleName("a-event-block-button");
		addMultipleDateComponent();
		addDescriptionComponent();
		addPublicEventOption();
		addNextInviteeComponent();
		addInvitationsComponents();
		
		addComponent(readOnlyDates); readOnlyDates.addStyleName("a-invite-readonly-date");
		addComponent(readOnlyDescription); readOnlyDescription.addStyleName("a-invite-readonly-description");
		addComponent(readOnlyOwner); readOnlyOwner.addStyleName("a-invite-readonly-owner");
		addComponent(readOnlyInviteesHeader); readOnlyInviteesHeader.addStyleName("a-invite-readonly-invitees-header");
		addComponent(readOnlyInviteesList); readOnlyInviteesList.addStyleName("a-invite-readonly-invitees-list");
		
		addComponent(commentsView);
		
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
	public void refreshInviteesToChoose(List<FullFeaturedItem> inviteesToChoose) {
		nextInvitee.setChoices(inviteesToChoose);
	}


	@Override
	public void displayEditting(String description, long[] datetimes, List<FullFeaturedItem> invitees, int totalInviteesCount, boolean isPublicEvent) {
		editAll(true);
		saveListenersActive = false;
	
		this.description.setValue(description);
		this.multipleDate.setValue(datetimes);
		refreshInvitationsHeader(totalInviteesCount);
		invitations.removeAllElements();
		invitations.addElements(invitees);

		saveListenersActive = true;

		if (description.isEmpty())
			this.multipleDate.focus();
		else
			this.description.focus();
		
		setPublic(isPublicEvent);
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
	public void displayReadOnly(Pair<String,String> owner, String description, long[] datetimes, List<FullFeaturedItem> knownInvitees, int totalInviteesCount, boolean isPublicEvent) {
		editAll(false);
		readOnlyDescription.setValue(new HTMLFormatter().makeClickable(description));
		
		if (datetimes.length > 1) {		
			StringBuilder buff = new StringBuilder();
			buff.append("Próximas Datas: <UL>");
			for (long date : datetimes) {
				buff.append("<LI>"); 
				buff.append(dateFormat.format(new Date(date)));
				buff.append("</LI>");
			}
			buff.append("</UL>");;
			readOnlyDates.setValue(buff.toString());
		} else if(datetimes.length == 1)  {
			readOnlyDates.setValue(dateFormat.format(new Date(datetimes[0])));
		} else {
			readOnlyDates.setValue("");
		}
		
		displayReadOnlyInvitees(owner, knownInvitees, totalInviteesCount);
	}
	
	private String getHTMLName(String key, String value, String icon) {
		if (value == null || value.isEmpty()) return key;
		
		if (icon == null)
			return "<span class='a-remov-elem-list-element-value'>" + value + "</span>" +
				   "<span class='a-remov-elem-list-element-key'>" + key + "</span>";
		else 
			return "<img src='" + icon + "' class='v-icon v-icon-list'/>" +  
				   "<span class='a-remov-elem-list-element-value'>" + value + "</span>" +
			       "<span class='a-remov-elem-list-element-key'>" + key + "</span>";
	}
	
	private void displayReadOnlyInvitees(Pair<String,String> owner, List<FullFeaturedItem> knownInvitees, int totalInviteesCount) {
		//readOnlyOwner.setValue(owner.b != null ? owner.b : owner.a);
		readOnlyOwner.setValue(getHTMLName(owner.a,owner.b, null));
		
		StringBuffer header;

		if (totalInviteesCount == 1)
			header = new StringBuffer("+ você");
		else
			header = new StringBuffer("+ " + totalInviteesCount + " convidados" );
			
		StringBuffer list = new StringBuffer();
		if (knownInvitees.size() > 0) {
			header.append(":" );
			list.append("<ul>");
			for (FullFeaturedItem invitee: knownInvitees)
				list.append("<li>" + getHTMLName(invitee.key, invitee.caption, invitee.icon) + "</li>");

			list.append("<li>você</li>");

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
	

	private boolean onNextInvitee(String key) {
		boolean result = boss.approveInviteesAdd(key);
		if (result)
			invitations.addElement(key, "");
		return result;
	}
	
	
	private void showEditFields(boolean b) {
		deleteButton.setVisible(b);
		multipleDate.setVisible(b);
		opennessLine.setVisible(b);
		description.setVisible(b);
		nextInvitee.setVisible(b);
		invitationsHeader.setVisible(b);
		invitations.setVisible(b);
	}
	
	private void showReadOnlyLabels(boolean b) {
		blockButton.setVisible(b);
		readOnlyDescription.setVisible(b);
		readOnlyDates.setVisible(b);
		readOnlyOwner.setVisible(b);
		readOnlyInviteesHeader.setVisible(b);
		readOnlyInviteesList.setVisible(b);
	}

	
	private void addNextInviteeComponent() {
		nextInvitee.setInputPrompt("Digite um grupo, email ou cole vários emails para convidar");
		nextInvitee.setListener(new Predicate<String>() { @Override public boolean evaluate(String invitee) {
			return onNextInvitee(invitee);
		}});
		// Peccin Favor Revisar:
		// Why setting this width here? It works without it. 
		// WARNING: This breaks the layout. Go to a editable event, readonly event and editable event again. Buttons disappear.
		nextInvitee.setInputWidth("280px"); //Colocar largura certa
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
	
	private void addMultipleDateComponent() {
		multipleDate.setInputPrompt("Escolha uma data");
		multipleDate.setResolution(DateField.RESOLUTION_MIN);
		multipleDate.setDateFormat("dd/MM/yyyy HH:mm");
		multipleDate.setRemoveListener(new Consumer<Long>() { @Override public void consume(Long date) {
			if (!saveListenersActive) return;
			boss.onDateRemoved(date);
		}});
		multipleDate.setAddListener(new Consumer<Long>() { @Override public void consume(Long date) {
			if (!saveListenersActive) return;
			boss.onDateAdded(date);
		}});
		multipleDate.setChangeListener(new Consumer<MultipleDatePopup.DateChanged>() { @Override public void consume(MultipleDatePopup.DateChanged dates) {
			if (!saveListenersActive) return;
			boss.onDateChanged(dates.from, dates.to);
		}});
		
		addComponent(multipleDate); multipleDate.addStyleName("a-invite-multiply-date");
	}
	
	private void addPublicEventOption() {
		buttonOnlyInvitedSign = new NativeButton();
		buttonOnlyInvitedSign.setSizeUndefined();
		
		Button buttonOnlyInvited = WidgetUtils.createLinkButton("Só a panelinha");
		buttonOnlyInvited.setSizeUndefined();
		
		buttonOnlyInvited.addStyleName("a-invite-openness-link");
		buttonOnlyInvitedSign.addStyleName("a-invite-openness-button");
		//buttonOnlyInvitedSign.addStyleName("a-default-nativebutton");
		//button.addStyleName("a-event-going-button-active");

		buttonOnlyInvited.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			if (!saveListenersActive) return;
			boss.onOpennessChanged(false);
			setPublic(false);
			requestRepaintAll();
		}});
		buttonOnlyInvitedSign.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			if (!saveListenersActive) return;
			boss.onOpennessChanged(false);
			setPublic(false);
			requestRepaintAll();
		}});
		
		buttonPublicSign = new NativeButton();
		buttonPublicSign.setSizeUndefined();
		
		Button buttonPublic = WidgetUtils.createLinkButton("Evento público");
		buttonPublic.setSizeUndefined();
		
		buttonPublic.addStyleName("a-invite-openness-link");
		buttonPublicSign.addStyleName("a-invite-openness-button");
		//buttonPublicSign.addStyleName("a-default-nativebutton");
		buttonPublic.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			if (!saveListenersActive) return;
			boss.onOpennessChanged(true);
			setPublic(true);
			requestRepaintAll();
		}});
		buttonPublicSign.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			if (!saveListenersActive) return;
			boss.onOpennessChanged(true);
			setPublic(true);
			requestRepaintAll();
		}});
		
		Button updateContacts = WidgetUtils.createLinkButton("Atualizar Contatos");
		updateContacts.setSizeUndefined();
		updateContacts.addStyleName("a-invite-openness-link");
		updateContacts.addStyleName("a-invite-openness-update-contact");
		updateContacts.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			if (!saveListenersActive) return;
			boss.onUpdateContacts();
		}});
		
		opennessLine = new CssLayout();
		opennessLine.addStyleName("a-invite-openness");
		opennessLine.addComponent(buttonPublicSign);
		opennessLine.addComponent(buttonPublic); 
		opennessLine.addComponent(buttonOnlyInvitedSign);
		opennessLine.addComponent(buttonOnlyInvited);
		opennessLine.addComponent(updateContacts);
		addComponent(opennessLine);
	}
	
	private void setPublic(boolean isPublicEvent) {
		if (isPublicEvent) {
			buttonPublicSign.setStyleName("a-invite-openness-button-active");
			buttonOnlyInvitedSign.setStyleName("a-invite-openness-button");
		} else {
			buttonPublicSign.setStyleName("a-invite-openness-button");
			buttonOnlyInvitedSign.setStyleName("a-invite-openness-button-active");			
		}
	}

	private Button addRemoveButton(String caption) {
		Button ret = WidgetUtils.createLinkButton(caption);
		ret.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			boss.onEventRemoved();
		}});
		addComponent(ret);
		return ret;
	}

	
	@Override
	public CommentsView commentsView() {
		return commentsView;
	}

}
