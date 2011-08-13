package org.prevayler.bubble;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sneer.foundation.lang.Immutable;
import sneer.foundation.lang.exceptions.NotImplementedYet;

public class IdMap implements Serializable {
	
	private final Map<Object, Long> _idsByObject = new ConcurrentHashMap<Object, Long>();
	private final Map<Long, Object> _objectsById = new ConcurrentHashMap<Long, Object>();
	private long _nextId = 1;

	
	IdMap(Object firstObject) {
		doRegister(firstObject);
	}
	
	
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

	
	void marshal(Object[] array) {
		if (array == null) return;
		
		for (int i = 0; i < array.length; i++)
			array[i] = marshalIfNecessary(array[i]);
	}
	
	
	private Object marshalIfNecessary(Object object) {
		if (object == null) return null;
		if (object.getClass().isArray()) return marshalArrayIfNecessary(object);
		if (object instanceof Collection) return marshalCollectionIfNecessary((Collection<?>)object);
		
		if (Immutable.isImmutable(object.getClass()))
			return object;
		
		return new OID(idFor(object));
	}


	private Object marshalCollectionIfNecessary(Collection<?> collection) {
		throw new NotImplementedYet();
	}


	private Object marshalArrayIfNecessary(Object array) {
		Class<?> type = array.getClass().getComponentType();
		if (Immutable.isImmutable(type))
			return array;
		
		if (type == Object.class) {
			marshal((Object[]) array);
			return array;
		}
		
		return marshallTypedArray(array, type);
	}


	private Object marshallTypedArray(Object array, Class<?> type) {
		Object[] copy = new Object[Array.getLength(array)];
		for (int i = 0; i < copy.length; i++) {
			Object element = Array.get(array, i);
			copy[i] = marshalIfNecessary(element);			
		}
		return new MarshalledArray(type, copy);
	}


	long idFor(Object object) {
		Long result = _idsByObject.get(object);
		if (result == null)
			throw new IllegalStateException("Mutable object " + object + " should have been registered in prevalence map.");
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
			: object instanceof MarshalledArray
				? unmarshalTypedArray((MarshalledArray)object)
				: object;
	}

	
	private Object unmarshalTypedArray(MarshalledArray array) {
		Object result = Array.newInstance(array.type, array.elements.length);
		for (int i = 0; i < array.elements.length; i++)
			Array.set(result, i, unmarshal(array.elements[i]));
		return result;		
	}


	Object unmarshal(long id) {
		Object result = _objectsById.get(id);
		if (result == null)
			throw new IllegalStateException("Object not found for id: " + id);
		return result;
	}


	public void registerIfNecessary(Object object) {
		if (object == null) return;
		if (Immutable.isImmutable(object.getClass())) return;
		if (PrevalentBubble.idMap().isRegistered(object)) return;
		PrevalentBubble.idMap().register(object);
	}


}
