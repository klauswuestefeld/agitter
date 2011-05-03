package agitter.ui.view.impl;

import agitter.ui.view.EventListView;
import agitter.ui.view.InviteView;
import agitter.ui.view.SessionView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.themes.BaseTheme;

public class SessionViewImpl implements SessionView {

	private final InviteViewImpl _invitePanel = new InviteViewImpl();
	private final EventListViewImpl _eventList = new EventListViewImpl();
	private final Button _logout = logoutButton();
	
    public SessionViewImpl(ComponentContainer container){
    	container.removeAllComponents();
		container.addComponent(_logout);
		container.addComponent(_invitePanel);
		container.addComponent(_eventList);
    }
    

	private Button logoutButton() {
        Button b = new Button("Sair");
        b.setStyleName(BaseTheme.BUTTON_LINK);
        return b;
	}


	@Override
	public InviteView inviteView() {
		return _invitePanel;
	}

	
	@Override
	public EventListView eventListView() {
		return _eventList;
	}


	@Override
	public void onLogout(final Runnable onLogout) {
		_logout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			onLogout.run();
		}});
	}


}
