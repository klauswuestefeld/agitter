package spikes;

import java.util.Map;

import com.google.common.collect.MapMaker;



public class Anything {

	public static void main(String[] args) {
		System.out.println(new Integer(1) == new Integer(1));

		System.out.println(new Long(1) == new Long(1));
		System.out.println(1 == new Long(1));
		System.out.println(new Long(1) == 1);
		int one = 1000;
		System.out.println(new Long(1000) == one);
		System.out.println(new Long(one) == one);
		System.out.println(one == new Long(1000));
		System.out.println(one == new Long(one));
		System.out.println(new Long(1) == Long.valueOf(1));
		
		Map<Long, Object> map = new MapMaker().weakValues().makeMap();
		
		Long long1 = new Long(42);
		Long long2 = new Long(42);
		System.out.println(long1 == long2);
		map.put(long1, "ban" + "ana");
		System.gc();
		System.out.println(map.get(long2));
		
		long i = 0;
		while (true) {
			map.put(new Long(i++), "a" + i);
			System.out.println(map.size());
		}
	}

}
