package guardachuva.agitos.client.resources.events;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.client.resources.BasePresenter;
import guardachuva.agitos.domain.Event;
import guardachuva.agitos.domain.User;
import guardachuva.agitos.shared.EventDTO;
import guardachuva.agitos.shared.SessionToken;
import guardachuva.agitos.shared.UserDTO;
import guardachuva.agitos.shared.Validations;
import guardachuva.agitos.shared.rpc.RemoteApplicationAsync;

import java.util.Date;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EventsPresenter extends BasePresenter {

	private EventsWidget _eventsWidget = null;

	public EventsPresenter(IController controller, RemoteApplicationAsync application) {
		super(controller, application);
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
			_application.createEvent(getSession(), description, date, new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					showError(caught);
					getEventsWidget().resetAddEventForm();
				}
				public void onSuccess(Void result) {
					updateEventsList();
					getEventsWidget().resetAddEventForm();
				}
			}); 
			
		}
	}
	
	protected void updateEventsList() {
		_application.getEventsForMe(getSession(), new AsyncCallback<EventDTO[]>() {
			public void onSuccess(EventDTO[] events) {
				getEventsWidget().renderEvents(events);
			}
			public void onFailure(Throwable e) {
				showError(e);
			}
		});
	}
	
	public void addContact(String contactMail) {
		if (!Validations.validateMultipleEmailAndOptinalName(contactMail)) {
			_controller.showError("A lista de emails e nomes é inválida.");
		} else {
			_application.addContactsToMe(getSession(), contactMail, null, new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					updateContactsList();
				}
				public void onFailure(Throwable caught) {
					showError(caught);
				}
			});
		}
	}
	
	private void updateContactsList() {
		_application.getContactsForMe(getSession(), new AsyncCallback<UserDTO[]>() {
			public void onSuccess(UserDTO[] contacts) {
				getEventsWidget().renderContacts(contacts);
			}
			public void onFailure(Throwable caught) {
				showError(caught);
			}
		});
	}
	
	public void deleteEvent(int id) {
		if (!Window.confirm("Excluir agito?"))
			return;
		_application.removeEventForMe(getSession(), id, new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
				updateContactsList();
			}
			public void onFailure(Throwable e) {
				showError(e);
			}
		});
	}

	private SessionToken getSession() {
		return _controller.getSession();
	}

	public void ignoreProducer(String email) {
		if (!Window.confirm("Ignorar usuário?"))
			return;
		
		_application.ignoreProducerForMe(getSession(), email, new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
				updateContactsList();
			}
			public void onFailure(Throwable e) {
				showError(e);
			}
		});
	}

	public void deleteContact(String email) {
		if (!Window.confirm("Excluir contato?"))
			return;
		
		_application.deleteContactForMe(getSession(), email, new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
				updateContactsList();
			}
			public void onFailure(Throwable caught) {
				showError(caught);
			}
		});

	}

	public void logout() {
		_controller.logout();
	}

}
