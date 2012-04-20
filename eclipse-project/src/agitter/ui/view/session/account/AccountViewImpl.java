package agitter.ui.view.session.account;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sneer.foundation.lang.Consumer;
import vaadinutils.ProfileList;
import vaadinutils.WidgetUtils;
import agitter.common.Portal;
import agitter.domain.users.User;

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
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;


public class AccountViewImpl implements AccountView {

	private static final String ACCOUNT = "Informações de Conta";
	
	private final ComponentContainer container;

	private final TextField name = new TextField();
	private List<Consumer<String>> newNameConsumers = new ArrayList<Consumer<String>>();
	private final PasswordField password = new PasswordField();
	private final PasswordField newPassword = new PasswordField();
	private final Button changePassword = new NativeButton("Alterar Senha");
	
	private final ProfileList optionsList = new ProfileList();

	CssLayout accountSettings = new CssLayout();
	CssLayout settingsSide = new CssLayout();	

	Consumer<Portal> linkAction;
	Consumer<Portal> unlinkAction;
	
	private Map<Portal, OAuthSettingsView> viewsByPortal = new HashMap<Portal, OAuthSettingsView>();
	private User user;
	private Boss boss;
	
	public AccountViewImpl(ComponentContainer container) {
		this.container = container;
				
		createAccountSettings();

		for (Portal portal : Portal.values())
			viewsByPortal.put(portal, new OAuthSettingsView(portal));
	}

	@Override
	public void startReportingTo(Boss b) {
		if (this.boss != null) throw new IllegalStateException();
		this.boss = b;
		changePassword.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			boss.onPasswordChange((String)password.getValue(), (String)newPassword.getValue());
		}});
	}

	@Override
	public void show() {
		container.removeAllComponents();

		CssLayout accountView = new CssLayout();
		container.addComponent(accountView); accountView.addStyleName("a-contacts-view");

		CssLayout options = new CssLayout(); options.addStyleName("a-contacts-groups");
		Label optionsCaption = WidgetUtils.createLabel("Opções");
		options.addComponent(optionsCaption); optionsCaption.addStyleName("a-contacts-groups-caption");
		
		options.addComponent(optionsList); optionsList.addStyleName("a-contacts-groups-list");
		optionsList.removeAllComponents();
		optionsList.addElementUnremovable(ACCOUNT);
		for (Portal portal : Portal.values())
			addPortalOption(portal);
		
		settingsSide = new CssLayout(); settingsSide.addStyleName("a-account-data");
		
		accountView.addComponent(options);
		accountView.addComponent(settingsSide);
		
		setOptionSelected(ACCOUNT);
	}


	private void addPortalOption(Portal portal) {
		OAuthSettingsView view = viewsByPortal.get(portal);
		optionsList.addElementUnremovable(portal.name(), null, null, view.style() + " a-account-social-item-list");
	}
	
	
	private void createAccountSettings() {
		accountSettings = new CssLayout(); 
		Label accountDetailsCaption = WidgetUtils.createLabel(ACCOUNT);
		accountSettings.addComponent(accountDetailsCaption); accountDetailsCaption.addStyleName("a-contacts-members-caption");

		Label caption;
		
		caption = new Label("Nome: ");
		accountSettings.addComponent(caption); caption.addStyleName("a-account-field-caption");
		initNameField();
		accountSettings.addComponent(name); name.addStyleName("a-account-name");

//		caption = new Label("Alterar Senha: ");
//		accountSettings.addComponent(caption); caption.addStyleName("a-account-field-caption");
		password.setCaption("Senha Atual:");
		accountSettings.addComponent(password); password.addStyleName("a-account-password");
		newPassword.setCaption("Senha Nova:");
		accountSettings.addComponent(newPassword); newPassword.addStyleName("a-account-password");
		accountSettings.addComponent(changePassword);
		changePassword.addListener(new ClickListener() {  @Override public void buttonClick(ClickEvent ignored) {
			
		}});
		
	}

	private void initNameField() {
		name.setImmediate(true);
		name.addListener(new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
			String value = (String)name.getValue();
			if (value == null || value.isEmpty()) return;
			for (Consumer<String> c : newNameConsumers) 
				c.consume(value);
		}});
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
		settingsSide.addComponent(viewByName(optionName));
		container.requestRepaintAll();
	}


	private CssLayout viewByName(String optionName) {
		if (optionName.equals(ACCOUNT)) return accountSettings;
		if (optionName.contains("Windows")) return viewsByPortal.get(Portal.WindowsLive);
		return viewsByPortal.get(Portal.valueOf(optionName));
	}

	
	@Override
	public void setUser(User user) {
		this.user = user;
		refreshFields();
	}
	
	
	private void refreshFields() {
		name.setValue(userName());
		
		if (user != null)
			refreshOAuthFields();
	}

	private String userName() {
		if (user == null) return "";
		if (user.name() == null) return "";
		return user.name();
	}

	
	private void refreshOAuthFields() {
		for (OAuthSettingsView view : viewsByPortal.values())
			refreshFields(view);
	}


	private void refreshFields(OAuthSettingsView v) {
		boolean linked = user.isAccountLinked(v.portal);
		v.setLoginVisible(!linked);
		v.setLogoutVisible(linked);
		v.setLogoutCaption("Desconectar de " + user.linkedAccountUsername(v.portal));
		v.setProfilePic(user.linkedAccountImage(v.portal));
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
	public void onLink(Consumer<Portal> action) {
		linkAction = action;
	}

	
	@Override
	public void onUnlink(Consumer<Portal> action) {
		unlinkAction = action;
	}
	

	@Override
	public void clearPasswordFields() {
		password.setValue("");
		newPassword.setValue("");
	}


	class OAuthSettingsView extends CssLayout {
		Button login;
		Button logout;
		Embedded image;
		
		Portal portal;
		
		public OAuthSettingsView(final Portal portal) {
			this.portal = portal;
					
			login = WidgetUtils.createLinkButton("Login");
			logout = WidgetUtils.createLinkButton("Logout");
			login.addStyleName("a-account-link-button");
			logout.addStyleName("a-account-link-button");
			this.addComponent(login);
			this.addComponent(logout);
			
			Label accountDetailsCaption = WidgetUtils.createLabel(friendlyName());
			this.addComponent(accountDetailsCaption); 
			accountDetailsCaption.addStyleName("a-contacts-members-caption");
			accountDetailsCaption.addStyleName("a-account-social-caption");
			accountDetailsCaption.addStyleName(style());
		
			image = new Embedded();
			this.addComponent(image);
			
			login.setDescription("Conectar com " + friendlyName());
			login.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
				linkAction.consume(portal);
			}});
			
			logout.setDescription("Desconectar do " + friendlyName());
			logout.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
				unlinkAction.consume(portal);
			}});		
		}

		public String portal() {
			return portal.name();
		}
		
		public String friendlyName() {
			return portal == Portal.WindowsLive
				? "Windows Live (Hotmail, MSN, etc)"
				: portal.name();
		}


		String style() {
			return "a-auth-topbar-social-"+portal.name().toLowerCase();
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
