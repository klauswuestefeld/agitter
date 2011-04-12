package guardachuva.agitos.client.resources;

import guardachuva.agitos.client.resources.shared.ValidationComponent;

import com.google.gwt.user.client.ui.RootPanel;

public abstract class BaseValidationWrapper extends BaseWrapper implements FailureListener {
	
	protected ValidationComponent validationComponent;
	protected abstract RootPanel getMainContainer();
	
	@Override
	public void onFailure(String... errorMessages) {
		getValidation().displayValidationMessages(errorMessages);
		getMainContainer().add(getValidation());
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
