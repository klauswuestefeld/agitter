package agitter.ui.view.session.contacts;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.*;

import sneer.foundation.lang.Consumer;

public class RemovableElementList extends CssLayout {

	private CssLayout elements = new CssLayout();

	private Consumer<String> selectionListener;
	private Consumer<String> removeListener;

	public RemovableElementList() {
		addStyleName("a-remov-elem-list");
		elements.addListener(new LayoutClickListener() {
			@SuppressWarnings({"unchecked"})
			@Override
			public void layoutClick(LayoutClickEvent event) {
				Consumer<String> listener = (Consumer<String>) ((AbstractComponent) event.getClickedComponent()).getData();

				if(listener==null) return;
				CssLayout element = (CssLayout)event.getChildComponent();
				Component comp = element.getComponentIterator().next();  // get the first component, which is the group name label
				listener.consume(comp.toString());
			}
		});
		addComponent(elements);
	}

	public void addElement(String element) {
		CssLayout elemLine = new CssLayout(); elemLine.addStyleName("a-remov-elem-list-element");
		Label newElemCaption = newElementLabel(element, selectionListener);
		elemLine.addComponent(newElemCaption); newElemCaption.addStyleName("a-remov-elem-list-element-caption");
		Label newElemRemove = newElementLabel("X", removeListener);
		elemLine.addComponent(newElemRemove); newElemRemove.addStyleName("a-remov-elem-list-element-remove-button");
		elements.addComponent(elemLine);
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
		elements.removeAllComponents();
	}

	private Label newElementLabel(String caption, Consumer<String> listener) {
		Label label = new Label(caption);
		label.setSizeUndefined();
		label.setData(listener);
		return label;
	}

}
