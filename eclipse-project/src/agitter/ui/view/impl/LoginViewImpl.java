package agitter.ui.view.impl;

import agitter.ui.view.LoginView;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;

public class LoginViewImpl implements LoginView {

	private final VerticalLayout container;
	private final TextField emailOrUsername = new TextField("Email ou Username");
	private final PasswordField password = new PasswordField("Senha");
	private final Button login = new NativeButton("Agitar!");
//	private final Button forgotMyPassword = forgotMyPasswordButton();

	LoginViewImpl(VerticalLayout container) {
		this.container = container;
	}

	@SuppressWarnings("unused")
	private Button forgotMyPasswordButton() {
        Button b = new Button("Esqueci");
        b.setStyleName(BaseTheme.BUTTON_LINK);
        return b;
	}

	@Override
	public void show() {
		container.removeAllComponents();
		GridLayout loginGrid = new GridLayout(3,2);
		loginGrid.addComponent(emailOrUsername, 0, 0);
		loginGrid.addComponent(password, 1, 0);
		loginGrid.addComponent(login, 2, 0);
		loginGrid.setComponentAlignment(login, Alignment.BOTTOM_CENTER);
//		loginGrid.addComponent(forgotMyPassword, 1, 1);
		loginGrid.setSpacing(true);
		container.addComponent(loginGrid);
		container.setComponentAlignment(loginGrid, Alignment.MIDDLE_RIGHT);

	}

	@Override
	public String emailOrUsername() {
		return (String)emailOrUsername.getValue();
	}

	@Override
	public String password() {
		return (String)password.getValue(); 
	}

	@Override
	public void onLoginAttempt(final Runnable runnable) {
		login.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			runnable.run();
		}});
	}

}
