package agitter.ui.view.session.account;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.foundation.lang.Consumer;
import vaadinutils.WidgetUtils;
import agitter.controller.oauth.OAuth;
import agitter.domain.users.User;
import agitter.ui.view.session.contacts.SelectableRemovableElementList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;


public class AccountViewImpl implements AccountView {

	private static final String ACCOUNT = "Informações de Conta";
	private static final String TWITTER = "Twitter";
	private static final String FACEBOOK = "Facebook";
	private static final String YAHOO = "Yahoo";
	private static final String GOOGLE = "Google";
	private static final String WINDOWS = "Windows";
	
	private final ComponentContainer container;
	private final ComponentContainer fixedContainer;

	private final TextField name = new TextField();
	private List<Consumer<String>> newNameConsumers = new ArrayList<Consumer<String>>();
	
	private final SelectableRemovableElementList optionsList = new SelectableRemovableElementList();

	CssLayout accountSettings = new CssLayout();
	CssLayout settingsSide = new CssLayout();	

	Consumer<String> linkAction;
	Consumer<String> unlinkAction;
	
	private Map<String, CssLayout> views = new HashMap<String, CssLayout>();
	private User user;

	public AccountViewImpl(ComponentContainer container, ComponentContainer fixedContainer) {
		this.container = container;
		this.fixedContainer = fixedContainer;
				
		createAccountSettings();

		views.put(TWITTER, new OAuthSettingsView(OAuth.TWITTER, "Twitter"));
		views.put(FACEBOOK, new OAuthSettingsView(OAuth.FACEBOOK, "Facebook"));
		views.put(GOOGLE, new OAuthSettingsView(OAuth.GOOGLE, "Google"));
		views.put(YAHOO, new OAuthSettingsView(OAuth.YAHOO, "Yahoo"));
		views.put(WINDOWS, new OAuthSettingsView(OAuth.HOTMAIL, "Hotmail, Windows, MSN"));
	}

	@Override
	public void show() {
		container.removeAllComponents();
		fixedContainer.removeAllComponents();

		CssLayout accountView = new CssLayout();
		container.addComponent(accountView); accountView.addStyleName("a-contacts-view");

		CssLayout options = new CssLayout(); options.addStyleName("a-contacts-groups");
		Label optionsCaption = WidgetUtils.createLabel("Opções");
		options.addComponent(optionsCaption); optionsCaption.addStyleName("a-contacts-groups-caption");
		
		options.addComponent(optionsList); optionsList.addStyleName("a-contacts-groups-list");
		optionsList.removeAllComponents();
		optionsList.addElementUnremovable(ACCOUNT);
		optionsList.addElementUnremovable(TWITTER, "a-auth-topbar-social-twitter a-account-social-item-list");
		optionsList.addElementUnremovable(FACEBOOK, "a-auth-topbar-social-facebook a-account-social-item-list");
		optionsList.addElementUnremovable(GOOGLE, "a-auth-topbar-social-google a-account-social-item-list");
		optionsList.addElementUnremovable(YAHOO, "a-auth-topbar-social-yahoo a-account-social-item-list");
		optionsList.addElementUnremovable(WINDOWS, "a-auth-topbar-social-windows a-account-social-item-list");
		
		settingsSide = new CssLayout(); settingsSide.addStyleName("a-account-data");
		
		accountView.addComponent(options);
		accountView.addComponent(settingsSide);
		
		setOptionSelected(ACCOUNT);
	}
	
