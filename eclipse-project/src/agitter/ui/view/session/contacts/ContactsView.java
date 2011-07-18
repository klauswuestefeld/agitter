package agitter.ui.view.session.contacts;

import sneer.foundation.lang.Consumer;

public interface ContactsView {

	void show();

	void addGroup(String groupName);
	void addMember(String memberName);

	void setGroupSelectionListener(Consumer<String> consumer);
	void setGroupRemoveListener(Consumer<String> consumer);
	void setMemberRemoveListener(Consumer<String> consumer);
}
