package vaadinutils;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Component;

public class SessionUtils {
	
	private static final String KEY = SessionUtils.class.getSimpleName();

	
	public static void initParameters(HttpSession session, Map<String, String[]> parameters) {
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

	
	public static HttpSession getHttpSession(Component component) {
		return ((WebApplicationContext)component.getApplication().getContext()).getHttpSession();
	}
	
}
