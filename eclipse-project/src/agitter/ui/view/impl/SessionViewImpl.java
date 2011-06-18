package agitter.ui.view.impl;

import agitter.ui.view.AgitterTheme;
import agitter.ui.view.EventListView;
import agitter.ui.view.InviteView;
import agitter.ui.view.SessionView;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.themes.BaseTheme;

public class SessionViewImpl implements SessionView {

	private final ComponentContainer container;
	private final CssLayout sessionView = new CssLayout();
	private final CssLayout sessionTopBar = new CssLayout();
	private final CssLayout sessionTopBarContent = new CssLayout();
	private final NativeButton logo = new NativeButton();
	private final CssLayout sessionTopBarRight = new CssLayout();
	private final Label userGreeting = new Label();
	private final Button logout = logoutButton();
	private final CssLayout mainContentWrapper = new CssLayout();
	private final CssLayout mainContent = new CssLayout();
	private final InviteViewImpl inviteView = new InviteViewImpl();
	private final EventListViewImpl eventList = new EventListViewImpl();
	
    public SessionViewImpl(ComponentContainer container){
    	this.container = container;
    }
    	
    public void show(String username) {
        container.removeAllComponents();
    	container.addComponent(sessionView); sessionView.addStyleName("a-session-view");
			sessionView.addComponent(sessionTopBar); sessionTopBar.addStyleName("a-session-top-bar");
				sessionTopBar.addComponent(sessionTopBarContent); sessionTopBarContent.addStyleName("a-session-top-bar-content");
					sessionTopBarContent.addComponent(logo); logo.addStyleName("a-session-logo");
		    			logo.addStyleName(AgitterTheme.DEFAULT_LOGO_COLOR_CLASS);
	    			sessionTopBarContent.addComponent(sessionTopBarRight); sessionTopBarRight.addStyleName("a-session-top-bar-right");
		    			userGreeting.setSizeUndefined();
		    			sessionTopBarRight.addComponent(userGreeting); userGreeting.addStyleName("a-session-user-greeting");
		    			sessionTopBarRight.addComponent(logout); logout.addStyleName("a-session-logout-button");
			sessionView.addComponent(mainContentWrapper); mainContentWrapper.addStyleName("a-session-main-content-wrapper");
    			mainContentWrapper.addComponent(mainContent);  mainContent.addStyleName("a-session-main-content");
    				mainContent.addComponent(eventList);
    				mainContent.addComponent(inviteView);
        userGreeting.setValue("Bem-vindo " + username + ".");
	}
    
	private Button logoutButton() {
        Button b = new Button("Sair");
        b.setStyleName(BaseTheme.BUTTON_LINK);
        return b;
	}


	@Override
	public InviteView inviteView() {
		return inviteView;
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
