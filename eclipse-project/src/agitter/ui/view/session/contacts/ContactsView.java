package agitter.ui.view.session.contacts;

import java.util.List;

import basis.lang.Consumer;
import basis.lang.Predicate;

import vaadinutils.ProfileListItem;
import agitter.common.Portal;

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

	void setMembers(List<ProfileListItem> memberNames);
	void setMembersToChoose(List<ProfileListItem> memberPairs);

	void setUpdateFriendsListener(Consumer<Portal> listener);

}
