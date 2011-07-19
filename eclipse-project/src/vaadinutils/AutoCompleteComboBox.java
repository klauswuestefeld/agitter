package vaadinutils;

import sneer.foundation.lang.Consumer;
import sneer.foundation.lang.Predicate;

import com.vaadin.ui.ComboBox;

public class AutoCompleteComboBox extends ComboBox {

	public AutoCompleteComboBox(String caption) {
		super(caption);

		setNewItemsAllowed(true);
        setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
        setImmediate(true);
        setNullSelectionAllowed(false);
	}

	public void addListener(final Predicate<String> valueValidator, final Consumer<String> listener) {
		addListener(new ValueChangeListener() {  @Override public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
			String string = event.getProperty().toString();
			if (!valueValidator.evaluate(string))
				return;
			setValue(null);
			listener.consume(string);
		}});
	}

}
