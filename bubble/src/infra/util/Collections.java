package infra.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Collections {

	public static <T> List<T> copy(Collection<T> original) {
		return new ArrayList<T>(original);
	}

}
