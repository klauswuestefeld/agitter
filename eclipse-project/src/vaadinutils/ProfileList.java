package vaadinutils;

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

/**
 * A list were each element: 
 * - Can have a Caption + Icon and so is drawn using HTML. 
 * - Can be selected (highlighted)
 * - Can be removable (Showing a remove button on hover)
 */
public class ProfileList extends CssLayout {

	private Consumer<String> selectionListener;
	private Consumer<String> removeListener;

	private final static String REMOVE_BUTTON = "REM_BUTTON";

	
	public ProfileList() {
		addStyleName("a-remov-elem-list");
		addListener(new LayoutClickListener() { @Override public void layoutClick(LayoutClickEvent event) {
			onClick(event);
		}});
	}

	public void addElementUnremovable(String key) {
		addElement(key, null, null, false, null);
	}
	
	public void addElement(String key, String value) {
		addElement(key, value, null, true, null);
	}

	public void addElement(String key, String value, String icon) {
		addElement(key, value, icon, true, null);
	}

	public void addElementUnremovable(String key, String value) {
		addElement(key, value, null, false, null);
	}
	
	public void addElementUnremovable(String key, String value, String icon) {
		addElement(key, value, icon, false, null);
	}
	
	public void addElementUnremovable(String key, String caption,	String icon, String style) {
		addElement(key, caption, icon, false, style);
	}
	
	public void addElements(Iterable<ProfileListItem> elements) {
		for (ProfileListItem p : elements)
			addElement(p.key, p.caption, p.icon);
	}
	
	public void addKeys(Iterable<String> keys) {
		for (String p : keys)
			addElement(p, null, null);
	}

	private void addElement(String key, String value, String icon, boolean removable, String style) {
		CssLayout elemLine = new CssLayout(); elemLine.addStyleName("a-remov-elem-list-element");
		Label caption = newElementCaptionLabel(getHTMLName(key, value, icon));
		caption.setData(key);
		
		if (style != null)
			caption.addStyleName(style);
		
		elemLine.addComponent(caption); 
		caption.addStyleName("a-remov-elem-list-element-caption");
		if (removable) {
			Label removeButton = newElementRemoveLabel();
			elemLine.addComponent(removeButton); removeButton.addStyleName("a-remov-elem-list-element-remove-button");
		}
		addComponent(elemLine);
	}
	
	private String getHTMLName(String key, String value, String icon) {
		if (value == null || value.isEmpty()) return key;
		
		if (icon == null)
			return "<span class='a-remov-elem-list-element-value'>" + value + "</span>" +
				   "<span class='a-remov-elem-list-element-key'>" + key + "</span>";
		else 
			return "<img src='" + icon + "' class='v-icon v-icon-list'/>" +  
				   "<span class='a-remov-elem-list-element-value'>" + value + "</span>" +
			       "<span class='a-remov-elem-list-element-key'>" + key + "</span>";
	}

	
	private void removeElement(String key) {
		List<Component> linesToRemove = new ArrayList<Component>();
		for (Iterator<Component> it = getComponentIterator(); it.hasNext();) {
			ComponentContainer elemLine = (ComponentContainer) it.next();
			if (getElementStringForElementLine(elemLine).equals(key))
					linesToRemove.add(elemLine);
		}
		for (Iterator<Component> it = linesToRemove.iterator(); it.hasNext();)
			removeComponent((Component) it.next());
	}


	public void highlightElement(String key) {
		for (Iterator<Component> it = getComponentIterator(); it.hasNext();) {
			ComponentContainer elemLine = (ComponentContainer) it.next();
			String eachElement = getElementStringForElementLine(elemLine);
			if (eachElement.equals(key))
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
		Label label = new Label(caption, Label.CONTENT_XHTML);
		label.setSizeUndefined();
		return label;
	}

	
	private Label newElementRemoveLabel() {
		Label rem = newElementCaptionLabel(null);
		rem.setData(REMOVE_BUTTON);
		return rem;
	}

	
	private String getElementStringForElementLine(ComponentContainer elemLine) {
		Iterator<Component> i = elemLine.getComponentIterator();
		Component t = i.next();
		Label caption;
		if (t instanceof Label) {
			caption = (Label)t;
		} else {
			t = i.next();
			caption = (Label)t;
		}
		return (String)caption.getData();
	}


	private void onClick(LayoutClickEvent event) {
		String element = getElementStringForElementLine((CssLayout)event.getChildComponent());
		if (((AbstractComponent)event.getClickedComponent()).getData() == REMOVE_BUTTON)
			onRemovalButtonPressed(element);
		else
			onSelection(element);
	}
	
	
	private void onRemovalButtonPressed(String key) {
		removeElement(key);
		
		if (removeListener == null) return;
		removeListener.consume(key);
	} 
		
	private void onSelection(String key) {
		if (selectionListener == null) return;
		selectionListener.consume(key);
	}
	
}