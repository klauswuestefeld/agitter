package infra.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Collections {

	public static <T, U extends T> List<T> copy(Collection<U> original) {
		return new ArrayList<T>(original);
	}

	static public <T> T[] append(T[] array, T element) {
		T[] ret = Arrays.copyOf(array, array.length + 1);
		ret[ret.length - 1] = element;
		return ret;
	}

	public static <T> T[] remove(T[] array, T element) {
		int iOld = 0;
		int iNew = 0;
		while (iOld < array.length) {
			if (!array[iOld].equals(element)) array[iNew++] = array[iOld];
			iOld++;
		}
		
		return Arrays.copyOf(array, iNew);
	}

}
