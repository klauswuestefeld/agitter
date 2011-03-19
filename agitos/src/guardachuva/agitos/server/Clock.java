package guardachuva.agitos.server;

import java.util.Date;

public interface Clock {

	long millis();

	Date date();

}
