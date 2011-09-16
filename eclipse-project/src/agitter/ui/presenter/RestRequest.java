package agitter.ui.presenter;


public abstract class RestRequest {

	private String params = "";

	public String asRelativeURI() {
		return command() + params;
	}

	abstract protected String command();

	protected void addParam(String name, String value) {
		params += params.isEmpty() ? "?" : "&";
		params += name + "=" + value;
	}

}
