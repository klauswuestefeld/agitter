package agitter.ui;

import java.util.Date;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;

public class InvitePanel extends CustomComponent {

	private static final long serialVersionUID = 1L;
	private static final String INVALID_DATE_MESSAGE = "Data inválida";
	private static final String INVALID_DESCRIPTION_MESSAGE = "Descreva seu agito!";
	private static final int WARNING_MESSAGES_DELAY = 2500;
	private static final String WARNING_MESSAGE_TITLE = "Atenção";

	private TextArea _descriptionTextArea = new TextArea();
	private PopupDateField _dateField = new PopupDateField();

	public InvitePanel(final EventAdder adder) {
		Panel panel = new Panel();
		panel.addComponent(_descriptionTextArea);
		panel.addComponent(_dateField);
		panel.addComponent(inviteButton(adder));
		setCompositionRoot(panel);

		_dateField.setResolution(DateField.RESOLUTION_MIN);
		_dateField.setDateFormat("dd/MM/yyyy HH:mm");
		_descriptionTextArea.setNullRepresentation("Descrição do seu agito...");
		_descriptionTextArea.setValue(null);

	}

	private Button inviteButton(final EventAdder adder) {
		Button result = new Button("Invite!");
		result.addListener(new ClickListener() { private static final long serialVersionUID = 1L; @Override public void buttonClick(ClickEvent event) {
			try {
				final String description = getEventDescription();
				final Date eventDate = getEventDate();
				adder.add(description, eventDate.getTime());
				InvitePanel.this._dateField.setValue(null);
				InvitePanel.this._descriptionTextArea.setValue(null);
			} catch (Exception exc) {
				InvitePanel.this.showErrorMessage(exc.getMessage());
			}
		}});
		return result;
	}

	private String getEventDescription() {
		final String description = (String) _descriptionTextArea.getValue();
		if(description==null) {
			_descriptionTextArea.setComponentError(new UserError(INVALID_DESCRIPTION_MESSAGE));
			throw new RuntimeException(INVALID_DESCRIPTION_MESSAGE); //TODO - Define some error msgs mechanism.
		}
		_descriptionTextArea.setComponentError(null);
		return description;
	}

	private Date getEventDate() {
		final Date eventDate = (Date) _dateField.getValue();
		if(eventDate==null) {
			_dateField.setComponentError(new UserError(INVALID_DATE_MESSAGE));
			throw new RuntimeException(INVALID_DATE_MESSAGE); //TODO - Define some error msgs mechanism.
		}
		_dateField.setComponentError(null);
		return eventDate;
	}

	private void showErrorMessage(String message) {
		Window.Notification notif = new Window.Notification(WARNING_MESSAGE_TITLE, message, Window.Notification.TYPE_WARNING_MESSAGE);
		notif.setPosition(Window.Notification.POSITION_CENTERED);
		notif.setDelayMsec(WARNING_MESSAGES_DELAY);
		this.getWindow().showNotification(notif);
	}

}
