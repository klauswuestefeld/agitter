package com.example.spikevaadin;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SpikevaadinApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("Spikevaadin Application");
		
		final ComponentContainer agitosList = new VerticalLayout();

		mainWindow.addComponent((Component) new AgitarPanel(new AgitoAdder() {  @Override public void add(Agito agito) {
			agitosList.addComponent(new AgitoPanel(agito));
		} }));
		mainWindow.addComponent(agitosList);
		setMainWindow(mainWindow);
	}

}
