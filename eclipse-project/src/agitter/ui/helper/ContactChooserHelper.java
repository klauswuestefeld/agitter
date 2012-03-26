package agitter.ui.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vaadinutils.AutoCompleteChooser.FullFeaturedItem;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class ContactChooserHelper {
	public static List<FullFeaturedItem> contacts(Iterable<Group> groups, Iterable<User> contacts) {
		List<FullFeaturedItem> contactsAndGroups = new ArrayList<FullFeaturedItem>();
		
		for (Group g : groups) 
			contactsAndGroups.add(new FullFeaturedItem(g.toString(), null, null));	
			
		for (User obj : contacts)
			contactsAndGroups.add(new FullFeaturedItem(obj.email().toString(), obj.name(), obj.picture()));
		
		Collections.sort(contactsAndGroups, new ItemComparator());
		
		return contactsAndGroups;
	}

	public static List<FullFeaturedItem> contacts(Group[] groupInvitees,
			User[] invitees) {
		return contacts(Arrays.asList(groupInvitees),Arrays.asList(invitees));
	}
	
	static class ItemComparator implements Comparator<FullFeaturedItem> {
		@Override
		public int compare(FullFeaturedItem o1, FullFeaturedItem o2) {
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
