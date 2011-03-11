package guardachuva.agitos.client.resources.users;

import guardachuva.agitos.client.IController;
import guardachuva.agitos.client.resources.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import wg.gwt.utils.httprevayler.client.DecodedResponse;
import wg.gwt.utils.httprevayler.client.SimpleRequest;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;

public class LoginPresenter extends BasePresenter {

	public static String ACCESS_CONTROL_URL = "api/access_control";
	private LoginWrapper _view;
	
	public LoginPresenter(IController controller) {
		super(controller);
	}
	
	public void wrap() {
		_view = new LoginWrapper(this);
	}
	
	public void tryLogin() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userName", _view.getUserName());
		params.put("password", _view.getPassword());
		
		new SimpleRequest(ACCESS_CONTROL_URL, RequestBuilder.GET, params) { @Override protected void onResponseReceived(Request request, DecodedResponse response) {
			if (response.getJsonValue().isBoolean().booleanValue())
				doLogin(_view.getUserName(), _view.getPassword());
			else {
				_controller.showError("Login ou senha inválido.");
				_view.resetForm();
			}
		}};
	}
	
	protected void doLogin(String userName, String password) {
		_controller.setLoggedUser(userName, password);
		_controller.redirect("/");
	}

}
