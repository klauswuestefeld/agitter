package agitter.ui.view.session.contacts;


import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import vaadinutils.AutoCompleteChooser;
import vaadinutils.WidgetUtils;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

public class ContactsViewImpl implements ContactsView {

	private static final String ALL = "Todos";

	private final ComponentContainer container;
	private final ComponentContainer fixedContainer;

	private final SelectableRemovableElementList groupList = new SelectableRemovableElementList();
	private final SelectableRemovableElementList memberList = new SelectableRemovableElementList();

	private final TextField newGroup = new TextField();
	private final ValueChangeListener newGroupListener;
	/** Clicking on this button has no effect. Lost focus from the setImmediate(true) combo-box will already trigger the event. */
	private final NativeButton ignored = AgitterVaadinUtils.createDefaultAddButton();
	private Consumer<String> newGroupConsumer;

	private final AutoCompleteChooser newMember = new AutoCompleteChooser();



	public ContactsViewImpl(ComponentContainer container, ComponentContainer fixedContainer) {
		this.container = container;
		this.fixedContainer = fixedContainer;
		newGroup.setImmediate(true);
		newGroupListener = new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
			String value = (String) newGroup.getValue();
			if (value.isEmpty()) return;
			newGroupConsumer.consume(value);
		}};
		newGroup.addListener(newGroupListener);
	}

	@Override
	public void show() {
		container.removeAllComponents();
		fixedContainer.removeAllComponents();

		CssLayout contactsView = new CssLayout();
		container.addComponent(contactsView); contactsView.addStyleName("a-contacts-view");

		CssLayout groups = new CssLayout(); groups.addStyleName("a-contacts-groups");
		Label groupsCaption = WidgetUtils.createLabel("Grupos");
		groups.addComponent(groupsCaption); groupsCaption.addStyleName("a-contacts-groups-caption");
		groups.addComponent(newGroup); newGroup.addStyleName("a-contacts-groups-new");
		groups.addComponent(ignored); ignored.addStyleName("a-contacts-groups-new-add");
		groups.addComponent(groupList); groupList.addStyleName("a-contacts-groups-list");
		
		CssLayout members = new CssLayout(); members.addStyleName("a-contacts-members");
		Label membersCaption = WidgetUtils.createLabel("Membros");
		members.addComponent(membersCaption); membersCaption.addStyleName("a-contacts-members-caption");
		members.addComponent(newMember); newMember.addStyleName("a-contacts-members-new");
		newMember.setInputWidth("300px");
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
	public void setGroups(List<String> groupNames) {
		groupList.removeAllElements();
		groupList.addElementUnremovable(ALL);
		groupList.addElements(groupNames);
	}
	
	@Override
	public void setGroupCreateListener(Consumer<String> consumer) {
		newGroupConsumer = consumer;
	}
	@Override
	public void setGroupSelected(String groupName) {
		if (groupName == null)
			groupList.highlightElement(ALL);
		else
			groupList.highlightElement(groupName);
	}
	@Override
	public void setGroupSelectionListener(final Consumer<String> consumer) {
		groupList.setSelectionListener(new Consumer<String>() { @Override public void consume(String value) {
			consumer.consume(value.equals(ALL) ? null : value);
		}});
	}
	@Override
	public void setGroupRemoveListener(Consumer<String> consumer) {
		groupList.setRemoveListener(consumer);
	}

	@Override
	public void setMemberEntryListener(Predicate<String> listener) {
		newMember.setListener(listener);
	}
	
	@Override
	public void setMemberRemoveListener(Consumer<String> consumer) {
		memberList.setRemoveListener(consumer);
	}

	@Override
	public void setMembers(List<String> memberNames) {
		memberList.removeAllElements();
		memberList.addElements(memberNames);
	}

	@Override
	public void clearGroupCreateField() {
		newGroup.setValue("");
	}

}
