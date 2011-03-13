package guardachuva.agitos.client.rest;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class UserData extends JavaScriptObject {

	protected UserData() { }
	
	public final native static JsArray<UserData> asArrayOfUserData(String json) /*-{
		return eval(json);
	}-*/;

	public final native String getEmail() /*-{ return this.email; }-*/;

}
