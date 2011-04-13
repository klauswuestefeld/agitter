package agitter.client.resources.shared;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ValidationComponent extends Composite {

	@UiField
	UListElement validationResults;
	
	private static ValidationComponentUiBinder uiBinder = GWT
			.create(ValidationComponentUiBinder.class);

	interface ValidationComponentUiBinder extends
			UiBinder<Widget, ValidationComponent> {
	}

	public ValidationComponent() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void displayValidationMessages(String[] errorList) {
		clear(); 
		displayAll(errorList);	
	}

	private void clear() {
		validationResults.setInnerHTML("");
	}
	private void displayAll(String[] errorList) {
		for (int i=0; i < errorList.length; i++) {
			displayOne(errorList[i]);
		}
	}
	private void displayOne(String msg) {
		LIElement li = Document.get().createLIElement();
        li.setInnerText(msg);
        li.addClassName("validation-message");
        validationResults.appendChild(li);
	}
}
