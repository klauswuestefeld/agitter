package agitter.ui.view.session.contacts;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.*;
import sneer.foundation.lang.Consumer;

public class RemovableElementList extends VerticalLayout {

	private VerticalLayout elements = new VerticalLayout();

	private Consumer<String> selectionListener;
	private Consumer<String> removeListener;

	public RemovableElementList() {
		elements.addListener(new LayoutClickListener() {
			@SuppressWarnings({"unchecked"})
			@Override
			public void layoutClick(LayoutClickEvent event) {
				Consumer<String> listener = (Consumer<String>) ((AbstractComponent) event.getClickedComponent()).getData();

				if(listener==null) return;
				HorizontalLayout line = (HorizontalLayout)event.getChildComponent();
				listener.consume(line.getComponent(0).toString());
			}
		});
		addComponent(elements);
	}

	public void addElement(String element) {
		HorizontalLayout line = new HorizontalLayout();
		line.addComponent(newLabel(element, selectionListener));
		line.addComponent(newLabel(" _X_ ", removeListener));
		elements.addComponent(line);
	}

	public void setSelectionListener(Consumer<String> listener) {
		if(selectionListener!=null) throw new IllegalStateException();
		selectionListener = listener;
	}
	public void setRemoveListener(Consumer<String> listener) {
		if(removeListener!=null) throw new IllegalStateException();
		removeListener = listener;
	}

	private Label newLabel(String cap, Consumer<String> listener) {
		Label label = new Label(cap);
		label.setData(listener);
		return label;
	}

//	private NativeButton button(String caption) {
//		return AgitterVaadinUtils.createDefaultNativeButton(caption);
//	}

}
