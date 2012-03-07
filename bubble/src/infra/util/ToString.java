package infra.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToString {
	
	public static final Comparator<Object> IGNORE_CASE = new Comparator<Object>() {  @Override public int compare(Object o1, Object o2) {
		return o1.toString().compareToIgnoreCase(o2.toString());
	}};


	public static boolean containsToString(Iterable<?> candidates, Object toMatch) {
		return searchToString(candidates, toMatch) != null;
	}


	public static <T> T findToString(Iterable<T> candidates, Object toMatch) {
		T result = searchToString(candidates, toMatch);
		if (result == null)
			throw new IllegalStateException("'" + toMatch + "' not found serching by all elements toString()");
		return result;
	}
	public static <T> T findToString(T[] candidates, Object toMatch) {
		T result = searchToString(candidates, toMatch);
		if (result == null)
			throw new IllegalStateException("'" + toMatch + "' not found serching by all elements toString()");
		return result;
	}


	private static <T> T searchToString(Iterable<T> candidates, Object toMatch) {
		String s = toMatch.toString();
		for (T obj : candidates)
			if (obj.toString().equalsIgnoreCase(s))
				return obj;
		return null;
	}
	private static <T> T searchToString(T[] candidates, Object toMatch) {
		String s = toMatch.toString();
		for (T obj : candidates)
			if (obj.toString().equalsIgnoreCase(s))
				return obj;
		return null;
	}


	static public void sortIgnoreCase(List<?> list) {
		Collections.sort(list, IGNORE_CASE);
	}


	public static List<String> toStrings(Iterable<?> iterable) {
		List<String> result = new ArrayList<String>();
		for (Object obj : iterable)
			result.add(obj.toString());
		return result ;
	}

	
	public static String[] toStrings(Object[] array) {
		String[] result = new String[array.length];
		for (int i = 0; i < result.length; i++)
			result[i] = array[i].toString();
		return result;
	}

}
