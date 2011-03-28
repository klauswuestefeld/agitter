package guardachuva.agitos.server.utils;

import java.util.Date;

public interface Clock {

	long millis();

	Date date();

}
