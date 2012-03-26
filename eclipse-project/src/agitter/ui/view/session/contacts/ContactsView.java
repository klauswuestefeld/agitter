package agitter.ui.view.session.contacts;

import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Pair;
import sneer.foundation.lang.Predicate;
import vaadinutils.AutoCompleteChooser.AutoCompleteItem;

public interface ContactsView {

	void show();

	void setGroups(List<String> groupNames);
	void setGroupCreateListener(Consumer<String> consumer);
	void setGroupSelected(String groupName);
	void setGroupSelectionListener(Consumer<String> consumer);
	void setGroupRemoveListener(Consumer<String> consumer);
	void clearGroupCreateField();

	void setMemberEntryListener(Predicate<String> listener);
	void setMemberRemoveListener(Consumer<String> consumer);

	void setMembers(List<Pair<String, String>> memberNames);
	void setMembersToChoose(List<AutoCompleteItem> memberPairs);

}
