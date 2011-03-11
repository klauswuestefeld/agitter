package guardachuva.agitos.client.resources.users;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.client.resources.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import wg.gwt.utils.httprevayler.client.DecodedResponse;
import wg.gwt.utils.httprevayler.client.SimpleRequest;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;

public class SignupPresenter extends BasePresenter {

	private static final String USERS_URL = "api/users";
	private SignupWrapper _view;

	public SignupPresenter(IController controller) {
		super(controller);
	}

	public void wrap() {
		_view = new SignupWrapper(this);
	}

	public void trySignup() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", _view.getName());
		params.put("userName", _view.getUserName());
		params.put("password", _view.getPassword());
		params.put("email", _view.getEmail());
		
		new SimpleRequest(USERS_URL, RequestBuilder.POST, params) { @Override protected void onResponseReceived(Request request, DecodedResponse response) {
			if (response.getStatusCode() == Response.SC_OK) {
				_controller.setLoggedUser(_view.getUserName(), _view.getPassword());
				_controller.redirect("/");	
			} else
				_controller.showError(response.getJsonValue());
//			_view.resetForm();
		}};
	}

}
