package agitter.ui.view.session.contacts;


import java.util.List;

import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.ui.*;
import sneer.foundation.lang.Consumer;
import vaadinutils.AutoCompleteChooser;
import vaadinutils.VaadinUtils;

public class ContactsViewImpl implements ContactsView {

	private final ComponentContainer container;
	private final SelectableRemovableElementList groupList = new SelectableRemovableElementList();
	private final SelectableRemovableElementList memberList = new SelectableRemovableElementList();

	private final TextField newGroup = new TextField();
	private final NativeButton addGroupButton = AgitterVaadinUtils.createDefaultAddButton();
	private Consumer<String> newGroupConsumer;

	private final AutoCompleteChooser newMember = new AutoCompleteChooser(null);

	public ContactsViewImpl(ComponentContainer container) {
		this.container = container;
		Button.ClickListener addGroupListener = new Button.ClickListener() { @Override public void buttonClick(Button.ClickEvent clickEvent) {
			newGroupConsumer.consume((String) newGroup.getValue());
		}};
		addGroupButton.addListener(addGroupListener);
	}

	@Override
	public void show() {
		container.removeAllComponents();

		CssLayout contactsView = new CssLayout();
		container.addComponent(contactsView); contactsView.addStyleName("a-contacts-view");
		
		CssLayout groups = new CssLayout(); groups.addStyleName("a-contacts-groups");
		Label groupsCaption = VaadinUtils.createLabel("Grupos");
		groups.addComponent(groupsCaption); groupsCaption.addStyleName("a-contacts-groups-caption");
		groups.addComponent(newGroup); newGroup.addStyleName("a-contacts-groups-new");
		groups.addComponent(addGroupButton); addGroupButton.addStyleName("a-contacts-groups-new-add");
		groups.addComponent(groupList); groupList.addStyleName("a-contacts-groups-list");
		
		CssLayout members = new CssLayout(); members.addStyleName("a-contacts-members");
		Label membersCaption = VaadinUtils.createLabel("Membros");
		members.addComponent(membersCaption); membersCaption.addStyleName("a-contacts-members-caption");
		members.addComponent(newMember); newMember.addStyleName("a-contacts-members-new");
		members.addComponent(memberList); memberList.addStyleName("a-contacts-members-list");

		contactsView.addComponent(groups);
		contactsView.addComponent(members);
		
		newGroup.setInputPrompt("Novo Grupo");
		newMember.setInputPrompt("Adicionar Membro");
	}

	@Override
	public void setMembersToChoose(List<String> membersToChoose) {
		newMember.setChoices(membersToChoose);
	}

	@Override
	public void addGroup(String groupName) {
		groupList.addElement(groupName);
	}
	@Override
	public void addMember(String memberName) {
		memberList.addElement(memberName);
	}
	@Override
	public void setGroupCreateListener(Consumer<String> consumer) {
		newGroupConsumer = consumer;
	}
	@Override
	public void setGroupSelected(String groupName) {
		groupList.selectElement(groupName);
	}
	@Override
	public void setGroupSelectionListener(Consumer<String> consumer) {
		groupList.setSelectionListener(consumer);
	}
	@Override
	public void setGroupRemoveListener(Consumer<String> consumer) {
		groupList.setRemoveListener(consumer);
	}
	@Override
	public void clearGroups() {
		groupList.removeAllElements();
	}
	@Override
	public void setMemberRemoveListener(Consumer<String> consumer) {
		memberList.setRemoveListener(consumer);
	}
	@Override
	public void clearMembers() {
		memberList.removeAllElements();
	}

}
