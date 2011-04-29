package agitter.ui.view.impl;

import java.util.Date;

import agitter.ui.view.InviteView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

public class InviteViewImpl extends CustomComponent implements InviteView {

	private TextArea _descriptionTextArea = new TextArea();
	private PopupDateField _dateField = new PopupDateField();
	private Button _inviteButton = new Button("Invite!");

	public InviteViewImpl() {
		Panel panel = new Panel();
		panel.addComponent(_descriptionTextArea);
		panel.addComponent(_dateField);
		panel.addComponent(_inviteButton);
		setCompositionRoot(panel);

		_dateField.setResolution(DateField.RESOLUTION_MIN);
		_dateField.setDateFormat("dd/MM/yyyy HH:mm");
	}

	
	@Override
	public String getEventDescription() {
		return (String)_descriptionTextArea.getValue();
	}


	@Override
	public void onInvite(final Runnable action) {
		_inviteButton.addListener(new ClickListener() { private static final long serialVersionUID = 1L; @Override public void buttonClick(ClickEvent ignored) {
			action.run();			
		}});
	}

	@Override
	public Date getDatetime() {
		Object result = _dateField.getValue();
		return result == null
			? null
			: (Date)result;
	}

	@Override
	public void clearFields() {
		_descriptionTextArea.setNullRepresentation("Descrição do seu Agito...");
		InviteViewImpl.this._dateField.setValue(null);
		InviteViewImpl.this._descriptionTextArea.setValue(null);
	}

}
