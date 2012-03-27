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
import com.vaadin.ui.NativeButton;
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

	CssLayout settingsSide;
	
	CssLayout accountSettings;
	CssLayout twitterSettings;
	CssLayout googleSettings;
	CssLayout facebookSettings;
	CssLayout windowsSettings;
	CssLayout yahooSettings;

	private final Button facebookLogin = WidgetUtils.createLinkButton("Login");
	private final Button twitterLogin = WidgetUtils.createLinkButton("Login");
	private final Button googleLogin = WidgetUtils.createLinkButton("Login");
	private final Button windowsLogin= WidgetUtils.createLinkButton("Login");
	private final Button yahooLogin = WidgetUtils.createLinkButton("Login");
	private final Button facebookLogout = WidgetUtils.createLinkButton("Logout");
	private final Button twitterLogout = WidgetUtils.createLinkButton("Logout");
	private final Button googleLogout = WidgetUtils.createLinkButton("Logout");
	private final Button windowsLogout= WidgetUtils.createLinkButton("Logout");
	private final Button yahooLogout = WidgetUtils.createLinkButton("Logout");
	
	private Embedded twitterImage = new Embedded();
	private Embedded facebookImage = new Embedded();
	private Embedded yahooImage = new Embedded();
	private Embedded windowsImage = new Embedded();
	private Embedded googleImage = new Embedded();

	
	private Map<String, CssLayout> views = new HashMap<String, CssLayout>();
	private User user;

	public AccountViewImpl(ComponentContainer container, ComponentContainer fixedContainer) {
		this.container = container;
		this.fixedContainer = fixedContainer;
				
		googleLogin.setDescription("Conectar com Google");
		windowsLogin.setDescription("Conectar com WindowsLive, MSN, Hotmail");
		yahooLogin.setDescription("Conectar com Yahoo");
		facebookLogin.setDescription("Conectar com Facebook");
		twitterLogin.setDescription("Conectar com Twitter");
		
		googleLogout.setDescription("Desconectar do Google");
		windowsLogout.setDescription("Desconectar do WindowsLive, MSN, Hotmail");
		yahooLogout.setDescription("Desconectar do Yahoo");
		facebookLogout.setDescription("Desconectar do Facebook");
		twitterLogout.setDescription("Desconectar do Twitter");
				
		createTwitterSettings();
		createFacebookSettings();
		createGoogleSettings();
		createYahooSettings();
		createWindowsSettings();
		
		createAccountSettings();
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

	public void createTwitterSettings() {
		twitterSettings = new CssLayout(); 
		twitterSettings.addComponent(twitterLogin);
		twitterSettings.addComponent(twitterLogout);
		twitterLogin.addStyleName("a-account-link-button");
		twitterLogout.addStyleName("a-account-link-button");
		Label accountDetailsCaption = WidgetUtils.createLabel(TWITTER);
		twitterSettings.addComponent(accountDetailsCaption); 
		accountDetailsCaption.addStyleName("a-contacts-members-caption");
		accountDetailsCaption.addStyleName("a-account-social-caption");
		accountDetailsCaption.addStyleName("a-auth-topbar-social-twitter");
		twitterSettings.addComponent(twitterImage);
		views.put(TWITTER, twitterSettings);
	}
	
	public void createFacebookSettings() {
		facebookSettings = new CssLayout(); 
		facebookSettings.addComponent(facebookLogin);
		facebookSettings.addComponent(facebookLogout);
		facebookLogin.addStyleName("a-account-link-button");
		facebookLogout.addStyleName("a-account-link-button");
		Label accountDetailsCaption = WidgetUtils.createLabel(FACEBOOK);
		facebookSettings.addComponent(accountDetailsCaption); 
		accountDetailsCaption.addStyleName("a-account-social-caption");
		accountDetailsCaption.addStyleName("a-auth-topbar-social-facebook");
		facebookSettings.addComponent(facebookImage);
		views.put(FACEBOOK, facebookSettings);
	}
	
	public void createGoogleSettings() {
		googleSettings = new CssLayout(); 
		googleSettings.addComponent(googleLogin);
		googleSettings.addComponent(googleLogout);
		googleLogin.addStyleName("a-account-link-button");
		googleLogout.addStyleName("a-account-link-button");
		Label accountDetailsCaption = WidgetUtils.createLabel(GOOGLE);
		googleSettings.addComponent(accountDetailsCaption); 
		accountDetailsCaption.addStyleName("a-account-social-caption");
		accountDetailsCaption.addStyleName("a-auth-topbar-social-google");
		googleSettings.addComponent(googleImage);
		views.put(GOOGLE, googleSettings);
	}
	
	public void createWindowsSettings() {
		windowsSettings = new CssLayout(); 
		windowsSettings.addComponent(windowsLogin);
		windowsSettings.addComponent(windowsLogout);
		windowsLogin.addStyleName("a-account-link-button");
		windowsLogout.addStyleName("a-account-link-button");
		Label accountDetailsCaption = WidgetUtils.createLabel(WINDOWS);
		windowsSettings.addComponent(accountDetailsCaption); 
		accountDetailsCaption.addStyleName("a-account-social-caption");
		accountDetailsCaption.addStyleName("a-auth-topbar-social-windows");

		windowsSettings.addComponent(windowsImage);
		views.put(WINDOWS, windowsSettings);
	}
	
	public void createYahooSettings() {
		yahooSettings = new CssLayout(); 
		yahooSettings.addComponent(yahooLogin);
		yahooSettings.addComponent(yahooLogout);
		yahooLogin.addStyleName("a-account-link-button");
		yahooLogout.addStyleName("a-account-link-button");
		Label accountDetailsCaption = WidgetUtils.createLabel(YAHOO);
		yahooSettings.addComponent(accountDetailsCaption); 
		accountDetailsCaption.addStyleName("a-account-social-caption");
		accountDetailsCaption.addStyleName("a-auth-topbar-social-yahoo");

		yahooSettings.addComponent(yahooImage);
		views.put(YAHOO, yahooSettings);
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
			
			twitterLogin.setVisible(!user.isAccountLinked(OAuth.TWITTER));
			facebookLogin.setVisible(!user.isAccountLinked(OAuth.FACEBOOK));
			googleLogin.setVisible(!user.isAccountLinked(OAuth.GOOGLE));
			yahooLogin.setVisible(!user.isAccountLinked(OAuth.YAHOO));
			windowsLogin.setVisible(!user.isAccountLinked(OAuth.HOTMAIL));

			twitterLogout.setVisible(user.isAccountLinked(OAuth.TWITTER));
			facebookLogout.setVisible(user.isAccountLinked(OAuth.FACEBOOK));
			googleLogout.setVisible(user.isAccountLinked(OAuth.GOOGLE));
			yahooLogout.setVisible(user.isAccountLinked(OAuth.YAHOO));
			windowsLogout.setVisible(user.isAccountLinked(OAuth.HOTMAIL));
				
			twitterLogout.setCaption("Desconectar de @" +  user.linkedAccountUsername(OAuth.TWITTER));
			facebookLogout.setCaption("Desconectar de " +  user.linkedAccountUsername(OAuth.FACEBOOK));
			yahooLogout.setCaption("Desconectar de " +  user.linkedAccountUsername(OAuth.YAHOO));
			googleLogout.setCaption("Desconectar de " +  user.linkedAccountUsername(OAuth.GOOGLE));
			windowsLogout.setCaption("Desconectar de " +  user.linkedAccountUsername(OAuth.HOTMAIL));
			
			setPictureProfile(twitterImage, user.linkedAccountImage(OAuth.TWITTER));
			setPictureProfile(facebookImage, user.linkedAccountImage(OAuth.FACEBOOK));
			setPictureProfile(googleImage, user.linkedAccountImage(OAuth.GOOGLE));
			setPictureProfile(yahooImage, user.linkedAccountImage(OAuth.YAHOO));
			setPictureProfile(windowsImage, user.linkedAccountImage(OAuth.HOTMAIL));

		} else {
			name.setValue("");
		}
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
	public void onGoogleLink(final Runnable action) {
		googleLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
			
		}});
	}

	@Override
	public void onWindowsLink(final Runnable action) {
		windowsLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onYahooLink(final Runnable action) {
		yahooLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onFacebookLink(final Runnable action) {
		facebookLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onTwitterLink(final Runnable action) {
		twitterLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}
	
	@Override
	public void onGoogleUnlink(final Runnable action) {
		googleLogout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onWindowsUnlink(final Runnable action) {
		windowsLogout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onYahooUnlink(final Runnable action) {
		yahooLogout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onFacebookUnlink(final Runnable action) {
		facebookLogout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onTwitterUnlink(final Runnable action) {
		twitterLogout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}
}
