package agitter.ui.view.session.account;

import sneer.foundation.lang.Consumer;
import agitter.common.Portal;
import agitter.domain.users.User;

public interface AccountView {
	
	interface Boss {
		void onPasswordChange(String currentPassword, String newPassword);
	}

	void startReportingTo(Boss b);
	
	void show();

	void setUser(User user);
	void setOptionSelected(String optionName);
	void setNameListener(Consumer<String> consumer);
	void clearPasswordFields();

	void setOptionSelectionListener(Consumer<String> consumer);
	
	void onLink(Consumer<Portal> action);
	void onUnlink(Consumer<Portal> action);

}
