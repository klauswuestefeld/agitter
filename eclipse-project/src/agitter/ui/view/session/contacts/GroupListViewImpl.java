package agitter.ui.view.session.contacts;

import java.util.Arrays;
import java.util.List;

import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class GroupListViewImpl extends VerticalLayout implements GroupListView {

	private static final List<String> groupNames = Arrays.asList(new String[]{"Amigos", "Familia", "Facul", "Trabalho"});

	private VerticalLayout groups = new VerticalLayout();
	private final TextField newGroup = new TextField("Adicionar Grupo");
	private final NativeButton addGroup = button("+");


	private NativeButton button(String caption) {
		return AgitterVaadinUtils.createDefaultNativeButton(caption);
	}


	public GroupListViewImpl() {
		groups.addListener(new LayoutClickListener(){  @Override public void layoutClick(LayoutClickEvent event) {
			System.out.println(((Label)event.getClickedComponent()).getValue());
		}});

		for (String group : groupNames) {
			HorizontalLayout line = new HorizontalLayout();
			line.addComponent(new Label(group));
			line.addComponent(button("X"));
			groups.addComponent(line);
		}

		addComponent(newGroup);
		addComponent(addGroup);
		addComponent(groups);
	}
}
