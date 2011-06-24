package agitter.ui.view.session.contacts;

import java.util.Arrays;
import java.util.List;

import agitter.ui.view.AgitterVaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.*;

public class GroupListViewImpl extends VerticalLayout implements GroupListView {

	private static final List<String> groups = Arrays.asList(new String[]{"Amigos", "Familia", "Facul", "Trabalho"});
	private static final List<String> friends = Arrays.asList(new String[]{"klaus", "leo@email.com", "Maria", "Jose"});

	private ListSelect groupsSelect = new ListSelect("Grupos", groups);
	private final TextField newGroup = new TextField("Adicionar Grupo");
	private final NativeButton addGroup = AgitterVaadinUtils.createDefaultNativeButton("+");
	private final NativeButton removeGroup = AgitterVaadinUtils.createDefaultNativeButton("-");

	private ListSelect friendsSelect = new ListSelect("Amigos", friends);
	private final TextField newFriend = new TextField("Adicionar Amigo");
	private final NativeButton addFriend = AgitterVaadinUtils.createDefaultNativeButton("+");
	private final NativeButton removeFriend = AgitterVaadinUtils.createDefaultNativeButton("-");

	public GroupListViewImpl() {
		groupsSelect.setInvalidAllowed(false);
		groupsSelect.setNullSelectionAllowed(false);
		groupsSelect.setImmediate(true);
		groupsSelect.addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
				GroupListViewImpl.this.getWindow().showNotification("SELECIONADO: "+valueChangeEvent.getProperty());
			}
		});

		friendsSelect.setInvalidAllowed(false);
		friendsSelect.setNullSelectionAllowed(false);
		friendsSelect.setImmediate(true);
		friendsSelect.addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
				GroupListViewImpl.this.getWindow().showNotification("SELECIONADO: "+valueChangeEvent.getProperty());
			}
		});

		HorizontalLayout panel = new HorizontalLayout();
		addComponent(panel);
		VerticalLayout direita = new VerticalLayout();
		VerticalLayout esquerda = new VerticalLayout();

		direita.addComponent(groupsSelect);
		direita.addComponent(newGroup);
		direita.addComponent(addGroup);
		direita.addComponent(removeGroup);
		panel.addComponent(direita);

		esquerda.addComponent(friendsSelect);
		esquerda.addComponent(newFriend);
		esquerda.addComponent(addFriend);
		esquerda.addComponent(removeFriend);
		panel.addComponent(esquerda);



	}
}
