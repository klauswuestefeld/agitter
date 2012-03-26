package agitter.ui.helper;

import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.Pair;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class ContactChooserHelper {
	public static List<Pair<String,String>> contacts(List<Group> groups, List<User> contacts) {
		List<Pair<String,String>> contactsAndGroups = new ArrayList<Pair<String,String>>(groups.size()+contacts.size());
		
		for (Group g : groups) 
			contactsAndGroups.add(new Pair<String,String>(g.toString(), null));
		
			
		for (User obj : contacts)
			contactsAndGroups.add(new Pair<String,String>(obj.email().toString(), obj.name()));
		
		return contactsAndGroups;
	}
	
}
