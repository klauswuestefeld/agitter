package guardachuva.agitos.client.resources.events;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.client.json_models.EventData;
import guardachuva.agitos.client.json_models.UserData;
import guardachuva.agitos.client.resources.BasePresenter;
import guardachuva.agitos.domain.Event;
import guardachuva.agitos.domain.User;
import guardachuva.agitos.shared.Validations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import wg.gwt.utils.httprevayler.client.DecodedResponse;
import wg.gwt.utils.httprevayler.client.SimpleRequest;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public class EventsPresenter extends BasePresenter {

	private static final String EVENTS_URL = "api/events";
	private static final String CONTACTS_URL = "api/contacts";
	private static final String IGNORED_PRODUCERS_URL = "api/ignored_producers";

	private EventsWidget _eventsWidget = null;

	public EventsPresenter(IController controller) {
		super(controller);
	}

	public EventsWidget loadDataAndShowEventsWidget() {
		updateEventsList();
		updateContactsList();
		return getEventsWidget();
	}

	private EventsWidget getEventsWidget() {
		if (_eventsWidget == null)
			_eventsWidget = new EventsWidget(this);
		
		return _eventsWidget;
	}

	protected void addEvent(String description, Date date) {
		String[] errorsForConstruction = Event.errorsForConstruction(
			new User(), 
			description, date
		);
		
		if (errorsForConstruction.length > 0) {
			String msg = "Corrija os erros:";
			for (String err : errorsForConstruction)
				msg += "\n" + err;
			_controller.showError(msg);
			_eventsWidget.addEventButton.setEnabled(true);
		} else {
			Map<String, String> params = new HashMap<String, String>();
			params.put("date", dateToStr(date));
			params.put("description", description);
			
			new SimpleRequest(EVENTS_URL, RequestBuilder.POST, params) { @Override protected void onResponseReceived(Request request, DecodedResponse response) {
				if (response.getStatusCode() == Response.SC_OK)
					updateEventsList();
				else
					_controller.showError(response.getText());
				getEventsWidget().resetAddEventForm();
			}};
		}
	}
	
	protected void updateEventsList() {
		HashMap<String, String> params = new HashMap<String, String>();
		
		new SimpleRequest(EVENTS_URL, RequestBuilder.GET, params) { @Override protected void onResponseReceived(Request request, DecodedResponse response) {
			getEventsWidget().renderEvents(response.getJsonValue());
		}};
	}
	
	public void addContact(String contactMail) {
		if (!Validations.validateMultipleEmailAndOptinalName(contactMail)) {
			_controller.showError("A lista de emails e nomes é inválida.");
		} else {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("contact_mail", contactMail);
			
			new SimpleRequest(CONTACTS_URL, RequestBuilder.POST, params) { @Override protected void onResponseReceived(Request request, DecodedResponse response) {
				updateContactsList();
				getEventsWidget().resetAddContactForm();
			}};
		}
	}
	
	private void updateContactsList() {
		HashMap<String, String> params = new HashMap<String, String>();
		
		new SimpleRequest(CONTACTS_URL, RequestBuilder.GET, params) { @Override protected void onResponseReceived(Request request, DecodedResponse response) {
				getEventsWidget().renderContacts(response.getJsonValue());
		}};
	}

	public void deleteEvent(EventData eventData) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", Integer.toString(eventData.getId()));
		
		if (!Window.confirm("Excluir agito?"))
			return;
		
		new SimpleRequest(EVENTS_URL, RequestBuilder.DELETE, params) { @Override protected void onResponseReceived(Request request, DecodedResponse response) {
			updateEventsList();
		}};
	}
	
	public void ignoreProducer(EventData eventData) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", eventData.getModerator().getEmail());
		
		if (!Window.confirm("Ignorar usuário?"))
			return;
		
		new SimpleRequest(IGNORED_PRODUCERS_URL, RequestBuilder.POST, params) { @Override protected void onResponseReceived(Request request, DecodedResponse response) {
			updateEventsList();
		}};
	}

	public void deleteContact(UserData userData) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("email", userData.getEmail());
		
		if (!Window.confirm("Excluir contato?"))
			return;

		new SimpleRequest(CONTACTS_URL, RequestBuilder.DELETE, params) { @Override protected void onResponseReceived(Request request, DecodedResponse response) {
			updateContactsList();
		}};
	}

	public void logout() {
		_controller.logout();
	}

}
