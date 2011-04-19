package agitter.util;

import java.io.Serializable;
import java.util.Comparator;

import agitter.Agito;

public class AgitoComparator implements Comparator<Agito>, Serializable {

	private static final long serialVersionUID = -3286972461451511508L;

	@SuppressWarnings({"FeatureEnvy"})
	@Override
	public int compare(Agito a1, Agito a2) {
		int result = a1.date().compareTo(a2.date());
		if(result==0) { result = a1.description().compareTo(a2.description()); }
		return result;
	}

}
