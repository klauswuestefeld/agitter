package agitter.ui.view.session.contacts;

import java.util.List;

import sneer.foundation.lang.Consumer;

public interface ContactsView {

	void show();

	void addGroup(String groupName);
	void setGroupCreateListener(Consumer<String> consumer);
	void setGroupSelected(String groupName);
	void setGroupSelectionListener(Consumer<String> consumer);
	void setGroupRemoveListener(Consumer<String> consumer);
	void clearGroups();

	void setMembersToChoose(List<String> membersToChoose);
	void addMember(String memberName);
	void setMemberRemoveListener(Consumer<String> consumer);
	void clearMembers();
}
