package agitter.ui.view.session.contacts;


import java.util.List;

import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.ui.*;
import sneer.foundation.lang.Consumer;
import vaadinutils.AutoCompleteChooser;

public class ContactsViewImpl implements ContactsView {

	private final ComponentContainer container;
	private final RemovableElementList groupList = new RemovableElementList();
	private final RemovableElementList memberList = new RemovableElementList();

	private final TextField newGroup = new TextField("Novo Grupo");
	private final NativeButton addGroupButton = AgitterVaadinUtils.createDefaultNativeButton("+");
	private Consumer<String> newGroupConsumer;

	private final AutoCompleteChooser newMember = new AutoCompleteChooser("Adicionar Membro");

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

		Layout groups = new VerticalLayout();
		groups.addComponent(newGroup);
		groups.addComponent(addGroupButton);
		groups.addComponent(groupList);
		
		Layout members = new VerticalLayout();
		members.addComponent(newMember);
		members.addComponent(memberList);

		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(groups);
		layout.addComponent(members);
		container.addComponent(layout);
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
