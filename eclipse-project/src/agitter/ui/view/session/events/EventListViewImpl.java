package agitter.ui.view.session.events;

import java.util.List;

import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.ProgressIndicator;

final class EventListViewImpl extends CssLayout implements EventListView {

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
		addComponent(createPoller(millisToNextRefresh));

		for (EventVO eventData : events)
			addComponent(new EventListElement(eventData, boss));
	}

	
	private Component createPoller(int millisToNextRefresh) {
		ProgressIndicator result = new ProgressIndicator();
		result.setPollingInterval(millisToNextRefresh);
		result.setWidth("0px");
		result.setHeight("0px");
		return result;
	}

	
	private void onEventSelected(LayoutClickEvent layoutClickEvent) {
		EventListElement eventView = (EventListElement) layoutClickEvent.getChildComponent();
		if (eventView == null) {
			System.out.println("Child clicado nao achado. Component Clicked: " + (layoutClickEvent.getClickedComponent() == null) );
			return;
		}
		Object eventObject = eventView.getEventObject();
		boss.onEventSelected(eventObject);
	}


	@Override
	public void startReportingTo(Boss boss) {
		if (this.boss != null) throw new IllegalStateException();
		this.boss = boss;
	}
	
}