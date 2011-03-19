package samples.jetty_prevayler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Aplicacao implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<String> targets = new ArrayList<String>();

	public String processa(String target) {
		targets.add(target);
		return targets.toString();
	}

}
