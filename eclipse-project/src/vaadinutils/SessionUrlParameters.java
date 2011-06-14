package vaadinutils;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vaadin.terminal.ParameterHandler;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

public class SessionUrlParameters implements ParameterHandler {
	
	private static final String KEY = SessionUrlParameters.class.getSimpleName();

	private final HttpSession session;

	
	public static void handleForMainWindow(Window mainWindow) {
		new SessionUrlParameters(mainWindow);
	}
	private SessionUrlParameters(Window mainWindow) {
		session = getHttpSession(mainWindow);
		mainWindow.addParameterHandler(this);
	}


	@Override public void handleParameters(Map<String, String[]> parameters) {
		session.setAttribute(KEY, parameters);
	}

	
	static public boolean isParameterSet(Component component, String param) {
		return getParameter(component, param) != null;
	}

	
	static public String getParameter(Component component, String param) {
		String[] values = paramsFor(component).get(param);
		return values == null ? null : values[0];
	}
	
	
	private static Map<String, String[]> paramsFor(Component component) {
		return (Map<String, String[]>)getHttpSession(component).getAttribute(KEY);
	}

	
	static private HttpSession getHttpSession(Component component) {
		return ((WebApplicationContext)component.getApplication().getContext()).getHttpSession();
	}
	
}
