package agitter.main;

import static agitter.controller.Controller.CONTROLLER;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vaadinutils.RestUtils;
import agitter.ui.presenter.Presenter;
import agitter.ui.view.AgitterViewImpl;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;

public class AgitterVaadinApplication extends Application implements HttpServletRequestListener  {

	private HttpServletRequest firstRequest;
	private HttpServletResponse firstResponse;
	private Presenter presenter;
	
	public static SystemMessages getSystemMessages() {
		final CustomizedSystemMessages messages = new CustomizedSystemMessages();
		messages.setSessionExpiredNotificationEnabled(false);
		return messages;
	}
	
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

		presenter = new Presenter(CONTROLLER, view, firstRequest, firstResponse);
		firstRequest = null;
		
		RestUtils.addRestHandler(view, presenter);
	}
	
}
