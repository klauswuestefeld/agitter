package agitter.client.resources.events;


import java.util.Date;
import java.util.List;

import agitter.client.IController;
import agitter.client.resources.BasePresenter;
import agitter.shared.EventDTO;
import agitter.shared.UserDTO;
import agitter.shared.Validations;
import agitter.shared.rpc.RemoteApplicationAsync;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EventsPresenter extends BasePresenter {

	private EventsWidget _eventsWidget = null;

	public EventsPresenter(IController controller, RemoteApplicationAsync application) {
		super(controller, application);
	}

	public EventsWidget loadDataAndShowEventsWidget() {
		_eventsWidget = new EventsWidget(this);
		updateEventsList();
		updateContactsList();
		return getEventsWidget();
	}

	private EventsWidget getEventsWidget() {
		return _eventsWidget;
	}

	protected void addEvent(String description, Date date) {
		String[] errorsForConstruction = EventDTO.errorsForConstruction(
			getLoggedUserEmail(), 
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
				@Override
				public void onFailure(Throwable caught) {
					showError(caught);
					getEventsWidget().resetAddEventForm();
				}
				@Override
				public void onSuccess(Void result) {
					getEventsWidget().resetAddEventForm();
					updateEventsList();
				}
			}); 
			
		}
	}
	
	protected void updateEventsList() {
		_application.getEventsForMe(getSession(), new AsyncCallback<List<EventDTO>>() {
			@Override
			public void onSuccess(List<EventDTO> events) {
				getEventsWidget().renderEvents(events);
			}
			@Override
			public void onFailure(Throwable e) {
				showError(e);
			}
		});
	}
	
	public void addContact(String contactMail) {
		if (!Validations.validateMultipleEmailAndOptinalName(contactMail)) {
			_controller.showError("A lista de emails e nomes é inválida.");
		} else {
			_application.addContactsToMe(getSession(), contactMail, new AsyncCallback<Void>() {
				@Override
				public void onSuccess(Void result) {
					getEventsWidget().resetAddContactForm();
					updateContactsList();
				}
				@Override
				public void onFailure(Throwable caught) {
					showError(caught);
				}
			});
		}
	}
	
	private void updateContactsList() {
		_application.getContactsForMe(getSession(), new AsyncCallback<List<UserDTO>>() {
			@Override
			public void onSuccess(List<UserDTO> contacts) {
				getEventsWidget().renderContacts(contacts);
			}
			@Override
			public void onFailure(Throwable caught) {
				showError(caught);
			}
		});
	}
	
	public void deleteEvent(EventDTO event) {
		if (!Window.confirm("Excluir agito?"))
			return;
		_application.removeEventForMe(getSession(), event.getId(), new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				updateEventsList();
			}
			@Override
			public void onFailure(Throwable e) {
				showError(e);
			}
		});
	}

	public void ignoreProducer(EventDTO event) {
		if (!Window.confirm("Ignorar usuário?"))
			return;
		
		_application.ignoreProducerForMe(getSession(), event.getModerator().getEmail(), new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				updateContactsList();
				updateEventsList();
			}
			@Override
			public void onFailure(Throwable e) {
				showError(e);
			}
		});
	}

	public void deleteContact(UserDTO contact) {
		if (!Window.confirm("Excluir contato?"))
			return;
		
		_application.deleteContactForMe(getSession(), contact.getEmail(), new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				updateContactsList();
			}
			@Override
			public void onFailure(Throwable caught) {
				showError(caught);
			}
		});

	}

	public void importContacts(String providerName) {
		this._controller.redirectToSocialAuth(providerName);
	}

}
