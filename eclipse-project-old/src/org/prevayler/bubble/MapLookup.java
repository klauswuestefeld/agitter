package org.prevayler.bubble;

import java.io.Serializable;

import sneer.foundation.lang.Producer;


class MapLookup implements Producer<Object>, Serializable {
	
	MapLookup(Object delegate) {
		_id = PrevalentContext.idMap().marshal(delegate);
	}


	private final long _id;
	

	@Override
	public Object produce() {
		return PrevalentContext.idMap().unmarshal(_id);
	}

	private static final long serialVersionUID = 1L;
}
