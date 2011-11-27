package agitter.main;

import static agitter.controller.Controller.CONTROLLER;

import java.net.URL;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vaadinutils.RestUtils;
import vaadinutils.RestUtils.RestHandler;
import vaadinutils.SessionUtils;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.AgitterViewImpl;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;

public class AgitterVaadinApplication extends Application implements HttpServletRequestListener  {

	private Presenter presenter;
	private String autehnticationToken;
	
	@Override
	public void init() {
		setTheme("agitter");
		final AgitterViewImpl view = new AgitterViewImpl();
		setMainWindow(view);

		SessionUtils.handleForMainWindow(view);

		RestUtils.addRestHandler(view, new RestHandler() { @Override public void onRestInvocation(URL context, String relativeUri, Map<String, String[]> params) {
			if (presenter == null) {
				presenter = new Presenter(CONTROLLER, view, SessionUtils.getHttpSession(view), context, autehnticationToken);
				autehnticationToken = null;
			}
			presenter.onRestInvocation(context, relativeUri, params);
		}});
	}
	
	@Override
	public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
		if( presenter == null ) {
			searchAuthenticationTokenIn( request.getCookies() );
		} else {
			presenter.updateCurrentResponse( response );
		}
	}
	
	@Override
	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
	}
	
	
	private void searchAuthenticationTokenIn(Cookie[] cookies) {
		for(Cookie c : cookies) {
			if( Presenter.AUTHENTICATION_TOKEN_NAME.equals( c.getName() ) ) {
				autehnticationToken = c.getValue();
			}
		}
	}
	
}
