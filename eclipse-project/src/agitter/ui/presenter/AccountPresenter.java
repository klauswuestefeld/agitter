package agitter.ui.presenter;

import javax.servlet.http.HttpSession;

import basis.lang.Consumer;
import basis.lang.exceptions.Refusal;

import agitter.common.Portal;
import agitter.controller.oauth.OAuth;
import agitter.domain.users.User;
import agitter.ui.view.session.account.AccountView;

public class AccountPresenter implements AccountView.Boss {

	private final User loggedUser;
	private final AccountView view;
	
	private final OAuth oAuth;
	private final Consumer<String> urlRedirector;
	private final Consumer<String> warningDisplayer;
	private final HttpSession httpSession;
	private final String context;
	
	public AccountPresenter(final User loggedUser, AccountView view, OAuth oAuth, Consumer<String> warningDisplayer, HttpSession httpSession, String context, Consumer<String> urlRedirector) {
		this.loggedUser = loggedUser;
		this.view = view;
		this.oAuth = oAuth;
		this.urlRedirector = urlRedirector;
		this.warningDisplayer = warningDisplayer;
		this.httpSession = httpSession;
		this.context = context;
			
		view.startReportingTo(this);
		refresh();
	}

	@Override
	public void onOptionSelected(String value) {
		view.setOptionSelected(value);
	}
	
	public void refresh() {
		view.setUser(loggedUser);
	}

	public void onUpdateFriends(Portal portal) {
		try{
			String url = oAuth.linkURL(context, httpSession, portal);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar a rede " + portal);
		}
	}
	
	@Override
	public void onPasswordChange(String currentPassword, String newPassword) {
		try {
			loggedUser.attemptToSetPassword(currentPassword, newPassword);
		} catch (Refusal e) {
			warningDisplayer.consume(e.getMessage());
			return;
		}
		view.clearPasswordFields();
		warningDisplayer.consume("Senha alterada com sucesso.");
	}

	@Override
	public void onNameChange(String newName) {
		loggedUser.setName(newName);
	}
	
	@Override
	public void onLink(Portal portal) {
		onUpdateFriends(portal); 
	}
	
	@Override
	public void onUnlink(Portal portal) {
		loggedUser.unlinkAccount(portal);
		refresh();
	}
	
}

