package agitter.ui;

import java.util.HashMap;


import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.VerticalLayout;

public class AgitterMainWindow extends com.vaadin.ui.Window implements AgitterPresenter.View {

	private static final long serialVersionUID = 1L;
	private final ComponentContainer eventList = new VerticalLayout();
	private final AgitterPresenter presenter;
	private final HashMap<agitter.domain.Event, EventPanel> events = new HashMap<agitter.domain.Event, EventPanel>();

    public AgitterMainWindow(AgitterPresenter presenter){
    		this.presenter = presenter;
    		this.presenter.setView(this);
		addComponent(invitePanel());
		addComponent(eventList);
    }
    
    private InvitePanel invitePanel() {
		return new InvitePanel(presenter);
	}

	@Override
	public void addEvent(agitter.domain.Event event) {
		EventPanel eventPanel = new EventPanel(event,presenter);
		events.put(event, eventPanel);		
		eventList.addComponent(eventPanel);
	}

	@Override
	public void removeEvent(agitter.domain.Event event) {
		EventPanel panel = events.get(event);
		eventList.removeComponent(panel);
	}
}
