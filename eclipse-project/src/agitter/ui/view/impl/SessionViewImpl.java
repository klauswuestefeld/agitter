package agitter.ui.view.impl;

import agitter.ui.view.EventListView;
import agitter.ui.view.InviteView;
import agitter.ui.view.SessionView;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.themes.BaseTheme;

public class SessionViewImpl implements SessionView {

	private final VerticalLayout sessionView = new VerticalLayout();
	private final InviteViewImpl invitePanel = new InviteViewImpl();
	private final EventListViewImpl eventList = new EventListViewImpl();
	private final Button logout = logoutButton();
	
    public SessionViewImpl(ComponentContainer container){
    	container.removeAllComponents();
    	sessionView.addStyleName("a-session-view");
    	sessionView.setHeight("");
    	container.addComponent(sessionView);
    		sessionView.addComponent(logout);
    		sessionView.addComponent(invitePanel);
    		sessionView.addComponent(eventList);
    }
    
	private Button logoutButton() {
        Button b = new Button("Sair");
        b.setStyleName(BaseTheme.BUTTON_LINK);
        return b;
	}


	@Override
	public InviteView inviteView() {
		return invitePanel;
	}

	
	@Override
	public EventListView eventListView() {
		return eventList;
	}


	@Override
	public void onLogout(final Runnable onLogout) {
		logout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			onLogout.run();
		}});
	}


}
