package infra.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToString {
	
	public static final Comparator<Object> IGNORE_CASE = new Comparator<Object>() {  @Override public int compare(Object o1, Object o2) {
		return o1.toString().compareToIgnoreCase(o2.toString());
	}};


	public static boolean containsToString(Iterable<?> iterable, Object toCompare) {
		return searchToString(iterable, toCompare) != null;
	}


	public static <T> T findToString(Iterable<T> iterable, Object toCompare) {
		T result = searchToString(iterable, toCompare);
		if (result == null)
			throw new IllegalStateException("'" + toCompare + "' not found serching by all elements toString()");
		return result;
	}


	private static <T> T searchToString(Iterable<T> iterable, Object toCompare) {
		String toString = toCompare.toString();
		for (T obj : iterable)
			if (obj.toString().equalsIgnoreCase(toString ))
				return obj;
		return null;
	}


	static public void sortIgnoreCase(List<?> list) {
		Collections.sort(list, IGNORE_CASE);
	}

}
