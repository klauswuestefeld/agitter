package agitter.ui.helper;

import java.util.ArrayList;
import java.util.List;

import vaadinutils.AutoCompleteChooser;
import vaadinutils.AutoCompleteChooser.AutoCompleteItem;
import agitter.domain.contacts.Group;
import agitter.domain.users.User;

public class ContactChooserHelper {
	public static List<AutoCompleteChooser.AutoCompleteItem> contacts(List<Group> groups, List<User> contacts) {
		List<AutoCompleteItem> contactsAndGroups = new ArrayList<AutoCompleteChooser.AutoCompleteItem>(groups.size()+contacts.size());
		
		for (Group g : groups) 
			contactsAndGroups.add(new AutoCompleteItem(g.toString(), null, null));	
			
		for (User obj : contacts)
			contactsAndGroups.add(new AutoCompleteItem(obj.email().toString(), obj.name(), obj.picture()));
		
		return contactsAndGroups;
	}
	
}
