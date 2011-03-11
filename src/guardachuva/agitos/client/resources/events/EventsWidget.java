package guardachuva.agitos.client.resources.events;

import guardachuva.agitos.client.json_models.EventData;
import guardachuva.agitos.client.json_models.UserData;
import guardachuva.agitos.client.resources.BasePresenter;

import java.util.Date;

import wg.gwt.widgets.client.LampTextArea;
import wg.gwt.widgets.client.LampTextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;

public class EventsWidget extends Composite {

	public static final String token = "meus_agitos";

	private final EventsPresenter _presenter;
	
	interface EventsViewUiBinder extends UiBinder<HTMLPanel, EventsWidget> { }

	private static EventsViewUiBinder uiBinder = GWT.create(EventsViewUiBinder.class);

	@UiField
	Label emailLabel;
	@UiField
	Anchor feedbackLink;
	@UiField
	Anchor logout;
	
	@UiField
	VerticalPanel eventsList;
	
	@UiField
	LampTextArea descriptionField;
	@UiField
	DateBox dateField;
	@UiField
	ListBox hourField;
	@UiField
	Button addEventButton;
	
	@UiField
	LampTextBox contactField;
	@UiField
	Button addContactButton;
	
	@UiField
	VerticalPanel contactsList;
	
	@UiField
	DialogBox feedbackDialog;
	

	public EventsWidget(EventsPresenter controller) {
		_presenter = controller;
		initWidget(uiBinder.createAndBindUi(this));
		
		emailLabel.setText(_presenter.getEmailLogado());
		dateField.setFormat(new DateBox.DefaultFormat(BasePresenter.getDateFormat()));
		fillHourField();
	}

	@Override
	protected void onLoad() {
		setDefaultValuesOnEventForm();
	}

	protected void renderEvents(JSONValue jsonValue) {
		JsArray<EventData> eventDataArray = EventData.asArrayOfEventData(jsonValue.toString());
		
		eventsList.clear();
		
		if (eventDataArray != null) {
			for (int i = 0; i < eventDataArray.length(); ++i) {
				renderEvent(eventDataArray.get(i));
			}
		}
	}
	
	private void renderEvent(final EventData eventData) {
		Label dateLabel = new Label(BasePresenter.dateToStr(eventData.getDate()));			
		Label moderatorLabel = new Label(eventData.getModerator().getEmail());
		Label descriptionLabel = new Label(eventData.getDescription());
		HorizontalPanel eventTime = new HorizontalPanel();
		VerticalPanel eventItem = new VerticalPanel();
		HorizontalPanel moderatorItem = new HorizontalPanel();
		
		Anchor ignoreAnchor = new Anchor("Ignorar");
		ignoreAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_presenter.ignoreProducer(eventData);
			}
		});
		ignoreAnchor.addStyleName("ignoreProducerAnchor");
		ignoreAnchor.setTitle("Ignorar Usuário");
		
		Anchor deleteAnchor = new Anchor("Excluir");
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_presenter.deleteEvent(eventData);
			}
		});
		deleteAnchor.addStyleName("deleteEventAnchor");
		deleteAnchor.setTitle("Excluir Agito");
		
		dateLabel.addStyleName("eventDate");
		moderatorItem.addStyleName("eventModerator");
		descriptionLabel.addStyleName("eventDescription");
		eventTime.addStyleName("eventTime");
		eventItem.addStyleName("eventItem");
		
		moderatorItem.add(moderatorLabel);
		moderatorItem.add(ignoreAnchor);
		
		eventItem.add(descriptionLabel);
		eventTime.add(dateLabel);
		eventItem.add(eventTime);
		eventItem.add(moderatorItem);
		
		eventItem.add(deleteAnchor);
		
		if (eventData.getModerator().getEmail().equals(_presenter.getEmailLogado()))
			ignoreAnchor.setVisible(false);
		else
			deleteAnchor.setVisible(false);
				
		eventsList.add(eventItem);
	}
	
	protected void renderContacts(JSONValue jsonValue) {
		JsArray<UserData> contactDataArray = UserData.asArrayOfUserData(jsonValue.toString());
		
		contactsList.clear();
		
		if (contactDataArray != null) {
			for (int i = 0; i < contactDataArray.length(); ++i) {
				renderContact(contactDataArray.get(i));
			}
		}
	}
	
	private void renderContact(final UserData userData) {
		HorizontalPanel contactPanel = new HorizontalPanel();
		Label emailLabel = new Label(userData.getEmail());
		Anchor deleteAnchor = new Anchor("del");
		deleteAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				_presenter.deleteContact(userData);
			}
		});
		contactPanel.add(emailLabel);
		contactPanel.add(deleteAnchor);
		contactsList.add(contactPanel);
		deleteAnchor.addStyleName("deleteEventAnchor");
	}

	@UiHandler("addEventButton")
	void addEventButtonClick(ClickEvent e) {
		addEvent();
	}
	
	@UiHandler("hourField")
	void hourFieldKeyPress(KeyPressEvent e) {
		if (e.getCharCode() == (char) 13)
			addEvent();
	}
	
	private void addEvent() {
		addEventButton.setEnabled(false);
		
		String[] hourFieldValue = hourField.getItemText(hourField.getSelectedIndex()).split(":");
		long eventTime = dateField.getValue().getTime();
		eventTime += Integer.parseInt(hourFieldValue[0]) * 60 * 60 * 1000;
		eventTime += Integer.parseInt(hourFieldValue[1]) * 60 * 1000;
		
		Date eventDate = new Date(eventTime);
		
		_presenter.addEvent(descriptionField.getTextCleaned(), eventDate);
	}

	@UiHandler("feedbackLink")
	void feedbackLinkClick(ClickEvent e) {
		feedbackDialog.setVisible(true);
		feedbackDialog.center();
	}
	
	@UiHandler("logout")
	void logoutLinkClick(ClickEvent e) {
		_presenter.logout();
	}
	
	@UiHandler("addContactButton")
	void addContactButtonClick(ClickEvent e) {
		addContact();
	}
	
	@UiHandler("contactField")
	void contactFieldKeyPress(KeyPressEvent e) {
		if (e.getCharCode() == (char) 13)
			addContact();
	}
	
	private void addContact() {
		addContactButton.setEnabled(false);
		_presenter.addContact(contactField.getTextCleaned());
	}

	public void resetAddEventForm() {
		descriptionField.resetText();
		descriptionField.setFocus(true);
		addEventButton.setEnabled(true);
		setDefaultValuesOnEventForm();
	}

	@SuppressWarnings("deprecation")
	private int indexOfHourFieldForNow() {
		int hour = new Date().getHours();
		boolean lessThanHalfHour = new Date().getMinutes() < 30;
		return hour * 2 + (lessThanHalfHour ? 1 : 2);
	}

	public void resetAddContactForm() {
		contactField.setText("");
		contactField.setFocus(true);
		addContactButton.setEnabled(true);
	}
	
	private void setDefaultValuesOnEventForm() {
		dateField.setValue(new Date());
		hourField.setItemSelected(indexOfHourFieldForNow(), true);
	}
	
	private void fillHourField() {
		for (int hour=0; hour<24; hour++) {
			hourField.addItem(hour + ":00");
			hourField.addItem(hour + ":30");
		}
	}
}
