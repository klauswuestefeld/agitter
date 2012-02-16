package vaadinutils;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.Consumer;

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

	private static final long TWENTY_FOUR_HOURS = 1000 * 60 * 60 * 24;
	
	private final static String REMOVE_BUTTON = "REM_BUTTON";
	
	private CssLayout datePopupsContainer;
	private NativeButton addButton; //Clicking on this button has no effect. Lost focus from the setImmediate(true) combo-box will already trigger the event.
	private ElipsisButton elipsisButton; 
	
	private String inputPrompt = "";
	private int resolutionMin = DateField.RESOLUTION_MIN;
	private String dateFormat = "dd/MM/yyyy HH:mm";
	
	private Consumer<Long> removeListener;
	private Consumer<Long> addListener;
	
	private long lastDateCache = 0; 
	
	public MultipleDatePopup() {
		addStyleName("a-multiply-date-chooser");
		setSizeUndefined();

		datePopupsContainer = new CssLayout(); datePopupsContainer.addStyleName("a-multiply-date-chooser-datePopupsContainer");
		addComponent(datePopupsContainer);

		addNewEventButton();
		addElipseButton();
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
			Date oldDate = ((Date)dateField.getData());
			Date newDate = ((Date)event.getProperty().getValue());
			MultipleDatePopup.this.onChangeDate(oldDate.getTime(), newDate.getTime());
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
		
		dateField.focus();
		
		updateLastDateCache(date);
		updateElipsisButtonVisibility();
	}

	private Label newElementRemoveLabel() {
		Label label = new Label();
		label.setSizeUndefined();
		label.setDescription("Não me interessa");
		label.setData(REMOVE_BUTTON);
		return label;
	}
	
	public void updateLastDateCache(long date) {
		if (date > lastDateCache) 
			lastDateCache = date;
	}
	
	public void setValue(long[] datetimes) {
		datePopupsContainer.removeAllComponents();
		lastDateCache = 0;
		
		Arrays.sort(datetimes);
		
		for(long date : datetimes)
			addRemovableDate(date, datetimes.length > 1);
				
		if (datetimes.length == 0)		
			addRemovableDate(new Date().getTime(), false);
		
		close();
		elipsisButton.close();
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
		
		updateLastDateCached();
	}
	
	private void onRemovalButtonPressed(long element, ComponentContainer elemLine) {
		datePopupsContainer.removeComponent(elemLine);
		
		if (removeListener == null) return;
		removeListener.consume(element);
		
		checkAllRemovableWhenMoreThanOne();
		updateLastDateCached();
		if (elipsisButton.isClosed()) updateClosed();
		updateElipsisButtonVisibility();
	}
	
	private void onAddButtonPressed() {
		if (lastDateCache == 0) lastDateCache = Clock.currentTimeMillis();
		long element = lastDateCache + TWENTY_FOUR_HOURS;
		addRemovableDate(element, true);
		
		if (addListener == null) return;
		addListener.consume(element);
		
		checkAllRemovableWhenMoreThanOne();
		updateElipsisButtonVisibility();
	}
	
	public void open() {
		Iterator<Component> iElem = datePopupsContainer.getComponentIterator();
		while (iElem.hasNext()) {
			iElem.next().setVisible(true);
		}
	}
	
	public void close() {
		Iterator<Component> iElem = datePopupsContainer.getComponentIterator();
		int cont = 0; 
		while (iElem.hasNext()) {
			iElem.next().setVisible(cont < 3);
			cont++;
		}
	}

	public int visibleDates() {
		Iterator<Component> iElem = datePopupsContainer.getComponentIterator();
		int cont = 0; 
		while (iElem.hasNext()) {
			if (iElem.next().isVisible()) cont++;
		}
		return cont;
	}
	
	public void updateClosed() {
		// only updates if it has less than 3 visible. 
		if (visibleDates() >= 3) return;
		
		Iterator<Component> iElem = datePopupsContainer.getComponentIterator();
		int cont = 0; 
		while (iElem.hasNext()) {
			iElem.next().setVisible(cont < 3);
			cont++;
		}
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
	
	private void updateLastDateCached() {
		lastDateCache = 0;
		
		Iterator<Component> iElem = datePopupsContainer.getComponentIterator();
		while (iElem.hasNext()) {
			ComponentContainer elem = (ComponentContainer) iElem.next();
			Iterator<Component> iLabel = elem.getComponentIterator();
			while (iLabel.hasNext()) {
				Component c = iLabel.next();
				if (c instanceof PopupDateField) {
					updateLastDateCache(((Date)((PopupDateField)c).getValue()).getTime());
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
	
	private void addNewEventButton() {
		addButton = new NativeButton("mais uma data"); addButton.addStyleName("a-multiply-date-chooser-ignored");
		addButton.setSizeUndefined();
		//addButton = AgitterVaadinUtils.createDefaultAddButton(); addButton.addStyleName("a-multiply-date-chooser-ignored");
		addButton.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			onAddButtonPressed();
		}});
		addComponent(addButton);
	}
	
	private void updateElipsisButtonVisibility() {
		boolean willBeVisible = datePopupsContainer.getComponentCount()>3;
		if (!elipsisButton.isVisible() && willBeVisible) {
			elipsisButton.open();
		}
		elipsisButton.setVisible(willBeVisible);
	}

	private void addElipseButton() {
		elipsisButton = new ElipsisButton(); 
		elipsisButton.setSizeUndefined();
		elipsisButton.setOpenListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			open();
		}});
		elipsisButton.setCloseListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			close();
		}});
		addComponent(elipsisButton);
	}
}
