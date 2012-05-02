package agitter.ui.presenter.hacks;

import agitter.ui.view.session.SessionView.Boss;


public class ContactsDemoBoss implements Boss {

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
