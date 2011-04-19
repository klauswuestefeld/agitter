package agitter.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import agitter.EventHome;
import agitter.EventHomeImpl;
import agitter.util.SystemClock;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

public class InvitePanel extends CustomComponent {

	private TextArea description = new TextArea(null, "Descricao do seu agito...");
	private PopupDateField _date = new PopupDateField();
	private DateFormat _dateFormat = DateFormat.getDateTimeInstance();
	private EventHome _eventHome = new EventHomeImpl(new SystemClock()); //TODO - Spike to use the home in the current UI.

	public InvitePanel(final EventAdder adder) {
		Panel panel = new Panel();
		panel.addComponent(description);
		panel.addComponent(_date);
		panel.addComponent(inviteButton(adder));
		setCompositionRoot(panel);

		_date.setResolution(PopupDateField.RESOLUTION_MIN);

		//TODO - Im my workstation the format is the following, we have to see how to use locales correctly in vaadin.
		//The setLocale and setFormat in the PopupDateField only sets the view for the client side, the toString (server side) method returns a date in the format bellow.
		//_dateFormat =  ...  //"Tue Apr 19 10:23:17 BRT 2011"
		
	}

	private Button inviteButton(final EventAdder adder) {
		Button result = new Button("Invite!");
		result.addListener(new ClickListener() { private static final long serialVersionUID = 1L;  @Override public void buttonClick(ClickEvent event) {
			try {
				adder.add(InvitePanel.this._eventHome.create(description.toString(), _dateFormat.parse(_date.toString()).getTime()));
			} catch(ParseException e) {
				System.out.println("Date format shout be in the format of the following string: "+ _date.toString());
				e.printStackTrace();
			}
		}});
		return result;
	}
	

	private static final long serialVersionUID = 1L;
}
