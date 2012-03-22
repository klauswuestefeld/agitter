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
		
		view.onTwitterLink(new Runnable() { @Override public void run() {
			twitterLinkAttempt(); 
		}});
		view.onGoogleLink(new Runnable() { @Override public void run() {
			googleLinkAttempt(); 
		}});
		view.onWindowsLink(new Runnable() { @Override public void run() {
			windowsLinkAttempt(); 
		}});
		view.onYahooLink(new Runnable() { @Override public void run() {
			yahooLinkAttempt(); 
		}});
		view.onFacebookLink(new Runnable() { @Override public void run() {
			facebookLinkAttempt(); 
		}});
		view.onTwitterUnlink(new Runnable() { @Override public void run() {
			twitterUnlinkAttempt(); 
		}});
		view.onGoogleUnlink(new Runnable() { @Override public void run() {
			googleUnlinkAttempt(); 
		}});
		view.onWindowsUnlink(new Runnable() { @Override public void run() {
			windowsUnlinkAttempt(); 
		}});
		view.onYahooUnlink(new Runnable() { @Override public void run() {
			yahooUnlinkAttempt(); 
		}});
		view.onFacebookUnlink(new Runnable() { @Override public void run() {
			facebookUnlinkAttempt(); 
		}});

		refresh();
	}

	protected void facebookUnlinkAttempt() {
		loggedUser.unlinkAccount(OAuth.FACEBOOK);
		refresh();
	}

	protected void yahooUnlinkAttempt() {
		loggedUser.unlinkAccount(OAuth.YAHOO);
		refresh();
	}

	protected void windowsUnlinkAttempt() {
		loggedUser.unlinkAccount(OAuth.HOTMAIL);
		refresh();
	}

	protected void googleUnlinkAttempt() {
		loggedUser.unlinkAccount(OAuth.GOOGLE);
		refresh();
	}

	protected void twitterUnlinkAttempt() {
		loggedUser.unlinkAccount(OAuth.TWITTER);
		refresh();
	}

	private void onOptionSelected(String value) {
		view.setOptionSelected(value);
	}
	
	public void refresh() {
		view.setUser(loggedUser);
	}

	private void googleLinkAttempt() {
		try{
			String url = oAuth.googleLinkURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o Google.");
		}
	}
	
	
	private void windowsLinkAttempt() {
		try{
			String url = oAuth.windowsLinkURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o WindowsLive.");
		}
	}
	
	
	private void yahooLinkAttempt() {
		try{
			String url = oAuth.yahooLinkURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o Yahoo.");
		}
	}
	
	
	private void facebookLinkAttempt() {
		try{
			String url = oAuth.facebookLinkURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o Facebook.");
		}
	}
	
	private void twitterLinkAttempt() {
		try{
			String url = oAuth.twitterLinkURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o Twitter.");
		}
	}
}

