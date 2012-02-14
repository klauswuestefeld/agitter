package agitter.ui.view.session.contacts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sneer.foundation.lang.Consumer;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

public class SelectableRemovableElementList extends CssLayout {

	private Consumer<String> selectionListener;
	private Consumer<String> removeListener;

	private final static String REMOVE_BUTTON = "REM_BUTTON";

	
	public SelectableRemovableElementList() {
		addStyleName("a-remov-elem-list");
		addListener(new LayoutClickListener() { @Override public void layoutClick(LayoutClickEvent event) {
			onClick(event);
		}});
	}

	
	public void addElement(String element) {
		addElement(element, true);
	}


	public void addElementUnremovable(String element) {
		addElement(element, false);
	}
	
	
	public void addElements(Iterable<String> elements) {
		for (String string : elements)
			addElement(string);
	}

	
	private void addElement(String element, boolean removable) {
		CssLayout elemLine = new CssLayout(); elemLine.addStyleName("a-remov-elem-list-element");
		Label caption = newElementCaptionLabel(element);
		elemLine.addComponent(caption); caption.addStyleName("a-remov-elem-list-element-caption");
		if (removable) {
			Label removeButton = newElementRemoveLabel();
			elemLine.addComponent(removeButton); removeButton.addStyleName("a-remov-elem-list-element-remove-button");
		}
		addComponent(elemLine);
	}

	
	private void removeElement(String element) {
		List<Component> linesToRemove = new ArrayList<Component>();
		for (Iterator<Component> it = getComponentIterator(); it.hasNext();) {
			ComponentContainer elemLine = (ComponentContainer) it.next();
			if (getElementStringForElementLine(elemLine).equals(element))
					linesToRemove.add(elemLine);
		}
		for (Iterator<Component> it = linesToRemove.iterator(); it.hasNext();)
			removeComponent((Component) it.next());
	}


	public List<String> getElements() {
		List<String> result = new ArrayList<String>();
		for (Iterator<Component> it = getComponentIterator(); it.hasNext();) {
			ComponentContainer elemLine = (ComponentContainer) it.next();
			result.add(getElementStringForElementLine(elemLine));
		}
		return result;
	}

	
	public void highlightElement(String element) {
		for (Iterator<Component> it = getComponentIterator(); it.hasNext();) {
			ComponentContainer elemLine = (ComponentContainer) it.next();
			String eachElement = getElementStringForElementLine(elemLine);
			if (eachElement.equals(element))
				elemLine.addStyleName("a-remov-elem-list-element-selected");
			else
				elemLine.removeStyleName("a-remov-elem-list-element-selected");
		}
	}

	
	public void setSelectionListener(Consumer<String> listener) {
		if (selectionListener != null) throw new IllegalStateException();
		selectionListener = listener;
	}
	
	
	public void setRemoveListener(Consumer<String> listener) {
		if (removeListener != null) throw new IllegalStateException();
		removeListener = listener;
	}

	
	public void removeAllElements() {
		removeAllComponents();
	}

	
	private Label newElementCaptionLabel(String caption) {
		Label label = new Label(caption);
		label.setSizeUndefined();
		return label;
	}

	
	private Label newElementRemoveLabel() {
		Label rem = newElementCaptionLabel(null);
		rem.setData(REMOVE_BUTTON);
		return rem;
	}

	
	private String getElementStringForElementLine(ComponentContainer elemLine) {
		Label caption = (Label)elemLine.getComponentIterator().next();
		return (String)caption.getValue();
	}


	private void onClick(LayoutClickEvent event) {
		String element = getElementStringForElementLine((CssLayout)event.getChildComponent());
		if (((AbstractComponent)event.getClickedComponent()).getData() == REMOVE_BUTTON)
			onRemovalButtonPressed(element);
		else
			onSelection(element);
	}
	
	
	private void onRemovalButtonPressed(String element) {
		removeElement(element);
		
		if (removeListener == null) return;
		removeListener.consume(element);
	} 
		
	private void onSelection(String element) {
		if (selectionListener == null) return;
		selectionListener.consume(element);
	}
	
}
