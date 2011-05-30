package infra.logging;

import java.util.logging.Logger;


public class LogInfra {

	public static Logger getLogger(Object object) {
		return Logger.getLogger(object.getClass().getPackage().getName());
	}
	
}
