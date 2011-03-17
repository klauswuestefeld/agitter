package org.prevayler.bubble;

import static sneer.foundation.environments.Environments.my;
import sneer.bricks.hardware.io.prevalence.map.PrevalenceMap;
import sneer.foundation.lang.Producer;


class MapLookup implements Producer<Object> {
	
	private static PrevalenceMap PrevalenceMap = my(PrevalenceMap.class);

	
	MapLookup(Object delegate) {
		_id = PrevalenceMap.marshal(delegate);
	}


	private final long _id;
	

	@Override
	public Object produce() {
		return PrevalenceMap.unmarshal(_id);
	}


}
