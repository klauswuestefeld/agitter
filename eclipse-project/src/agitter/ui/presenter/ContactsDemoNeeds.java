package agitter.ui.presenter;

import agitter.ui.view.session.SessionView.Needs;


class ContactsDemoNeeds implements Needs {

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

}
