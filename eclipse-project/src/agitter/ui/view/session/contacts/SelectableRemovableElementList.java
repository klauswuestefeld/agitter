package agitter.ui.view.session.contacts;

import java.util.Iterator;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.*;

import sneer.foundation.lang.Consumer;

public class SelectableRemovableElementList extends CssLayout {

	private Consumer<String> selectionListener;
	private Consumer<String> removeListener;

	public SelectableRemovableElementList() {
		addStyleName("a-remov-elem-list");
		addListener(new LayoutClickListener() {
			@Override
			public void layoutClick(LayoutClickEvent event) {
				AbstractComponent component = (AbstractComponent) event.getClickedComponent();
				if (component == null) return;
				Consumer<String> listener = (Consumer<String>)component.getData();
				if (listener == null) return;
				CssLayout elemLine = (CssLayout)event.getChildComponent();
				listener.consume(getElementStringForElementLine(elemLine));
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
		Label newElemCaption = newElementLabel(element, selectionListener);
		elemLine.addComponent(newElemCaption); newElemCaption.addStyleName("a-remov-elem-list-element-caption");
		if (removable) {
			Label newElemRemove = newElementLabel(null, removeListener);
			elemLine.addComponent(newElemRemove); newElemRemove.addStyleName("a-remov-elem-list-element-remove-button");
		}
		addComponent(elemLine);
	}

	
	public void selectElement(String element) {
		for (Iterator<Component> it = getComponentIterator(); it.hasNext();) {
			ComponentContainer elemLine = (ComponentContainer) it.next();
			String eachElement = getElementStringForElementLine(elemLine);
			if (eachElement.toString().equals(element))
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

	
	private Label newElementLabel(String caption, Consumer<String> listener) {
		Label label = new Label(caption);
		label.setSizeUndefined();
		label.setData(listener);
		return label;
	}

	
	private String getElementStringForElementLine(ComponentContainer elemLine) {
		Component comp = elemLine.getComponentIterator().next();  // get the first component, which is the caption label
		return comp.toString();
	}
	
}
