package agitter;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

public class AgitoPanel extends CustomComponent {

	private Panel mainPanel;

	public AgitoPanel(Agito agito) {
		mainPanel = new Panel();
		mainPanel.addComponent(new Label(agito.description()));
		mainPanel.addComponent(new Label("klaus@gmail.com"));
		mainPanel.addComponent(new Label(agito.date()));
		mainPanel.addComponent(removeButton());
		setCompositionRoot(mainPanel);
	}


	private Button removeButton() {
		Button result = new Button("X");
		result.addListener(new ClickListener() {  private static final long serialVersionUID = 1L;  @Override public void buttonClick(ClickEvent event) {
			System.out.println("Agito removido");
		}});
		return result;
	}

	
	private static final long serialVersionUID = 1L;
}
