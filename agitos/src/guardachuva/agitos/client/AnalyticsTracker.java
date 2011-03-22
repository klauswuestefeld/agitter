package guardachuva.agitos.client;

public class AnalyticsTracker {

	public static void track(String historyToken) {
		if (historyToken == null) {
			historyToken = "null";
		}

		historyToken = "/agitos/" + historyToken;

		trackGoogleAnalytics(historyToken);

	}

	public static native void trackGoogleAnalytics(String historyToken) /*-{
	    try {
		    var pageTracker = $wnd._gat._getTracker("UA-22190720-1");
		    
		    pageTracker._setRemoteServerMode();
		    
		    // turn on anchor observing
		    pageTracker._setAllowAnchor(true)
		    
		    // send event to google server
		    pageTracker._trackPageview(historyToken);
		        
		} catch(err) { }
    }-*/;

}
