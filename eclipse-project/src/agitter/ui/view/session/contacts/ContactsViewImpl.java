package agitter.ui.view.session.contacts;


import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.ui.*;
import sneer.foundation.lang.Consumer;

public class ContactsViewImpl implements ContactsView {

	private final ComponentContainer container;
	private final RemovableElementList groupList = new RemovableElementList();
	private final RemovableElementList memberList = new RemovableElementList();

	private final TextField newGroup = new TextField("Novo Grupo");
	private final NativeButton addGroup = AgitterVaadinUtils.createDefaultNativeButton("+");

	private final TextField newMember = new TextField("Adicionar Membro");
	private final NativeButton addMember = AgitterVaadinUtils.createDefaultNativeButton("+");

	private Button.ClickListener addGroupListener = new Button.ClickListener() { @Override public void buttonClick(Button.ClickEvent clickEvent) {
		groupList.addElement((String) newGroup.getValue());
	}};
	private Button.ClickListener addMemberListener = new Button.ClickListener() { @Override public void buttonClick(Button.ClickEvent clickEvent) {
		memberList.addElement((String) newMember.getValue());
	}};

	public ContactsViewImpl(ComponentContainer container) {
		this.container = container;
		addGroup.addListener(addGroupListener);
		addMember.addListener(addMemberListener);
	}

	@Override
	public void show() {
		container.removeAllComponents();

		Layout groups = new VerticalLayout();
		groups.addComponent(newGroup);
		groups.addComponent(addGroup);
		groups.addComponent(groupList);
		
		Layout members = new VerticalLayout();
		members.addComponent(newMember);
		members.addComponent(addMember);
		members.addComponent(memberList);

		HorizontalLayout layout = new HorizontalLayout();
		layout.addComponent(groups);
		layout.addComponent(members);
		container.addComponent(layout);
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
	public void setGroupSelectionListener(Consumer<String> consumer) {
		groupList.setSelectionListener(consumer);
	}
	@Override
	public void setGroupRemoveListener(Consumer<String> consumer) {
		groupList.setRemoveListener(consumer);
	}
	@Override
	public void setMemberRemoveListener(Consumer<String> consumer) {
		memberList.setRemoveListener(consumer);
	}

}
