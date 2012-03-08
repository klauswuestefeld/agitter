package agitter.ui.view.session.account;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.foundation.lang.Consumer;
import vaadinutils.WidgetUtils;
import agitter.domain.users.User;
import agitter.ui.view.session.contacts.SelectableRemovableElementList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

public class AccountViewImpl implements AccountView {

	private static final String ACCOUNT = "Informações de Conta";
	private static final String TWITTER = "Twitter";
	
	private final ComponentContainer container;
	private final ComponentContainer fixedContainer;

	private final TextField name = new TextField();
	private List<Consumer<String>> newNameConsumers = new ArrayList<Consumer<String>>();
	
	private final SelectableRemovableElementList optionsList = new SelectableRemovableElementList();

	CssLayout settingsSide;
	
	CssLayout accountSettings;
	CssLayout twitterSettings;

	private final Button facebookLogin = new NativeButton();
	private final Button twitterLogin = new NativeButton();
	private final Button googleLogin = new NativeButton();
	private final Button windowsLogin= new NativeButton();
	private final Button yahooLogin = new NativeButton();
	
	private Map<String, CssLayout> views = new HashMap<String, CssLayout>();
	private User user;

	public AccountViewImpl(ComponentContainer container, ComponentContainer fixedContainer) {
		this.container = container;
		this.fixedContainer = fixedContainer;
		
		twitterLogin.setCaption("Login");
		
		googleLogin.setDescription("Conectar com Google");
		windowsLogin.setDescription("Conectar com WindowsLive, MSN, Hotmail");
		yahooLogin.setDescription("Conectar com Yahoo");
		facebookLogin.setDescription("Conectar com Facebook");
		twitterLogin.setDescription("Conectar com Twitter");
		
		createTwitterSettings();
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
		optionsList.addElementUnremovable(TWITTER);
		
		settingsSide = new CssLayout(); settingsSide.addStyleName("a-contacts-members");
		
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
		Label accountDetailsCaption = WidgetUtils.createLabel(TWITTER);
		twitterSettings.addComponent(accountDetailsCaption); accountDetailsCaption.addStyleName("a-contacts-members-caption");
		twitterSettings.addComponent(twitterLogin);
		Label caption = new Label("Usuario: ");
		twitterSettings.addComponent(caption); caption.addStyleName("a-account-field-caption");
		twitterSettings.addComponent(name); name.addStyleName("a-contacts-members-new");
		views.put(TWITTER, twitterSettings);
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
		System.out.println("Colocando" + optionName);
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
	public void onGoogleSignin(final Runnable action) {
		googleLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onWindowsSignin(final Runnable action) {
		windowsLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onYahooSignin(final Runnable action) {
		yahooLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onFacebookSignin(final Runnable action) {
		facebookLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}

	@Override
	public void onTwitterSignin(final Runnable action) {
		twitterLogin.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			action.run();
		}});
	}
}
