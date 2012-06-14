package org.prevayler.bubble;

import java.io.Serializable;

import basis.lang.Producer;


//Refactor: OID and MapLookup both only have an id field.
class MapLookup implements Producer<Object>, Serializable {
	
	MapLookup(Object delegate) {
		_id = PrevalentBubble.idMap().idFor(delegate);
	}


	final long _id;
	

	@Override
	public Object produce() {
		return PrevalentBubble.idMap().unmarshal(_id);
	}

	private static final long serialVersionUID = 1L;

}
