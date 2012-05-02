package agitter.ui.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vaadinutils.ProfileListItem;
import agitter.domain.contacts.Group;
import agitter.domain.events.Invitation;
import agitter.domain.users.User;

public class ContactChooserHelper {

	public static List<ProfileListItem> contacts(Invitation invitationTree) {
		return contacts(invitationTree, 0);
	}
	
	public static List<ProfileListItem> contacts(Invitation invitationTree, int level) {
		List<ProfileListItem> results = new ArrayList<ProfileListItem>();
		
		results.add(createProfile(invitationTree.host(), level));
		
		for (Invitation inv : invitationTree.invitees()) {
			results.addAll(contacts(inv, level+1));
		}
		
		return results;		
	}
	
	public static ProfileListItem createProfile(User obj) {
		return createProfile(obj, 0);
	}
	
	public static ProfileListItem createProfile(User obj, int level) {
		return new ProfileListItem(obj.email().toString(), obj.name(), obj.picture(), level);
	}
	
	public static List<ProfileListItem> contacts(Iterable<Group> groups, Iterable<User> contacts) {
		List<ProfileListItem> contactsAndGroups = new ArrayList<ProfileListItem>();
		
		for (Group g : groups) 
			contactsAndGroups.add(new ProfileListItem(g.toString()));	
			
		for (User obj : contacts)
			contactsAndGroups.add(createProfile(obj));
		
		Collections.sort(contactsAndGroups, new ItemComparator());
		
		return contactsAndGroups;
	}
	
	public static List<ProfileListItem> contacts(Iterable<User> contacts) {
		return contacts(new ArrayList<Group>(), contacts);
	}
	
	public static List<ProfileListItem> contacts(User[] invitees) {
		return contacts(new Group[0], invitees);
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
