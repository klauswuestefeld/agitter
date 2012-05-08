package agitter.ui.view.session.events;

import java.util.Iterator;
import java.util.List;

import vaadinutils.WidgetUtils;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;

final class EventListViewImpl extends CssLayout implements EventListView {

	private final ProgressIndicator poller = WidgetUtils.createPoller(100*1000); //Any large value. Will be reset later.
	private Boss boss;


	EventListViewImpl() {
		this.addListener(new LayoutEvents.LayoutClickListener() { @Override public void layoutClick(LayoutEvents.LayoutClickEvent layoutClickEvent) {
			System.out.println("Elemento da lista selecionado." + System.currentTimeMillis());
			onEventSelected(layoutClickEvent);
		}});
		addStyleName("a-event-list-view");
	}

	
	@Override
	public void refresh(List<EventVO> events, int millisToNextRefresh) {
		removeAllComponents();
		poller.setPollingInterval(millisToNextRefresh);
		addComponent(poller);

		addComponent(searchField());
		for (EventVO eventData : events)
			addComponent(new EventListElement(eventData, boss));
	}

	
	private void onEventSelected(LayoutClickEvent layoutClickEvent) {
		if (!(layoutClickEvent.getChildComponent() instanceof EventListElement)) {
			System.out.println("This is a Hack for the search field");
			return;
		}
		
		EventListElement eventView = (EventListElement) layoutClickEvent.getChildComponent();
		if (eventView == null) {
			// Vaddin Bug? Searching in a limited parent chain. 
			Component current = layoutClickEvent.getClickedComponent();
			for (int i=0; i<5; i++) {
				if (current == null) return;
				
				if (current instanceof EventListElement) {
					eventView = (EventListElement)current;
					break;
				} 
				
				current = current.getParent();
			}
			System.out.println("Searching: " + current.getClass());
			if (eventView == null) return;
		}
		Object eventObject = eventView.getEventObject();
		boss.onEventSelected(eventObject);
		
	}


	@Override
	public void startReportingTo(Boss boss) {
		if (this.boss != null) throw new IllegalStateException();
		this.boss = boss;
	}


	@Override
	public void setSelectedEvent(agitter.domain.events.Event event) {
		for (Iterator<Component> it = getComponentIterator(); it.hasNext();) {
			Component item = it.next();
			if (!(item instanceof EventListElement)) continue;
			EventListElement eventItem = (EventListElement) item;
			eventItem.setSelected(eventItem.getData().equals(event));
		}
	}

	
	private TextField searchField() {
		TextField ret = new TextField();
		ret.setInputPrompt("Search");
		ret.setImmediate(true);
		ret.setTextChangeEventMode(TextChangeEventMode.LAZY);
		ret.setTextChangeTimeout(500);
		ret.addListener(new TextChangeListener() { @Override public void textChange(TextChangeEvent event) {
			boss.onSearch(event.getText());
		}});
		return ret;
	}

}