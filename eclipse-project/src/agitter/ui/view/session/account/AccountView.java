package agitter.ui.view.session.account;

import agitter.common.Portal;
import agitter.domain.users.User;

public interface AccountView {
	
	interface Boss {
		void onPasswordChange(String currentPassword, String newPassword);
		void onNameChange(String newName);
		void onOptionSelected(String option);
		void onLink(Portal portal);
		void onUnlink(Portal portal);
	}

	void startReportingTo(Boss b);
	
	void show();

	void setUser(User user);
	void setOptionSelected(String optionName);
	void clearPasswordFields();


}
