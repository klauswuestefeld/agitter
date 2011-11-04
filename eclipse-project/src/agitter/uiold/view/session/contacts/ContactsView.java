package agitter.uiold.view.session.contacts;

import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

public interface ContactsView {

	void show();

	void setGroups(List<String> groupNames);
	void setGroupCreateListener(Consumer<String> consumer);
	void setGroupSelected(String groupName);
	void setGroupSelectionListener(Consumer<String> consumer);
	void setGroupRemoveListener(Consumer<String> consumer);
	void clearGroupCreateField();

	void setMembersToChoose(List<String> membersToChoose);
	void setMembers(List<String> memberNames);
	void setMemberEntryListener(Predicate<String> valueValidator, Consumer<String> consumer);
	void setMemberRemoveListener(Consumer<String> consumer);

}
