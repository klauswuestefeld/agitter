package org.prevayler.bubble;

import java.io.Serializable;

class MarshalledArray implements Serializable {

	final Class<?> type;
	final Object[] elements;

	public MarshalledArray(Class<?> type, Object[] elements) {
		this.type = type;
		this.elements = elements;
	}
	
	private static final long serialVersionUID = 1L;	
}
