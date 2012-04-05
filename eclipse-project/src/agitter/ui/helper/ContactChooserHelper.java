package agitter.ui.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vaadinutils.ProfileListItem;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class ContactChooserHelper {
	public static List<ProfileListItem> contacts(Iterable<Group> groups, Iterable<User> contacts) {
		List<ProfileListItem> contactsAndGroups = new ArrayList<ProfileListItem>();
		
		for (Group g : groups) 
			contactsAndGroups.add(new ProfileListItem(g.toString()));	
			
		for (User obj : contacts)
			contactsAndGroups.add(new ProfileListItem(obj.email().toString(), obj.name(), obj.picture()));
		
		Collections.sort(contactsAndGroups, new ItemComparator());
		
		return contactsAndGroups;
	}

	public static List<ProfileListItem> contacts(Group[] groupInvitees,
			User[] invitees) {
		return contacts(Arrays.asList(groupInvitees),Arrays.asList(invitees));
	}
	
	static class ItemComparator implements Comparator<ProfileListItem> {
		@Override
		public int compare(ProfileListItem o1, ProfileListItem o2) {
			if (o1.caption == null && o2.caption != null) {
				return -1;
			}
			if (o1.caption != null && o2.caption == null) {
				return 1;
			}
			if (o1.caption == null && o2.caption == null) {
				return o1.key.compareTo(o2.key);
			}
			return o1.caption.compareTo(o2.caption);
		}
	}

}
