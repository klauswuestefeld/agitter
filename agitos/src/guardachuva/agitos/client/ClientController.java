package guardachuva.agitos.client;

import guardachuva.agitos.client.resources.events.EventsPresenter;
import guardachuva.agitos.client.resources.events.EventsWidget;
import guardachuva.agitos.client.resources.users.LoginPresenter;
import guardachuva.agitos.client.resources.users.SignupPresenter;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.rpc.RemoteApplication;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class ClientController implements IController, EntryPoint,
		ValueChangeHandler<String> {

	private static final String AGITOS_BASE_URL = ""; //"/agitos";
	private final EventsPresenter _eventsPresenter;
	@SuppressWarnings("unused")
	private final LoginPresenter _loginPresenter;
	private RemoteApplicationAsync _application;
	protected SessionToken _session;

	public ClientController() {
		_application = (RemoteApplicationAsync) GWT.create(RemoteApplication.class);
		_eventsPresenter = new EventsPresenter(this, _application);
		_loginPresenter = new LoginPresenter(this, _application);
	}

	@Override
	public void onModuleLoad() {
		History.addValueChangeHandler(this);
		
		if (isAtLoginPage()) {
			if (isLogged()) {				
				redirectToRoot();
			} else {
				new LoginPresenter(this, _application).wrap();	
				AnalyticsTracker.track("login");			
			}
		}
		
		if (isAtSignupPage()) {
			if (isLogged()) {
				redirectToRoot();	
			} else {
				new SignupPresenter(this, _application).wrap();
				AnalyticsTracker.track("signup");
			}
		}
		
		if (isAtRootPage()) {
			if (!isLogged())
				redirectToLoginPage();
			else {
				showByToken(EventsWidget.token);
				History.fireCurrentHistoryState();
				AnalyticsTracker.track("app_loaded");
			}
		}
			
		if(RootPanel.get("signupLink")!=null){			
			UrlBuilder urlBuilder = createUrlBuilder("/signup.html");
			HTML link = new HTML("<a href='" + urlBuilder.buildString() + "'>Cadastrar</a>");
			RootPanel.get("signupLink").add(link);		
		}	
		
		if(RootPanel.get("loginLink")!=null){			
			UrlBuilder urlBuilder = createUrlBuilder("/login.html");
			HTML link = new HTML("<a href='" + urlBuilder.buildString() + "'>Acesse</a>");
			RootPanel.get("loginLink").add(link);		
		}	
	}

	public boolean isAtRootPage() {
		return Window.Location.getPath().equals(AGITOS_BASE_URL + "/") 
			|| Window.Location.getPath().equals(AGITOS_BASE_URL) 
			|| Window.Location.getPath().endsWith("/index.html");
	}

	public boolean isAtSignupPage() {
		return Window.Location.getPath().endsWith("/signup.html");
	}

	public boolean isAtLoginPage() {
		return Window.Location.getPath().endsWith("/login.html");
	}

	public void redirectToRoot() {
		redirect("/index.html");
	}

	public void redirectToLoginPage() {
		redirect("/login.html");
	}
	
	public void redirectToSignupPage() {
		redirect("/signup.html");
	}

	public boolean isLogged() {
		return (Cookies.getCookie(SessionToken.COOKIE_NAME) != null);
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		RootPanel.get("mainContainer").clear();
		RootPanel.get("mainContainer").add(_eventsPresenter.loadDataAndShowEventsWidget());
		
		AnalyticsTracker.track(History.getToken());
	}
	
	@Override
	public void logout() {
		_application.logout(_session, new AsyncCallback<Void>() {			
			@Override
			public void onSuccess(Void result) {
				clearSession();
				clearLoggedUserEmail();
				redirectToLoginPage();
			}
			@Override
			public void onFailure(Throwable caught) {
				clearSession();
				clearLoggedUserEmail();
				redirectToLoginPage();
			}
		});
	}

	private void redirect(String path) {			
		redirect(path, new Parameter[0]);		
	}

	private void redirect(String path, Parameter params[]) {			
		UrlBuilder urlBuilder = createUrlBuilder(path, params);		
		Window.Location.assign(urlBuilder.buildString());		
	}

	private UrlBuilder createUrlBuilder(String path) {
		return createUrlBuilder(path, new Parameter[0]);
	}
	
	private UrlBuilder createUrlBuilder(String path, Parameter params[]) {
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath(AGITOS_BASE_URL + path);
		
		for (Parameter parameter : params)
			urlBuilder.setParameter(parameter.getName(), parameter.getValue());

		String argName = "gwt.codesvr";
		String gwtCodesvr = Window.Location.getParameter(argName);
		if(gwtCodesvr != null && !gwtCodesvr.isEmpty()){
			urlBuilder.setParameter(argName, gwtCodesvr);
		}
		

		return urlBuilder;
	}	

	@Override
	public void showByToken(String token) {
		History.newItem(token, true);
	}

	@Override
	public void showMessage(String message) {
		Window.alert(message);
	}

	@Override
	public void showError(JSONValue jsonErrors) {
		JSONArray errorArray = jsonErrors.isArray();
		String errorString = "";
		for (int i = 0; i < errorArray.size(); i++)
			errorString  += errorArray.get(i).isString().stringValue() + "\n";
		showError("Verifique os erros: \n\n" + errorString);
	}

	@Override
	public void showError(String string) {
		Window.alert(string);
	}
	
	@Override
	public void showError(Throwable e) {
		showError(e.getMessage());
	}

	
	@Override
	public void setLoggedUserEmail(String email) {
		Cookies.setCookie("loggedUserEmail", email, null, null, "/", false);
	}
	
	@Override
	public String getLoggedUserEmail() {
		return Cookies.getCookie("loggedUserEmail");
	}
		
	protected void clearLoggedUserEmail() {
		Cookies.removeCookie("loggedUserEmail", "/");
	}

	protected void clearSession() {
		Cookies.removeCookie(SessionToken.COOKIE_NAME);
		_session = null;
	}

	@Override
	public void setSession(SessionToken session) {
		Cookies.setCookie(SessionToken.COOKIE_NAME, session.getToken());
		_session = session;
	}

	@Override
	public SessionToken getSession() {
		if(_session==null) {
			String token = Cookies.getCookie(SessionToken.COOKIE_NAME);
			if(token!=null && !token.isEmpty()) {
				_session = new SessionToken(token);
			}
		}
		return _session;
	}

	@Override
	public void redirectToSocialAuth(String providerName) {
		redirect("/agitosweb/social_auth", new Parameter[] { new Parameter("id", providerName) });
	}

}
