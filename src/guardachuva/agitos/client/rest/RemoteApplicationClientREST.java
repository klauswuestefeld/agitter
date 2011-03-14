package guardachuva.agitos.client.rest;

import guardachuva.agitos.client.DateTimeUtilsClient;
import guardachuva.agitos.shared.EventDTO;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UserDTO;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import wg.gwt.utils.httprevayler.client.DecodedResponse;
import wg.gwt.utils.httprevayler.client.SimpleRequest;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RemoteApplicationClientREST implements RemoteApplicationAsync {

	private static final String CONTACTS_URL = "api/contacts";
	private static final String EVENTS_URL = "api/events";
	private static final String ACCESS_CONTROL_URL = "api/access_control";
	private static final String IGNORED_PRODUCERS_URL = "api/ignored_producers";
	private static final String USERS_URL = "api/users";


	@Override
	public void addContactsToMe(SessionToken session, String contact_mail,
			String linkToApplication, AsyncCallback<Void> callback) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("contact_mail", contact_mail);
		
		new SimpleRequestAsyncCallbackVoid(CONTACTS_URL, 
				RequestBuilder.POST, params, callback);
	}

	@Override
	public void authenticate(String email, String password,
			AsyncCallback<SessionToken> callback) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userName", email);
		params.put("password", password);
		
		new SimpleRequestAsyncCallback<SessionToken>(ACCESS_CONTROL_URL, 
				RequestBuilder.GET, params, callback) {
			@Override
			protected SessionToken getResult(Request request, DecodedResponse response) {
				return new SessionToken(response.getText());
			}
		};
		
	}

	@Override
	public void createEvent(SessionToken session, String description,
			Date date, AsyncCallback<Void> callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("session", session.getToken());
		params.put("date", DateTimeUtilsClient.dateToStr(date));
		params.put("description", description);
		new SimpleRequestAsyncCallbackVoid(EVENTS_URL, 
				RequestBuilder.POST, params, callback);
	}

	@Override
	public void createNewUser(String name, String userName, String password,
			String email, AsyncCallback<SessionToken> callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		params.put("userName", userName);
		params.put("password", password);
		params.put("email", email);
		
		new SimpleRequestAsyncCallback<SessionToken>(USERS_URL, 
				RequestBuilder.POST, params, callback) {

			@Override
			protected SessionToken getResult(Request request,
					DecodedResponse response) {
				return new SessionToken(response.getText());
			}};		
	}

	@Override
	public void deleteContactForMe(SessionToken session, String email,
			AsyncCallback<Void> callback) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("session", session.getToken());
		params.put("email", email);
		new SimpleRequestAsyncCallbackVoid(CONTACTS_URL, 
				RequestBuilder.DELETE, params, callback);
	}
	
	@Override
	public void getContactsForMe(SessionToken session,
			AsyncCallback<UserDTO[]> callback) {
		HashMap<String, String> params = new HashMap<String, String>();	
		new SimpleRequestAsyncCallback<UserDTO[]>(CONTACTS_URL, 
				RequestBuilder.GET, params, callback){
			@Override
			protected UserDTO[] getResult(Request request, DecodedResponse response) throws Throwable {
				/*
				JsArray<UserData> users = UserData.asArrayOfUserData(response.getJsonValue().toString());
				User[] contacts = new User[users.length()];
				return null;
				/**/
				throw new RuntimeException("Implementado errado");
			}}; 
	}

	@Override
	public void getEventsForMe(SessionToken session,
			AsyncCallback<EventDTO[]> callback) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("session", session.getToken());
		
		new SimpleRequestAsyncCallback<EventDTO[]>(EVENTS_URL, 
				RequestBuilder.GET, params, callback){
			
			@Override
			protected EventDTO[] getResult(Request request, DecodedResponse response) throws Throwable {
				// FIXME: Como deserializar JSON para Event
				/*
				JSONArray array = response.getJsonValue().isArray();
				Event[] events = new Event[array.size()];
				for(int x=0; x<array.size(); x++) {
					//events[x] = array.get(x).isObject();
				}
				return events;
				/**/
				throw new RuntimeException("Implementado errado");
			}};
		
	}

	@Override
	public void ignoreProducerForMe(SessionToken session, String email,
			AsyncCallback<Void> callback) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("session", session.getToken());
		params.put("email", email);
		new SimpleRequestAsyncCallbackVoid(IGNORED_PRODUCERS_URL, 
				RequestBuilder.POST, params, callback);		
	}

	@Override
	public void removeEventForMe(SessionToken session, int id,
			AsyncCallback<Void> callback) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("session", session.getToken());
		params.put("id", Integer.toString(id));
		new SimpleRequestAsyncCallbackVoid(EVENTS_URL, 
				RequestBuilder.DELETE, params,callback);		
	}

	@Override
	public void logout(SessionToken session, AsyncCallback<Void> callback) {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public void getLoggedUserOn(SessionToken session,
			AsyncCallback<UserDTO> callback) {
		throw new RuntimeException("Not Implemented");
	}
	
	protected abstract class SimpleRequestAsyncCallback<T> extends SimpleRequest {
		private final AsyncCallback<T> _callback;

		SimpleRequestAsyncCallback(String uri, RequestBuilder.Method method, Map<String, String> params, AsyncCallback<T> callback) {
			super(uri, method, params);
			_callback = callback;
		}
		
		@Override
		protected void onResponseReceived(Request request, DecodedResponse response) {
			try {
			T result = getResult(request, response);
			_callback.onSuccess(result);
			} catch(Throwable caught) {
				_callback.onFailure(caught);
			}
		}

		abstract protected T getResult(Request request, DecodedResponse response) throws Throwable;
	}

	protected class SimpleRequestAsyncCallbackVoid extends SimpleRequestAsyncCallback<Void> {

		SimpleRequestAsyncCallbackVoid(String uri, Method method,
				Map<String, String> params, AsyncCallback<Void> callback) {
			super(uri, method, params, callback);
		}

		@Override
		protected Void getResult(Request request, DecodedResponse response) {
			return null;
		}
		
	}
}
