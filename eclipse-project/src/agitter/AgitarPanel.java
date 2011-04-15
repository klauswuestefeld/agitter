package agitter;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

public class AgitarPanel  extends CustomComponent {

	private TextArea description = new TextArea(null, "Descricao do seu agito...");
	private PopupDateField date = new PopupDateField();

	public AgitarPanel(final AgitoAdder adder) {
		Panel panel = new Panel();
		panel.addComponent(description);
		panel.addComponent(date);
		panel.addComponent(agitarButton(adder));
		setCompositionRoot(panel);
	}

	private Button agitarButton(final AgitoAdder adder) {
		Button result = new Button("Agitar!");
		result.addListener(new ClickListener() { private static final long serialVersionUID = 1L;  @Override public void buttonClick(ClickEvent event) {
			adder.add(new Agito(description.toString(), date.toString()));
		}});
		return result;
	}
	

	private static final long serialVersionUID = 1L;
}
