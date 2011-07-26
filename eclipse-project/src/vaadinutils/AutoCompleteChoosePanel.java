package vaadinutils;

import java.util.List;

import agitter.ui.view.AgitterVaadinUtils;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

public class AutoCompleteChoosePanel extends HorizontalLayout {

	private ComboBox comboBox;
	private final NativeButton chooseButton;
	private Predicate<String> valueValidator;
	private Consumer<String> listener;

	public AutoCompleteChoosePanel(String caption) {
		comboBox = new ComboBox(caption);
		chooseButton = AgitterVaadinUtils.createDefaultNativeButton("+");

		comboBox.setNewItemsAllowed(true);
		comboBox.setFilteringMode(AbstractSelect.Filtering.FILTERINGMODE_CONTAINS);
		comboBox.setImmediate(true);
		comboBox.setNullSelectionAllowed(false);
		addComponent(comboBox);
		addComponent(chooseButton);
	}

	public void setListener(final Predicate<String> valueValidator, final Consumer<String> listener) {
		if(this.listener!=null) { throw new IllegalStateException(); }
		this.valueValidator = valueValidator;
		this.listener = listener;
		comboBox.addListener(new Property.ValueChangeListener() { @Override public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			onChoose();
		}});
		chooseButton.addListener(new Button.ClickListener() { @Override public void buttonClick(Button.ClickEvent clickEvent) {
			onChoose();
		}});
	}

	private void onChoose() {
		final Object comboBoxValue = comboBox.getValue();
		if(comboBoxValue==null || !valueValidator.evaluate(comboBoxValue.toString())) { return; }
		comboBox.setValue(null);
		listener.consume(comboBoxValue.toString());
	}

	public void setChoices(List<String> options) {
		comboBox.removeAllItems();
		for(String option : options) {
			comboBox.addItem(option);
		}
	}
	public void setInputPrompt(String prompt) {
		comboBox.setInputPrompt(prompt);
	}
}
