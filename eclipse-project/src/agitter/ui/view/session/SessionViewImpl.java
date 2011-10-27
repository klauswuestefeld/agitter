package agitter.ui.view.session;

import vaadinutils.SessionUrlParameters;
import vaadinutils.WidgetUtils;
import agitter.ui.view.session.contacts.ContactsView;
import agitter.ui.view.session.contacts.ContactsViewImpl;
import agitter.ui.view.session.events.EventsView;
import agitter.ui.view.session.events.EventsViewImpl;
import agitter.ui.view.session.events.EventsViewImplOld;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;

public class SessionViewImpl implements SessionView {

	private final ComponentContainer container;
	private final CssLayout sessionView = new CssLayout();
	private final CssLayout sessionTopBar = new CssLayout();
	private final CssLayout sessionTopBarContent = new CssLayout();
	private final NativeButton logo = new NativeButton();
	private final CssLayout mainMenu = new CssLayout();
	private final Button events = WidgetUtils.createLinkButton("Agitos");
	private final Button contacts = WidgetUtils.createLinkButton("Galera");
	private final Label account = WidgetUtils.createLabel("");
	private final Label accountSeparator = WidgetUtils.createLabel("-");
	private final Button logout = WidgetUtils.createLinkButton("Sair");
	private final CssLayout mainContentWrapper = new CssLayout();
	private final CssLayout mainContent = new CssLayout();
	private EventsView eventsView;

	private final ContactsView contactsView = new ContactsViewImpl(mainContent);

	private static final String MENU_DEFAULT_STYLE = "a-session-menu-default";
	private static final String MENU_ACTIVE_STYLE = "a-session-menu-active";

	
    public SessionViewImpl(ComponentContainer container) {
    	this.container = container;
    }

    
    @Override
    public void init(Needs needs) {
        container.removeAllComponents();
    	container.addComponent(sessionView); sessionView.addStyleName("a-session-view");
			sessionView.addComponent(sessionTopBar); sessionTopBar.addStyleName("a-session-top-bar");
				sessionTopBar.addComponent(sessionTopBarContent); sessionTopBarContent.addStyleName("a-session-top-bar-content");
					sessionTopBarContent.addComponent(logo); logo.addStyleName("a-session-logo");
	    			sessionTopBarContent.addComponent(mainMenu); mainMenu.addStyleName("a-session-top-bar-right");
    					mainMenu.addComponent(events);  events.addStyleName("a-session-menu-events");
    					events.addStyleName(MENU_DEFAULT_STYLE);
    					mainMenu.addComponent(contacts); contacts.addStyleName("a-session-menu-contacts");
    					contacts.addStyleName(MENU_DEFAULT_STYLE);
		    			mainMenu.addComponent(account); account.addStyleName("a-session-menu-user");
		    			account.addStyleName(MENU_DEFAULT_STYLE);
		    			mainMenu.addComponent(accountSeparator); accountSeparator.addStyleName("a-session-menu-user-separator");
		    			accountSeparator.addStyleName(MENU_DEFAULT_STYLE);
		    			mainMenu.addComponent(logout); logout.addStyleName("a-session-menu-logout");
		    			logout.addStyleName(MENU_DEFAULT_STYLE);
			sessionView.addComponent(mainContentWrapper); mainContentWrapper.addStyleName("a-session-main-content-wrapper");
    			mainContentWrapper.addComponent(mainContent);  mainContent.addStyleName("a-session-main-content");

    	account.setValue(needs.userScreenName());

    	initListeners(needs);
	}


	private void initListeners(final Needs needs) {
		logout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
    		needs.onLogout();
    	}});
    	events.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
    		needs.onEventsMenu();
    	}});
    	contacts.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
    		needs.onContactsMenu();
    	}});
	}


	@Override public EventsView eventsView() {
		if (eventsView == null)
			eventsView = createEventView();
		return eventsView;
	}
	@Override public void showEventsView() {
		highlightMenuItem(events);
		eventsView().show();
	}

	
	@Override public ContactsView contactsView() { return contactsView; }  
	@Override public void showContactsView() {
		highlightMenuItem(contacts);
		contactsView.show();
	}

	
	private void highlightMenuItem(Component menuItem) {
		events.removeStyleName(MENU_ACTIVE_STYLE);
		contacts.removeStyleName(MENU_ACTIVE_STYLE);

		menuItem.addStyleName(MENU_ACTIVE_STYLE);
	}

	
	private EventsView createEventView() {
		return SessionUrlParameters.isParameterSet(container, "new-events-screen")
			? new EventsViewImpl(container)
			: new EventsViewImplOld(mainContent);
	}

}
