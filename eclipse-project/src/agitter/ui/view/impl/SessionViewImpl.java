package agitter.ui.view.impl;

import agitter.ui.view.EventListView;
import agitter.ui.view.InviteView;
import agitter.ui.view.SessionView;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

public class SessionViewImpl implements SessionView {

	private final ComponentContainer container;
	private final VerticalLayout sessionView = new VerticalLayout();
	private final HorizontalLayout sessionTopBar = new HorizontalLayout();
	private final NativeButton logo = new NativeButton("agitter!");
	private final Label userGreeting = new Label();
	private final Button logout = logoutButton();
	private final Panel sessionScrollContent = new Panel();
	private final Panel mainContent = new Panel();
	private final InviteViewImpl inviteView = new InviteViewImpl();
	private final EventListViewImpl eventList = new EventListViewImpl();
	private final Label bottomFiller = new Label("");
	
    public SessionViewImpl(ComponentContainer container){
    	this.container = container;
    }
    	
    public void show(String username) {
        container.removeAllComponents();
    	sessionView.setSizeFull();
    	container.addComponent(sessionView); sessionView.addStyleName("a-session-view");
    		sessionTopBar.setHeight("45px");
    		sessionTopBar.setWidth("990px");
    		sessionView.addComponent(sessionTopBar); sessionTopBar.addStyleName("a-session-top-bar");
    		sessionView.setComponentAlignment(sessionTopBar, Alignment.TOP_CENTER);
    		sessionView.setExpandRatio(sessionTopBar, 0);
    			sessionTopBar.addComponent(logo); logo.addStyleName("a-session-logo");
    			sessionTopBar.setComponentAlignment(logo, Alignment.MIDDLE_LEFT);
	    		sessionTopBar.setExpandRatio(logo, 1);
	    		userGreeting.setSizeUndefined();
	    		sessionTopBar.addComponent(userGreeting); userGreeting.addStyleName("a-session-user-greeting");
	    		sessionTopBar.setComponentAlignment(userGreeting, Alignment.MIDDLE_RIGHT);
	    		sessionTopBar.setExpandRatio(userGreeting, 0);
	    		sessionTopBar.addComponent(logout); logout.addStyleName("a-session-logout-button");
	    		sessionTopBar.setComponentAlignment(logout, Alignment.MIDDLE_RIGHT);
	    		sessionTopBar.setExpandRatio(logout, 0);
	    	sessionScrollContent.addStyleName("a-session-scroll-content");
	    	sessionScrollContent.addStyleName(Reindeer.PANEL_LIGHT);
	    	sessionScrollContent.setSizeFull();
	    	sessionScrollContent.setScrollable(true);
	    	sessionView.addComponent(sessionScrollContent);
    		sessionView.setComponentAlignment(sessionScrollContent, Alignment.TOP_CENTER);
    		sessionView.setExpandRatio(sessionScrollContent, 1);
    		VerticalLayout scrollContentLayout = (VerticalLayout)sessionScrollContent.getContent();
    		scrollContentLayout.setMargin(false);
    			VerticalLayout mainContentWrapper = new VerticalLayout();
        		mainContentWrapper.setMargin(true, true, false, true);
        		mainContentWrapper.setWidth("1000px");
    			scrollContentLayout.addComponent(mainContentWrapper); mainContentWrapper.addStyleName("a-session-main-content-wrapper");
    			scrollContentLayout.setComponentAlignment(mainContentWrapper, Alignment.TOP_CENTER);
	    			mainContent.setScrollable(false);
	    			mainContent.addStyleName(Reindeer.PANEL_LIGHT);
	    			mainContentWrapper.addComponent(mainContent);  mainContent.addStyleName("a-session-main-content");
	    			mainContentWrapper.setComponentAlignment(mainContent, Alignment.TOP_CENTER);
	        		VerticalLayout mainContentLayout = (VerticalLayout)mainContent.getContent();
	        		mainContentLayout.setMargin(false);
		        		mainContentLayout.addComponent(inviteView);
		        		mainContentLayout.addComponent(eventList);
		        		bottomFiller.setSizeFull();
		        		bottomFiller.setHeight("1000px");
		        		mainContentLayout.addComponent(bottomFiller); bottomFiller.addStyleName("a-session-main-content-bottom-filler");
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
