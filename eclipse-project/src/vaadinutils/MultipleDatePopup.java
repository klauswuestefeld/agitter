package vaadinutils;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import sneer.foundation.lang.Consumer;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;

public class MultipleDatePopup extends CssLayout {

	private final static String REMOVE_BUTTON = "REM_BUTTON";
	
	private CssLayout datePopupsContainer;
	private final NativeButton addButton; //Clicking on this button has no effect. Lost focus from the setImmediate(true) combo-box will already trigger the event.

	private String inputPrompt = "";
	private int resolutionMin = DateField.RESOLUTION_MIN;
	private String dateFormat = "dd/MM/yyyy HH:mm";
	
	private Consumer<Long> removeListener;
	private Consumer<Long> addListener;
	
	public MultipleDatePopup() {
		addStyleName("a-multiply-date-chooser");
		setSizeUndefined();

		datePopupsContainer = new CssLayout(); datePopupsContainer.addStyleName("a-multiply-date-chooser-datePopupsContainer");

		addButton = AgitterVaadinUtils.createDefaultAddButton(); addButton.addStyleName("a-multiply-date-chooser-ignored");
		addButton.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			onAddButtonPressed();
		}});
		
		addComponent(datePopupsContainer);
		addComponent(addButton);
	}
	
	public PopupDateField newDateField() {
		PopupDateField dateField = new PopupDateField();
		dateField.setImmediate(true);
		dateField.setSizeUndefined();
		dateField.setResolution(resolutionMin);
		dateField.setDateFormat(dateFormat);
		dateField.setInputPrompt(inputPrompt);
		return dateField;
	}
	
	public void addRemovableDate(long date, boolean removable) {
		CssLayout elemLine = new CssLayout(); elemLine.addStyleName("a-remov-date-list-element");
		
		final PopupDateField dateField = newDateField();
		dateField.setData(new Date(date)); // keep the old data for changing. 
		dateField.setValue(new Date(date));
		
		dateField.addListener(new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
			long oldDate = ((Date)dateField.getData()).getTime();
			long newDate = ((Date)event.getProperty().getValue()).getTime();
			MultipleDatePopup.this.onChangeDate(oldDate, newDate);
			dateField.setData(newDate);
		}});
		
		elemLine.addComponent(dateField); dateField.addStyleName("a-remov-date-list-element-caption");
		
		Label removeButton = newElementRemoveLabel();
		
		if (!removable) 
			removeButton.setVisible(false);
		
		elemLine.addListener(new LayoutClickListener() { @Override public void layoutClick(LayoutClickEvent event) {
			onElemLineClick(event);
		}});
		elemLine.addComponent(removeButton); removeButton.addStyleName("a-remov-date-list-element-remove-button");
		
		datePopupsContainer.addComponent(elemLine);
	}

	private Label newElementRemoveLabel() {
		Label label = new Label();
		label.setSizeUndefined();
		label.setData(REMOVE_BUTTON);
		return label;
	}
	
	public void setValue(long[] datetimes) {
		datePopupsContainer.removeAllComponents();

		Arrays.sort(datetimes);
		
		for(long date : datetimes)
			addRemovableDate(date, datetimes.length > 1);
	}
	
	private Date getElementDateForElementLine(ComponentContainer elemLine) {
		PopupDateField field = (PopupDateField)elemLine.getComponentIterator().next();
		return (Date)field.getValue();
	}
	
	private void onElemLineClick(LayoutClickEvent event) {
		if (event.getChildComponent() instanceof Label) {
			Long element = getElementDateForElementLine((CssLayout)event.getComponent()).getTime();
			if (((AbstractComponent)event.getClickedComponent()).getData() == REMOVE_BUTTON) {
				onRemovalButtonPressed(element, (ComponentContainer) event.getComponent());
			}
		}
	}
	
	private void onChangeDate(long oldDate, long newDate) {
		removeListener.consume(oldDate);
		addListener.consume(newDate);
	}
	
	private void onRemovalButtonPressed(long element, ComponentContainer elemLine) {
		datePopupsContainer.removeComponent(elemLine);
		
		if (removeListener == null) return;
		removeListener.consume(element);
		
		checkAllRemovableWhenMoreThanOne();
	}
	
	private void onAddButtonPressed() {
		long element = new Date().getTime();
		addRemovableDate(element, true);
		
		if (addListener == null) return;
		addListener.consume(element);
		
		checkAllRemovableWhenMoreThanOne();
	}
	
	private void checkAllRemovableWhenMoreThanOne() {
		Iterator<Component> iElem = datePopupsContainer.getComponentIterator();
		while (iElem.hasNext()) {
			ComponentContainer elem = (ComponentContainer) iElem.next();
			Iterator<Component> iLabel = elem.getComponentIterator();
			while (iLabel.hasNext()) {
				Component c = iLabel.next();
				if (c instanceof Label) {
					c.setVisible(datePopupsContainer.getComponentCount()>1);
				}
			}
		}
	}

	public void setRemoveListener(Consumer<Long> consumer) {
		if (removeListener != null) throw new IllegalStateException();
		removeListener = consumer;
	}
	
	public void setAddListener(Consumer<Long> consumer) {
		if (addListener != null) throw new IllegalStateException();
		addListener = consumer;
	}
	
	public void setInputPrompt(String prompt) {
		this.inputPrompt = prompt;
	}

	public void setResolution(int resolutionMin) {
		this.resolutionMin = resolutionMin;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@Override
	public void focus() {
		((PopupDateField)
				((CssLayout)datePopupsContainer.getComponentIterator().next())
		.getComponentIterator().next()).focus();
	}
}
