package guardachuva.agitos.client;

import guardachuva.agitos.client.resources.events.EventsPresenter;
import guardachuva.agitos.client.resources.events.EventsWidget;
import guardachuva.agitos.client.resources.users.LoginPresenter;
import guardachuva.agitos.client.resources.users.SignupPresenter;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class ClientController implements IController, EntryPoint,
		ValueChangeHandler<String> {

	private final EventsPresenter _eventsPresenter;
	@SuppressWarnings("unused")
	private final LoginPresenter _loginPresenter;

	public ClientController() {
		_eventsPresenter = new EventsPresenter(this);
		_loginPresenter = new LoginPresenter(this);
	}

	public void onModuleLoad() {
		History.addValueChangeHandler(this);
		
		if (Window.Location.getPath().equals("/login.html")) {
			if (isLogged()) {				
				redirect("/");
			} else {
				new LoginPresenter(this).wrap();	
				AnalyticsTracker.track("login");			
			}
		}
		
		if (Window.Location.getPath().equals("/signup.html")) {
			if (isLogged()) {
				redirect("/");	
			} else {
				new SignupPresenter(this).wrap();
				AnalyticsTracker.track("signup");
			}
		}
		
		if (Window.Location.getPath().equals("/") || Window.Location.getPath().equals("/index.html")) {
			if (!isLogged())
				redirect("/login.html");
			else {
				showByToken(EventsWidget.token);
				History.fireCurrentHistoryState();
				AnalyticsTracker.track("app_loaded");
			}
		}
			
		if(RootPanel.get("signupLink")!=null){			
			UrlBuilder urlBuilder = createUrlBuilder("signup.html");
			HTML link = new HTML("<a href='" + urlBuilder.buildString() + "'>Cadastrar</a>");
			RootPanel.get("signupLink").add(link);		
		}	
		
		if(RootPanel.get("loginLink")!=null){			
			UrlBuilder urlBuilder = createUrlBuilder("login.html");
			HTML link = new HTML("<a href='" + urlBuilder.buildString() + "'>Acesse</a>");
			RootPanel.get("loginLink").add(link);		
		}	
	}

	private boolean isLogged() {
		return (Cookies.getCookie("userName") != null &&
				Cookies.getCookie("password") != null);
	}
	
	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		RootPanel.get("mainContainer").clear();
		RootPanel.get("mainContainer").add(_eventsPresenter.loadDataAndShowEventsWidget());
		
		AnalyticsTracker.track(History.getToken());
	}
	
	@Override
	public void setLoggedUser(String userName, String password) {
		Cookies.setCookie("userName", userName, null, null, "/", false);
		Cookies.setCookie("password", password, null, null, "/", false);
	}
	
	@Override
	public void logout() {
		Cookies.removeCookie("userName", "/");
		Cookies.removeCookie("password", "/");
		redirect("/login.html");
	}

	@Override
	public String getUserMail() {
		return Cookies.getCookie("userName");
	}
	
	@Override
	public void redirect(String path) {			
		UrlBuilder urlBuilder = createUrlBuilder(path);		
		Window.Location.assign(urlBuilder.buildString());		
	}

	private UrlBuilder createUrlBuilder(String path) {
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		urlBuilder.setPath(path);
		
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
}
