package vaadinutils;

import java.util.List;

import agitter.ui.view.AgitterVaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

public class AutoCompleteChooser extends CssLayout {

	private ComboBox choice;
	private final NativeButton add;
	private Predicate<String> valueValidator;
	private Consumer<String> listener;

	public AutoCompleteChooser(String caption) {
		addStyleName("a-auto-complete-chooser");
		choice = new ComboBox(caption); choice.addStyleName("a-auto-complete-chooser-choice");
		choice.setSizeUndefined();
		add = AgitterVaadinUtils.createDefaultAddButton(); add.addStyleName("a-auto-complete-chooser-add");

		choice.setNewItemsAllowed(true);
		choice.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
		choice.setImmediate(true);
		choice.setNullSelectionAllowed(false);
		addComponent(choice);
		addComponent(add);
	}

	public void setListener(final Predicate<String> valueValidator, final Consumer<String> listener) {
		if(this.listener!=null) { throw new IllegalStateException(); }
		this.valueValidator = valueValidator;
		this.listener = listener;
		choice.addListener(new Property.ValueChangeListener() { @Override public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			onChoose();
		}});
		add.addListener(new Button.ClickListener() { @Override public void buttonClick(Button.ClickEvent clickEvent) {
			onChoose();
		}});
	}

	private void onChoose() {
		final Object comboBoxValue = choice.getValue();
		if(comboBoxValue==null || !valueValidator.evaluate(comboBoxValue.toString())) { return; }
		choice.setValue(null);
		listener.consume(comboBoxValue.toString());
	}

	public void setChoices(List<String> options) {
		choice.removeAllItems();
		for(String option : options) {
			choice.addItem(option);
		}
	}
	public void setInputPrompt(String prompt) {
		choice.setInputPrompt(prompt);
	}

}
