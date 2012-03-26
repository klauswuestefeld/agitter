package agitter.ui.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vaadinutils.AutoCompleteChooser.AutoCompleteItem;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class ContactChooserHelper {
	public static List<AutoCompleteItem> contacts(Iterable<Group> groups, Iterable<User> contacts) {
		List<AutoCompleteItem> contactsAndGroups = new ArrayList<AutoCompleteItem>();
		
		for (Group g : groups) 
			contactsAndGroups.add(new AutoCompleteItem(g.toString(), null, null));	
			
		for (User obj : contacts)
			contactsAndGroups.add(new AutoCompleteItem(obj.email().toString(), obj.name(), obj.picture()));
		
		Collections.sort(contactsAndGroups, new ItemComparator());
		
		return contactsAndGroups;
	}

	public static List<AutoCompleteItem> contacts(Group[] groupInvitees,
			User[] invitees) {
		return contacts(Arrays.asList(groupInvitees),Arrays.asList(invitees));
	}
	
	static class ItemComparator implements Comparator<AutoCompleteItem> {
		@Override
		public int compare(AutoCompleteItem o1, AutoCompleteItem o2) {
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
