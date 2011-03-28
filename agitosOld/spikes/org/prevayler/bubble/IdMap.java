package org.prevayler.bubble;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sneer.foundation.lang.Immutable;

public class IdMap {
	
	private final Map<Object, Long> _idsByObject = new ConcurrentHashMap<Object, Long>();
	private final Map<Long, Object> _objectsById = new ConcurrentHashMap<Long, Object>();
	private long _nextId = 1;

	
	IdMap() {} //Not public
	
	
	public void register(Object object) {
		checkInsidePrevalence(object);
		doRegister(object);
	}


	private void doRegister(Object object) {
		if (object == null) throw new IllegalArgumentException();
		
		if (_idsByObject.containsKey(object))
			throw new IllegalStateException("Object already registered in prevalence map: " + object);
		
		long id = _nextId++;
		_idsByObject.put(object, id);
		_objectsById.put(id, object);
	}

	
	public boolean isRegistered(Object object) {
		return _idsByObject.containsKey(object);
	}

	
	private void checkInsidePrevalence(Object object) {
		if (!PrevalenceFlag.isInsidePrevalence())
			throw new IllegalStateException("Trying to register object '" + object + "' outside prevalent environment.");
	}

	
	Object[] marshal(Object[] array) {
		if (array == null)
			return null;
		
		Object[] result = new Object[array.length]; 
		for (int i = 0; i < result.length; i++)
			result[i] = marshalIfNecessary(array[i]);
		return result;
	}
	
	
	private Object marshalIfNecessary(Object object) {
		if (object == null) return null;
		
		Long id = _idsByObject.get(object);
		if (id != null)
			return new OID(id);
		
		if (requiresRegistration(object))
			throw new IllegalStateException("Mutable object " + object + " should have been registered in prevalence map.");
		return object;
	}


	long marshal(Object object) {
		Long result = _idsByObject.get(object);
		if (result == null)
			throw new IllegalStateException("Id not found for object: " + object);
		return result;
	}
	
	
	Object[] unmarshal(Object[] array) {
		if (array == null)
			return null;

		Object[] result = new Object[array.length]; 
		for (int i = 0; i < array.length; i++)
			result[i] = unmarshal(array[i]);
		return result;
	}

	
	Object unmarshal(Object object) {
		return object instanceof OID
			? unmarshal(((OID)object)._id)
			: object;
	}

	
	Object unmarshal(long id) {
		Object result = _objectsById.get(id);
		if (result == null)
			throw new IllegalStateException("Object not found for id: " + id);
		return result;
	}


	boolean requiresRegistration(Object object) {
		if (object == null) return false;

		Class<?> type = object.getClass();
		if (type.isArray()) return false;
		if (Collection.class.isAssignableFrom(type)) return false;
		if (Immutable.isImmutable(type)) return false;
		
		return true;
	}


	void registerFirstObject(Object object) {
		if (!_objectsById.isEmpty()) throw new IllegalStateException();
		doRegister(object);
	}
	
}
