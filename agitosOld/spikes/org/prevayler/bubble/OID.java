package org.prevayler.bubble;

import java.io.Serializable;

class OID implements Serializable {

	final long _id;

	
	OID(long id) {
		_id = id;
	}

	
	@Override
	public String toString() {
		return "OID:" + _id;
	}

	
	private static final long serialVersionUID = 1L;
}
