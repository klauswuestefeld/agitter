package vaadinutils;

import java.util.List;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.NativeButton;

public class AutoCompleteChooser extends CssLayout {

	private ComboBox choice;
	private final NativeButton ignored; //Clicking on this button has no effect. Lost focus from the setImmediate(true) combo-box will already trigger the event.
	private Predicate<String> valueValidator;
	private Consumer<String> listener;

	public AutoCompleteChooser() {
		addStyleName("a-auto-complete-chooser");
		choice = new ComboBox(); choice.addStyleName("a-auto-complete-chooser-choice");
		choice.setSizeUndefined();
		ignored = AgitterVaadinUtils.createDefaultAddButton(); ignored.addStyleName("a-auto-complete-chooser-ignored");

		choice.setNewItemsAllowed(true);
		choice.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
		choice.setImmediate(true);
		choice.setNullSelectionAllowed(false);
		addComponent(choice);
		addComponent(ignored);
	}

	public void setListener(final Predicate<String> valueValidator, final Consumer<String> listener) {
		if(this.listener!=null) { throw new IllegalStateException(); }
		this.valueValidator = valueValidator;
		this.listener = listener;
		choice.addListener(new Property.ValueChangeListener() { @Override public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			onChoose();
		}});
	}

	private void onChoose() {
		Object comboBoxValue = choice.getValue();
		if (comboBoxValue==null) { return;}
		if (!valueValidator.evaluate(comboBoxValue.toString())) {
			choice.removeItem(comboBoxValue.toString());
			return;
		}
		choice.setValue(null);
		listener.consume(comboBoxValue.toString());
	}

	
	public void setChoices(List<String> options) {
		choice.removeAllItems();
		for(String option : options)
			choice.addItem(option);
	}
	
	
	public void setInputPrompt(String prompt) {
		choice.setInputPrompt(prompt);
	}

}
