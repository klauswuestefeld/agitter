package agitter.ui.presenter.hacks;

import agitter.ui.view.session.SessionView.Needs;


public class ContactsDemoNeeds implements Needs {

	public ContactsDemoNeeds() {
		
	}
	
	@Override
	public String userScreenName() {
		return "fulano@example.com";
	}

	@Override
	public void onLogout() {
		System.out.println("Logout");
	}

	@Override
	public void onEventsMenu() {
		System.out.println("Events Menu");
	}

	@Override
	public void onContactsMenu() {
		System.out.println("Contacts Menu");
	}

	@Override
	public void onAccountMenu() {
		System.out.println("Account Menu");
	}

}
