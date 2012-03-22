package agitter.ui.view.session.account;

import sneer.foundation.lang.Consumer;
import agitter.domain.users.User;

public interface AccountView {

	void show();

	void setUser(User user);
	void setOptionSelected(String optionName);
	void setNameListener(Consumer<String> consumer);

	void setOptionSelectionListener(Consumer<String> consumer);
	

	void onGoogleLink(Runnable action);
	void onWindowsLink(Runnable action);
	void onYahooLink(Runnable action);
	void onFacebookLink(Runnable action);
	void onTwitterLink(Runnable action);

	void onGoogleUnlink(Runnable action);
	void onWindowsUnlink(Runnable action);
	void onYahooUnlink(Runnable action);
	void onFacebookUnlink(Runnable action);
	void onTwitterUnlink(Runnable action);
}
