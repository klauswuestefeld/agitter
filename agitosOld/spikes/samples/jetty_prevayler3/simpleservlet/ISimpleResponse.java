package samples.jetty_prevayler3.simpleservlet;

import java.io.IOException;
import java.io.PrintWriter;

public interface ISimpleResponse {

	void setContentType(String string);

	void setStatus(int scOk);

	PrintWriter getWriter() throws IOException;

}
