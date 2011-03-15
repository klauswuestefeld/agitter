package guardachuva.agitos.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("serial")
public class Mail implements Serializable {
	
	private String _to_mail;
	private String _template;
	private HashMap<String, String> _properties;
	
	protected Mail() { }
	
	public Mail(String to_email, String templateName) {
		_to_mail = to_email;
		_template = templateName;
		_properties = new HashMap<String, String>();
	}
	
	public String getToMail() {
		return _to_mail;
	}
	
	public void setToMail(String to_mail) {
		_to_mail = to_mail;
	}
	
	public String getTemplate() {
		return _template;
	}
	
	public void setTemplate(String template) {
		_template = template;
	}

	public String get(String key) {
		return _properties.get(key);
	}

	public void set(String key, String value) {
		_properties.put(key, value);
	}

	public Set<String> getKeys() {
		return _properties.keySet();
	}

}
