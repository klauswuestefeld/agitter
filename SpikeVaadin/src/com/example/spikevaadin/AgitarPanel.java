package com.example.spikevaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;

public class AgitarPanel  extends CustomComponent {

	public AgitarPanel(final AgitoAdder adder) {
		Panel panel = new Panel();
		panel.addComponent(description);
		panel.addComponent(date);
		panel.addComponent(agitar);
		agitar.addListener(new ClickListener() {  @Override public void buttonClick(ClickEvent event) {
			adder.add(new Agito(description.toString(), date.toString()));
		}});
		setCompositionRoot(panel);
	}
	
	private TextArea description = new TextArea(null, "Descricao do seu agito...");
	private PopupDateField date = new PopupDateField();
	private Button agitar = new Button("Agitar!");

	private static final long serialVersionUID = 1L;
}
