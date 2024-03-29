package agitter.ui.view.session.events;

import java.util.Date;
import java.util.List;

import vaadinutils.AutoCompleteChooser;
import vaadinutils.MultipleDatePopup;
import vaadinutils.ProfileList;
import vaadinutils.ProfileListItem;
import vaadinutils.WidgetUtils;
import agitter.ui.helper.AgitterDateFormatter;
import agitter.ui.helper.HTMLFormatter;
import basis.lang.Consumer;
import basis.lang.Pair;
import basis.lang.Predicate;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;

class EventViewImpl extends CssLayout implements EventView {
	
	static private final AgitterDateFormatter dateFormat = new AgitterDateFormatter();

	private final MultipleDatePopup multipleDate = new MultipleDatePopup();
	private final TextArea description = new TextArea();
	private final AutoCompleteChooser nextInvitee = new AutoCompleteChooser();
	private final Label invitationsHeader = WidgetUtils.createLabel();
	private final ProfileList invitations = new ProfileList();
	
	private final Button deleteButton;
	private final Button notInterestedButton;
	private final Button spreadButton;
	
	private final Label readOnlyDates = WidgetUtils.createLabelXHTML(""); 
	private final Label readOnlyDescription = WidgetUtils.createLabelXHTML("");
	private final Label readOnlyOwner = WidgetUtils.createLabelXHTML("");
	private final Label readOnlyInviteesHeader = WidgetUtils.createLabelXHTML("");
	private final Label readOnlyInviteesList = WidgetUtils.createLabelXHTML("");
	
	private Boss boss;
	private boolean saveListenersActive = false;

	private final CommentsViewImpl commentsView = new CommentsViewImpl();


	EventViewImpl() {
		addStyleName("a-invite-view");

		deleteButton = addRemoveButton("Excluir este Agito"); deleteButton.addStyleName("a-invite-delete-button");
		notInterestedButton = addRemoveButton("Não Me Interessa"); notInterestedButton.addStyleName("a-invite-delete-button");
			
		addComponent(readOnlyDates); readOnlyDates.addStyleName("a-invite-readonly-date");
		addComponent(readOnlyDescription); readOnlyDescription.addStyleName("a-invite-readonly-description");
		addComponent(readOnlyOwner); readOnlyOwner.addStyleName("a-invite-readonly-owner");
		addComponent(readOnlyInviteesHeader); readOnlyInviteesHeader.addStyleName("a-invite-readonly-invitees-header");
		addComponent(readOnlyInviteesList); readOnlyInviteesList.addStyleName("a-invite-readonly-invitees-list");

		spreadButton = addSpreadButton("Reagitar!"); spreadButton.addStyleName("a-invite-readonly-spread-button");
		
		addMultipleDateComponent();
		addDescriptionComponent();
		addNextInviteeComponent();
		addInvitationsComponents();
		
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
	public void refreshInviteesToChoose(List<ProfileListItem> inviteesToChoose) {
		nextInvitee.setChoices(inviteesToChoose);
		
//		setWidth(nextInvitee);
	}


//	TODO Peccin Não sei por que colocaram isso aqui, mas me parece errado... Funciona sem isso, e sem quebrar o layout
//	public void setWidth(ComponentContainer cc) {
//		Iterator<Component> c = cc.getComponentIterator();
//		while (c.hasNext()) {
//			Component co = c.next();
//			if (!(co instanceof Button))
//				co.setWidth("380px");
//			if (co instanceof ComponentContainer) {
//				setWidth((ComponentContainer)co);
//			}
//		}
//	}

	@Override
	public void displayEditting(String description, long[] datetimes, List<ProfileListItem> invitees, int totalInviteesCount) {
		editAll(true);
		saveListenersActive = false;
	
		this.description.setValue(description);
		this.multipleDate.setValue(datetimes);
		refreshInvitationsHeader(totalInviteesCount, invitees);

		saveListenersActive = true;

		if (description.isEmpty())
			this.multipleDate.focus();
		else
			this.description.focus();
		
		spreadButton.setVisible(false);
	}

	@Override
	public void refreshInvitationsHeader(int totalInviteesCount, List<ProfileListItem> invitees) {
		invitationsHeader.setValue(
				totalInviteesCount == 0 
					? "" 
					: totalInviteesCount + (totalInviteesCount > 1 ? " convidados:"	: " convidado:")
		);
		
		invitations.removeAllElements();
		invitations.addElements(invitees);
	}
	

	@Override
	public void displayReadOnly(Pair<String,String> owner, String description, long[] datetimes, List<ProfileListItem> knownInvitees, int totalInviteesCount) {
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
		
		spreadButton.setVisible(true);
		
		displayReadOnlyInvitees(owner, knownInvitees, totalInviteesCount);
	}
		
	private void displayReadOnlyInvitees(Pair<String,String> owner, List<ProfileListItem> knownInvitees, int totalInviteesCount) {
		//readOnlyOwner.setValue(owner.b != null ? owner.b : owner.a);
		readOnlyOwner.setValue(new ProfileListItem(owner.a,owner.b, null));
		
		StringBuffer header;

		if (totalInviteesCount == 1)
			header = new StringBuffer("+ você");
		else
			header = new StringBuffer("+ " + totalInviteesCount + " convidados" );
			
		StringBuffer list = new StringBuffer();
		if (knownInvitees.size() > 0) {
			header.append(":" );
			list.append("<ul>");
			for (ProfileListItem invitee: knownInvitees)
				list.append("<li>" + invitee.toHTML() + "</li>");

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
		return boss.approveInviteesAdd(key);
	}
	
	private void showEditFields(boolean b) {
		deleteButton.setVisible(b);
		multipleDate.setVisible(b);
		description.setVisible(b);
		nextInvitee.setVisible(b);
		invitationsHeader.setVisible(b);
		invitations.setVisible(b);
	}
	
	private void showReadOnlyLabels(boolean b) {
		notInterestedButton.setVisible(b);
		readOnlyDescription.setVisible(b);
		readOnlyDates.setVisible(b);
		readOnlyOwner.setVisible(b);
		readOnlyInviteesHeader.setVisible(b);
		readOnlyInviteesList.setVisible(b);
		
		if (!b) spreadButton.setVisible(false);
	}

	
	private void addNextInviteeComponent() {
		nextInvitee.setInputPrompt("Digite um grupo, email ou cole vários emails para convidar");
		nextInvitee.setSizeUndefined();
		nextInvitee.setListener(new Predicate<String>() { @Override public boolean evaluate(String invitee) {
			return onNextInvitee(invitee);
		}});
		// Peccin Preciso setar o tamanho assim pq a combo não permite controlar o tamanho via CSS. Ela sempre faz override...
		nextInvitee.setInputWidth("407px");
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
	

	private Button addRemoveButton(String caption) {
		Button ret = WidgetUtils.createLinkButton(caption);
		ret.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			boss.onEventRemoved();
		}});
		addComponent(ret);
		return ret;
	}
	
	private Button addSpreadButton(String caption) {
		Button ret = WidgetUtils.createLinkButton(caption);
		ret.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent ignored) {
			openSpreadingView();
		}});
		addComponent(ret);
		return ret;
	}

	
	protected void openSpreadingView() {
		nextInvitee.setVisible(true);
		invitations.setVisible(true);
		invitations.removeAllElements();
		spreadButton.setVisible(false);
	}


	@Override
	public CommentsView commentsView() {
		return commentsView;
	}

}
