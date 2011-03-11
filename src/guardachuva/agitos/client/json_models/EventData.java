package guardachuva.agitos.client.json_models;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class EventData extends JavaScriptObject {

	protected EventData() { }
	
	public final native static JsArray<EventData> asArrayOfEventData(String json) /*-{
		return eval(json);
	}-*/;
	
	public final native int getId() /*-{ return this.id; }-*/;

	public final native String getDescription() /*-{ return decodeURIComponent(this.description); }-*/;

	private final native String getJSDate() /*-{ return this.date.toString(); }-*/;
	
	public final native UserData getModerator() /*-{ return this.moderator; }-*/;
	
	public final Date getDate() {
		long dateLong = Long.parseLong(getJSDate());
		return new Date(dateLong);
	}

}