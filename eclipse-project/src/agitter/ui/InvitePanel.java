package agitter.ui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

public class InvitePanel extends CustomComponent {

	private TextArea description = new TextArea(null, "Descricao do seu agito...");
	private PopupDateField date = new PopupDateField();

	public InvitePanel(final EventAdder adder) {
		Panel panel = new Panel();
		panel.addComponent(description);
		panel.addComponent(date);
		panel.addComponent(inviteButton(adder));
		setCompositionRoot(panel);
	}

	private Button inviteButton(final EventAdder adder) {
		Button result = new Button("Invite!");
		result.addListener(new ClickListener() { private static final long serialVersionUID = 1L;  @Override public void buttonClick(ClickEvent event) {
			DateFormat dateFormat = new SimpleDateFormat(); //TODO - See whats the pattern and set
			try {
				adder.add(new agitter.Event(description.toString(), dateFormat.parse(date.toString()).getTime()));
			} catch(ParseException e) {
				System.out.println("Date format shout be in the format of the folowing string: "+ date.toString());
				e.printStackTrace();
			}
		}});
		return result;
	}
	

	private static final long serialVersionUID = 1L;
}
