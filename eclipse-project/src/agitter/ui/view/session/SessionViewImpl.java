package agitter.ui.view.session;

import vaadinutils.SessionUrlParameters;
import agitter.ui.view.AgitterVaadinUtils;
import agitter.ui.view.session.contacts.ContactsView;
import agitter.ui.view.session.contacts.ContactsViewImpl;
import agitter.ui.view.session.events.EventsView;
import agitter.ui.view.session.events.EventsViewImpl;

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
	private final CssLayout mainMenu = new CssLayout();
	private final Button events = linkButton("Agitos");
	private final Button contacts = linkButton("Galera");
	private final Label account = new Label();
	private final Button logout = linkButton("Sair");
	private final CssLayout mainContentWrapper = new CssLayout();
	private final CssLayout mainContent = new CssLayout();
	private final EventsView eventsView = new EventsViewImpl(mainContent);
	private final ContactsView contactsView = new ContactsViewImpl(mainContent);
	
    public SessionViewImpl(ComponentContainer container) {
    	this.container = container;
    }
    	
    public void show(String username) {
        container.removeAllComponents();
    	container.addComponent(sessionView); sessionView.addStyleName("a-session-view");
			sessionView.addComponent(sessionTopBar); sessionTopBar.addStyleName("a-session-top-bar");
				sessionTopBar.addComponent(sessionTopBarContent); sessionTopBarContent.addStyleName("a-session-top-bar-content");
					sessionTopBarContent.addComponent(logo); logo.addStyleName("a-session-logo");
		    			logo.addStyleName(AgitterVaadinUtils.DEFAULT_LOGO_COLOR_CLASS);
	    			sessionTopBarContent.addComponent(mainMenu); mainMenu.addStyleName("a-session-top-bar-right");
	    				if (SessionUrlParameters.isParameterSet(mainMenu, "groups")) {
	    					mainMenu.addComponent(events); mainMenu.addComponent(new Label("  "));
	    					mainMenu.addComponent(contacts); mainMenu.addComponent(new Label("  "));
	    				}
		    			account.setSizeUndefined();
		    			mainMenu.addComponent(account); account.addStyleName("a-session-user-greeting");
		    			mainMenu.addComponent(logout); logout.addStyleName("a-session-logout-button");
			sessionView.addComponent(mainContentWrapper); mainContentWrapper.addStyleName("a-session-main-content-wrapper");
    			mainContentWrapper.addComponent(mainContent);  mainContent.addStyleName("a-session-main-content");

    	account.setValue(username);
	}
    
	private Button linkButton(String caption) {
        Button b = new Button(caption);
        b.setStyleName(BaseTheme.BUTTON_LINK);
        return b;
	}


	@Override
	public void onLogout(final Runnable onLogout) {
		logout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			onLogout.run();
		}});
	}

	@Override
	public void onEventsMenu(final Runnable onEventsMenu) {
		events.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			onEventsMenu.run();
		}});
	}

	@Override
	public void onContactsMenu(final Runnable onContactsMenu) {
		contacts.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			onContactsMenu.run();
		}});
	}

	@Override
	public EventsView eventsView() {
		return eventsView;
	}

	@Override
	public ContactsView showContactsView() {
		contactsView.show();
		return contactsView;
	}

}
