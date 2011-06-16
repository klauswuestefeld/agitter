package infra.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToString {
	
	public static final Comparator<Object> IGNORE_CASE = new Comparator<Object>() {  @Override public int compare(Object o1, Object o2) {
		return o1.toString().compareToIgnoreCase(o2.toString());
	}};


	public static boolean containsToString(List<?> collection, Object toCompare) {
		String toString = toCompare.toString();
		for (Object obj : collection)
			if (obj.toString().equalsIgnoreCase(toString ))
				return true;
		return false;
	}


	static public void sortIgnoreCase(List<?> list) {
		Collections.sort(list, IGNORE_CASE);
	}

}
