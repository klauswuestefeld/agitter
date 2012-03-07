package infra.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Collections {

	public static <T> List<T> copy(Collection<T> original) {
		return new ArrayList<T>(original);
	}

	static public <T> T[] append(T[] array, T element) {
		T[] ret = Arrays.copyOf(array, array.length + 1);
		ret[ret.length - 1] = element;
		return ret;
	}

}
