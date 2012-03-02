package agitter.ui.view.session.account;

import sneer.foundation.lang.Consumer;
import agitter.domain.users.User;

public interface AccountView {

	void show();

	void setUser(User user);
	void setOptionSelected(String optionName);
	void setNameListener(Consumer<String> consumer);

}
