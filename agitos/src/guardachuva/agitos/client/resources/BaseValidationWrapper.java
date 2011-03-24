package guardachuva.agitos.client.resources;

import guardachuva.agitos.client.resources.shared.ValidationComponent;

import com.google.gwt.user.client.ui.RootPanel;

public abstract class BaseValidationWrapper extends BaseWrapper {
	
	protected ValidationComponent validationComponent;
	protected abstract RootPanel getContainerForValidation();
	public void showValidation(String errors) {
		String[] errorList = errors.split("\n");
		getValidation().displayValidationMessages(errorList);
		getContainerForValidation().add(getValidation());
	}

	protected ValidationComponent getValidation(){
		if (validationComponent == null)
			validationComponent = new ValidationComponent();
		return validationComponent;
	}
	
	protected String getIdContainerForValidation(){
		return "mainContent";
	}
}
