package guardachuva.agitos.server.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Flash {

	public static Flash flash(HttpServletRequest _request) {
		return getFlash(_request.getSession());
	}

	public static Flash flash(HttpSession session) {
		return getFlash(session);
	}
	
	private static Flash getFlash(HttpSession session) {
		Flash flash = (Flash) session.getAttribute("flash");
		if(flash==null) {
			flash = new Flash(); 
			session.setAttribute("flash", flash);
		}
		return flash;
	}

	private HashMap<String, List<String>> flashes = new HashMap<String, List<String>>();

	public void error(String text) {
		errors().add(text);
	}

	public List<String> errors() {
		return getListFor("errors");
	}

	private List<String> getListFor(final String category) {
		List<String> flashesFor = flashes.get(category);
		if(flashesFor==null) {
			flashesFor = new ArrayList<String>();
			flashes.put(category, flashesFor);
		}
		return flashesFor;
	}


}
