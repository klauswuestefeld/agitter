package agitter.main;

import static agitter.controller.Controller.CONTROLLER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vaadinutils.RestUtils;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.AgitterViewImpl;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Label;

public class AgitterSession extends Application implements HttpServletRequestListener  {

	private HttpServletRequest firstRequest;
	private HttpServletResponse firstResponse;
	private Presenter presenter;
	
	
	@Override
	public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {
		if (presenter == null) {
			firstRequest = request;
			firstResponse = response;
		} else
			presenter.setCurrentResponse(response);
	}


	@Override
	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {
		if (presenter != null) //Exception during init can leave it null.
			presenter.setCurrentResponse(null);
	}


	@Override
	public void init() {
		setTheme("agitter");
		AgitterViewImpl view = new AgitterViewImpl();
		setMainWindow(view);
		if (!browserCompatibilityCheck()) return;

		presenter = new Presenter(CONTROLLER, view, firstRequest, firstResponse);
		firstRequest = null;
		firstResponse = null;
		
		RestUtils.addRestHandler(view, presenter);
	}
	
	/*
	@Override
	public Window getWindow(String name) {
		System.err.println( "getWindow: " + name );
		Window window = super.getWindow(name);
		System.err.println( "getWindow.super.window: " + window );
		if(window != null)
			return window;
		
		window = new AgitterViewImpl();

		// Use the random name given by the framework to identify this
		// window in future
		window.setName(name);
		addWindow(window);

		// Move to the url to remember the name in the future
		window.open(new ExternalResource(window.getURL()));

		presenter = new Presenter(CONTROLLER, (AgitterView) window, firstRequest, firstResponse);
//		firstRequest = null;
//		firstResponse = null;
		
		RestUtils.addRestHandler(window, presenter);

		
		return null;
		
	}
	*/

	
	private boolean browserCompatibilityCheck() {
		WebBrowser browser = ((WebApplicationContext)getContext()).getBrowser();
		if (!browser.isIE()) return true;
		int version = browser.getBrowserMajorVersion();
		if (version >= 9) return true;
		getMainWindow().addComponent(new Label("A versão deste browser que você está usando (Internet Explorer " + version + ") é antiga demais."));
		getMainWindow().addComponent(new Label("Use a versão 9 pra cima ou use outro browser como o Firefox: http://www.mozilla.org/pt-BR/firefox"));
		return false;			  
	}


	public static SystemMessages getSystemMessages() {
		CustomizedSystemMessages messages = new CustomizedSystemMessages();
		messages.setOutOfSyncNotificationEnabled(false);
		messages.setSessionExpiredNotificationEnabled(false);
		return messages;
	}

}
