package vaadinutils;

import java.net.URL;
import java.util.Map;

import com.vaadin.terminal.DownloadStream;
import com.vaadin.terminal.ParameterHandler;
import com.vaadin.terminal.URIHandler;
import com.vaadin.ui.Window;

public class RestUtils {

	public interface RestHandler {
		void onRestInvocation(URL context, String relativeUri, Map<String, String[]> params);
	}

	public static void addRestHandler(Window window, RestHandler handler) {
		RestAdapter adapter = new RestAdapter(handler);
		window.addParameterHandler(adapter);
		window.addURIHandler(adapter);
	}

	private static class RestAdapter implements ParameterHandler, URIHandler {

		private final RestHandler handler;
		Map<String, String[]> params;

		public RestAdapter(RestHandler handler) {
			this.handler = handler;
		}
		
		@Override
		public void handleParameters(Map<String, String[]> params) {
			this.params = params;
		}
		@Override
		public DownloadStream handleURI(URL context, String relativeUri) {
			handler.onRestInvocation(context, relativeUri, params);
			return null;
		}
	}

	
}
