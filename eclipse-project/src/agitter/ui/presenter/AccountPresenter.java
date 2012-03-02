package agitter.ui.presenter;

import sneer.foundation.lang.Consumer;
import agitter.domain.users.User;
import agitter.ui.view.session.account.AccountView;

public class AccountPresenter {

	private final User loggedUser;
	private final AccountView view;
	
	public AccountPresenter(final User loggedUser, AccountView view) {
		this.loggedUser = loggedUser;
		this.view = view;
		view.setNameListener(new Consumer<String>() { @Override public void consume(String value) {
			loggedUser.setName(value);
		}});
			
		refresh();
	}

	public void refresh() {
		view.setUser(loggedUser);
	}

	

}

