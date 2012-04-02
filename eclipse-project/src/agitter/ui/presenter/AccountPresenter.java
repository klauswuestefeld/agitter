package agitter.ui.presenter;

import javax.servlet.http.HttpSession;

import sneer.foundation.lang.Consumer;
import agitter.controller.oauth.OAuth;
import agitter.domain.users.User;
import agitter.ui.view.session.account.AccountView;

public class AccountPresenter {

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
		view.setNameListener(new Consumer<String>() { @Override public void consume(String value) {
			loggedUser.setName(value);
		}});
		
		view.setOptionSelectionListener(new Consumer<String>() { @Override public void consume(String value) {
			onOptionSelected(value);
		}});
		
		view.onUnlink(new Consumer<String>() { @Override public void consume(String value) {
			unlinkAttempt(value); 
		}});
			
		view.onLink(new Consumer<String>() { @Override public void consume(String value) {
			linkAttempt(value); 
		}});

		refresh();
	}

	private void onOptionSelected(String value) {
		view.setOptionSelected(value);
	}
	
	public void refresh() {
		view.setUser(loggedUser);
	}

	public void linkAttempt(String network) {
		onUpdateFriends(network);
	}
	
	public void onUpdateFriends(String network) {
		try{
			String url = oAuth.linkURL(context, httpSession, network);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar a rede " + network);
		}
	}
	
	public void unlinkAttempt(String network) {
		loggedUser.unlinkAccount(network);
		refresh();
	}
}

