package guardachuva.mailer.core;

import java.io.Serializable;
import java.util.Properties;

@SuppressWarnings("serial")
public class Mail implements Serializable {

	private String _to_mail;
	private String _template;
	private Properties _properties;
	
	protected Mail() { }
	
	public Mail(String to_email, String templateName,
			Properties properties) {
		_to_mail = to_email;
		_template = templateName;
		_properties = properties;
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
	
	public Properties getProperties() {
		return _properties;
	}
	
	public void setProperties(Properties properties) {
		_properties = properties;
	}

	public String getProperty(String key) {
		return getProperties().getProperty(key);
	}

}