	public void createAccountSettings() {
		name.setImmediate(true);
		ValueChangeListener nameListener = new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
			String value = (String) name.getValue();
			if (value == null || value.isEmpty()) return;
			for (Consumer<String> c : newNameConsumers) 
				c.consume(value);
		}};
		name.addListener(nameListener);
		
		accountSettings = new CssLayout(); 
		Label accountDetailsCaption = WidgetUtils.createLabel(ACCOUNT);
		accountSettings.addComponent(accountDetailsCaption); accountDetailsCaption.addStyleName("a-contacts-members-caption");
		Label caption = new Label("Nome: ");
		accountSettings.addComponent(caption); caption.addStyleName("a-account-field-caption");
		accountSettings.addComponent(name); name.addStyleName("a-contacts-members-new");
		views.put(ACCOUNT, accountSettings);
	}

	@Override
	public void setOptionSelected(String optionName) {
		if (optionName == null) {
			optionsList.highlightElement(ACCOUNT);
			setSettingsView(ACCOUNT);
		} else {
			optionsList.highlightElement(optionName);
			setSettingsView(optionName);
		}
	}
	
	private void setSettingsView(String optionName) {
		settingsSide.removeAllComponents();
		settingsSide.addComponent(views.get(optionName));
		container.requestRepaintAll();
	}

	@Override
	public void setUser(User user) {
		this.user = user;
		refreshFields();
	}
	
	private void refreshFields() {
		if (user != null) {
			name.setValue(user.name());
			
			for (String network : views.keySet()) {
				if (views.get(network) instanceof OAuthSettingsView) {
					OAuthSettingsView v = ((OAuthSettingsView)views.get(network)); 
					v.setLoginVisible(!user.isAccountLinked(network.toLowerCase()));
					v.setLogoutVisible(user.isAccountLinked(network.toLowerCase()));
					v.setLogoutCaption("Desconectar de " + user.linkedAccountUsername(network.toLowerCase()));
					v.setProfilePic(user.linkedAccountImage(network.toLowerCase()));
				}
			}
		} else {
			name.setValue("");
		}
	}

	@Override
	public void setNameListener(Consumer<String> consumer) {
		newNameConsumers.add(consumer);
	}

	@Override
	public void setOptionSelectionListener(final Consumer<String> consumer) {
		optionsList.setSelectionListener(new Consumer<String>() { @Override public void consume(String value) {
			consumer.consume(value.equals(ACCOUNT) ? null : value);
		}});
	}
	
	@Override
	public void onLink(Consumer<String> action) {
		linkAction = action;
	}

	@Override
	public void onUnlink(Consumer<String> action) {
		unlinkAction = action;
	}
	

	class OAuthSettingsView extends CssLayout {
		Button login;
		Button logout;
		Embedded image;
		
		String network;
		
		public OAuthSettingsView(final String network, String networkFriendlyName) {
			this.network = network;
					
			login = WidgetUtils.createLinkButton("Login");
			logout = WidgetUtils.createLinkButton("Logout");
			login.addStyleName("a-account-link-button");
			logout.addStyleName("a-account-link-button");
			this.addComponent(login);
			this.addComponent(logout);
			
			Label accountDetailsCaption = WidgetUtils.createLabel(networkFriendlyName);
			this.addComponent(accountDetailsCaption); 
			accountDetailsCaption.addStyleName("a-contacts-members-caption");
			accountDetailsCaption.addStyleName("a-account-social-caption");
			accountDetailsCaption.addStyleName("a-auth-topbar-social-"+network);
		
			image = new Embedded();
			this.addComponent(image);
			
			login.setDescription("Conectar com " + networkFriendlyName);
			login.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
				linkAction.consume(network);
			}});
			
			logout.setDescription("Desconectar do " + networkFriendlyName);
			logout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
				unlinkAction.consume(network);
			}});		
		}
	
		public void setLoginVisible(boolean b) {
			login.setVisible(b);
		}
	
		public void setLogoutVisible(boolean b) {
			logout.setVisible(b);
		}
	
		public void setLogoutCaption(String string) {
			logout.setCaption(string);
		}
	
		public void setProfilePic(String linkedAccountImage) {
			setPictureProfile(image, linkedAccountImage);
		}
		
		public void setPictureProfile(Embedded view, String url) {
			if (url != null) {
				view.setSource(new ExternalResource(url));
				view.setType(Embedded.TYPE_IMAGE);
				view.setSizeUndefined();
				view.setVisible(true);
			} else {
				view.setSource(null);
				view.setType(Embedded.TYPE_IMAGE);
				view.setSizeUndefined();
				view.setVisible(false);
			}
		}
	}

}
