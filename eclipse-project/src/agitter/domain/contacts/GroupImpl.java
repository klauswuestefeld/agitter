package agitter.domain.contacts;

import java.util.ArrayList;
import java.util.List;

import agitter.domain.emails.EmailAddress;

@Deprecated
public class GroupImpl {
	
	String name;
	List<EmailAddress> members = new ArrayList<EmailAddress>();
	List<Group> subgroups = new ArrayList<Group>();
	
}
