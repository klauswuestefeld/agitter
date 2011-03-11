package guardachuva.mailer.templates;

import java.util.Properties;

public abstract class MailTemplate {

	public abstract String getSubject();

	protected abstract String getBodyTemplate();

	public String fillOutWith(Properties properties) {
		String result = getBodyTemplate();
		for (Object key : properties.keySet())
			result = result.replace("[" + (String) key + "]", (String) properties.get(key));

		return result;
	}

}