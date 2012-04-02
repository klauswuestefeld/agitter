package vaadinutils;

import java.util.List;

import sneer.foundation.lang.Predicate;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.data.Property;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeButton;

public class AutoCompleteChooser extends CssLayout {

	private ComboBox choice;
	private final NativeButton ignored; //Clicking on this button has no effect. Lost focus from the setImmediate(true) combo-box will already trigger the event.
	private Predicate<String> listener;

	public AutoCompleteChooser() {
		addStyleName("a-auto-complete-chooser");
		setSizeUndefined();

		choice = new ComboBox(); choice.addStyleName("a-auto-complete-chooser-choice");
		choice.setNewItemsAllowed(true);
		choice.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
		choice.setImmediate(true);
		choice.setNullSelectionAllowed(false);
		choice.setSizeUndefined();
		addComponent(choice);

		ignored = AgitterVaadinUtils.createDefaultAddButton(); ignored.addStyleName("a-auto-complete-chooser-ignored");
		addComponent(ignored);
	}

	public void setListener(final Predicate<String> listener) {
		if (this.listener != null) throw new IllegalStateException();
		this.listener = listener;
		choice.addListener(new Property.ValueChangeListener() { @Override public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			onChoose();
		}});
	}

	private void onChoose() {
		Object value = choice.getValue();
		if (value == null) return;
		if (!listener.evaluate(value.toString())) {
			choice.removeItem(value.toString());
			return;
		}
		choice.setValue(null);
	}
	
	public void setChoices(List<FullFeaturedItem> optionsAndCaptions) {
		choice.removeAllItems();
		for (FullFeaturedItem p : optionsAndCaptions) 
			addItem(p);
	}
	
	public void addItem(FullFeaturedItem p) {
		choice.addItem(p.key);
		if (p.caption != null)
			choice.setItemCaption(p.key, p.caption + "   (" + p.key + ") ");
		if (p.icon != null)
			choice.setItemIcon(p.key, new ExternalResource(p.icon));
	}
	
	public void setInputPrompt(String prompt) {
		choice.setInputPrompt(prompt);
	}
	
	public void setInputWidth(String width) {
		// Peccin Favor Revisar:
		// WARNING: algo errado com esse comando. Ele destroi o layout.
		//choice.setWidth(width);
	}
	
	public static class FullFeaturedItem {
		public String key;
		public String caption;
		public String icon;
		
		public FullFeaturedItem(String key, String caption, String icon) {
			super();
			this.key = key;
			this.caption = caption;
			this.icon = icon;
		}
	}
}
