package agitter.ui.view.session.contacts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.*;

import sneer.foundation.lang.Consumer;

public class SelectableRemovableElementList extends CssLayout {

	private Consumer<String> selectionListener;
	private Consumer<String> removeListener;

	private final static String REMOVE_BUTTON = "REM_BUTTON";

	public SelectableRemovableElementList() {
		addStyleName("a-remov-elem-list");
		addListener(new LayoutClickListener() {
			@Override
			public void layoutClick(LayoutClickEvent event) {
				String element = getElementStringForElementLine((CssLayout)event.getChildComponent());
				if (((AbstractComponent)event.getClickedComponent()).getData() == REMOVE_BUTTON) {
					if (removeListener != null) removeListener.consume(element);
				} else {
					if (selectionListener != null) selectionListener.consume(element);
				}
			}
		});
	}

	
	public void addElement(String element) {
		addElement(element, true);
	}


	public void addElementUnremovable(String element) {
		addElement(element, false);
	}

	
	private void addElement(String element, boolean removable) {
		CssLayout elemLine = new CssLayout(); elemLine.addStyleName("a-remov-elem-list-element");
		Label newElemCaption = newElementCaptionLabel(element);
		elemLine.addComponent(newElemCaption); newElemCaption.addStyleName("a-remov-elem-list-element-caption");
		if (removable) {
			Label newElemRemove = newElementRemoveLabel();
			elemLine.addComponent(newElemRemove); newElemRemove.addStyleName("a-remov-elem-list-element-remove-button");
		}
		addComponent(elemLine);
	}

	
	public void removeEement(String element) {
		List<Component> linesToRemove = new ArrayList<Component>();
		for (Iterator<Component> it = getComponentIterator(); it.hasNext();) {
			ComponentContainer elemLine = (ComponentContainer) it.next();
			if (getElementStringForElementLine(elemLine).equals(element))
					linesToRemove.add(elemLine);
		}
		for (Iterator<Component> it = linesToRemove.iterator(); it.hasNext();) {
			Component line = (Component) it.next();
			removeComponent(line);
		}
	}


	public List<String> getElements() {
		List<String> result = new ArrayList<String>();
		for (Iterator<Component> it = getComponentIterator(); it.hasNext();) {
			ComponentContainer elemLine = (ComponentContainer) it.next();
			result.add(getElementStringForElementLine(elemLine));
		}
		return result;
	}

	public void selectElement(String element) {
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
		if(selectionListener!=null) throw new IllegalStateException();
		selectionListener = listener;
	}
	
	
	public void setRemoveListener(Consumer<String> listener) {
		if(removeListener!=null) throw new IllegalStateException();
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
		Component comp = elemLine.getComponentIterator().next();  // get the first component, which is the caption label
		return comp.toString();
	}
	
}
