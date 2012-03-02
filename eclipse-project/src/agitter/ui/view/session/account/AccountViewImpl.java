package agitter.ui.view.session.account;


import java.util.ArrayList;
import java.util.List;

import sneer.foundation.lang.Consumer;
import vaadinutils.WidgetUtils;
import agitter.domain.users.User;
import agitter.ui.view.session.contacts.SelectableRemovableElementList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class AccountViewImpl implements AccountView {

	private static final String ACCOUNT = "Informações de Conta";
	
	private final ComponentContainer container;
	private final ComponentContainer fixedContainer;

	private final TextField name = new TextField();
	private List<Consumer<String>> newNameConsumers = new ArrayList<Consumer<String>>();
	
	private final SelectableRemovableElementList groupList = new SelectableRemovableElementList();

	private User user;

	public AccountViewImpl(ComponentContainer container, ComponentContainer fixedContainer) {
		this.container = container;
		this.fixedContainer = fixedContainer;
		name.setImmediate(true);
		ValueChangeListener nameListener = new ValueChangeListener() { @Override public void valueChange(ValueChangeEvent event) {
			String value = (String) name.getValue();
			if (value == null || value.isEmpty()) return;
			for (Consumer<String> c : newNameConsumers) 
				c.consume(value);
		}};
		name.addListener(nameListener);
	}

	@Override
	public void show() {
		container.removeAllComponents();
		fixedContainer.removeAllComponents();

		CssLayout contactsView = new CssLayout();
		container.addComponent(contactsView); contactsView.addStyleName("a-contacts-view");

		CssLayout options = new CssLayout(); options.addStyleName("a-contacts-groups");
		Label optionsCaption = WidgetUtils.createLabel("Opções");
		options.addComponent(optionsCaption); optionsCaption.addStyleName("a-contacts-groups-caption");
		
		options.addComponent(groupList); groupList.addStyleName("a-contacts-groups-list");
		groupList.removeAllComponents();
		groupList.addElementUnremovable(ACCOUNT);
		groupList.highlightElement(ACCOUNT);
		
		CssLayout accountDetails = new CssLayout(); accountDetails.addStyleName("a-contacts-members");
		Label accountDetailsCaption = WidgetUtils.createLabel(ACCOUNT);
		accountDetails.addComponent(accountDetailsCaption); accountDetailsCaption.addStyleName("a-contacts-members-caption");
		Label caption = new Label("Nome: ");
		accountDetails.addComponent(caption); caption.addStyleName("a-account-field-caption");
		accountDetails.addComponent(name); name.addStyleName("a-contacts-members-new");

		contactsView.addComponent(options);
		contactsView.addComponent(accountDetails);
	}

	@Override
	public void setOptionSelected(String optionName) {
		if (optionName == null)
			groupList.highlightElement(ACCOUNT);
		else
			groupList.highlightElement(optionName);
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

}
