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
		
		view.onTwitterSignin(new Runnable() { @Override public void run() {
			twitterSigninAttempt(); 
		}});
		view.onGoogleSignin(new Runnable() { @Override public void run() {
			googleSigninAttempt(); 
		}});
		view.onWindowsSignin(new Runnable() { @Override public void run() {
			windowsSigninAttempt(); 
		}});
		view.onYahooSignin(new Runnable() { @Override public void run() {
			yahooSigninAttempt(); 
		}});
		view.onFacebookSignin(new Runnable() { @Override public void run() {
			facebookSigninAttempt(); 
		}});

		refresh();
	}

	private void onOptionSelected(String value) {
		view.setOptionSelected(value);
	}
	
	public void refresh() {
		view.setUser(loggedUser);
	}


	
	private void googleSigninAttempt() {
		try{
			String url = oAuth.googleSigninURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o Google.");
		}
	}
	
	
	private void windowsSigninAttempt() {
		try{
			String url = oAuth.windowsSigninURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o WindowsLive.");
		}
	}
	
	
	private void yahooSigninAttempt() {
		try{
			String url = oAuth.yahooSigninURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o Yahoo.");
		}
	}
	
	
	private void facebookSigninAttempt() {
		try{
			String url = oAuth.facebookSigninURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o Facebook.");
		}
	}
	
	private void twitterSigninAttempt() {
		try{
			String url = oAuth.twitterSigninURL(context, httpSession);
			urlRedirector.consume(url);
		} catch (Exception e) {
			warningDisplayer.consume("Erro ao acessar o Twitter.");
		}
	}
}

