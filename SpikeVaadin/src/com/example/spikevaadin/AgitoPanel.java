package com.example.spikevaadin;

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
		Label agito1 = new Label(agito.description());
		Label email = new Label("klaus@gmail.com");
		Label dataAgito = new Label(agito.date());
		mainPanel.addComponent(agito1);
		mainPanel.addComponent(email);
		mainPanel.addComponent(dataAgito);
		Button excluirBt = new Button("X");
		excluirBt.addListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("Agito removido");
				
			}
		});
		mainPanel.addComponent(excluirBt);
		setCompositionRoot(mainPanel);
		
		

	}

}
