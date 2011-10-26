package org.prevayler.bubble;

import infra.logging.LogInfra;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import sneer.foundation.lang.Immutable;
import sneer.foundation.lang.exceptions.NotImplementedYet;

import com.google.common.collect.MapMaker;

public class IdMap implements Serializable {
	
	private Object refToAvoidGc;
	private Map<Object, Long> idsByObject = new MapMaker().weakKeys().concurrencyLevel(1).makeMap(); //Make final after migration;
	private Map<Long, Object> objectsById = new MapMaker().weakValues().concurrencyLevel(1).makeMap(); //Make final after migration;
	private final Map<Object, Long> _idsByObject = new ConcurrentHashMap<Object, Long>();
	private final Map<Long, Object> _objectsById = new ConcurrentHashMap<Long, Object>();
	private long _nextId = 1;

	
	IdMap(Object firstObject) {
		this.refToAvoidGc = firstObject;
		doRegister(firstObject);
	}
	
	
	public void register(Object object) {
		checkInsidePrevalence(object);
		doRegister(object);
	}


	private void doRegister(Object object) {
		if (object == null) throw new IllegalArgumentException();
		if (Proxy.isProxyClass(object.getClass())) throw new IllegalArgumentException();//OPTIMIZE - this can be removed in the future
		
		if (getIdsByObject().containsKey(object))
			throw new IllegalStateException("Object already registered in prevalence map: " + object);
		
		long id = _nextId++;
		getIdsByObject().put(object, id);
		getObjectsById().put(id, object);
	}

	
	public boolean isRegistered(Object object) {
		return getIdsByObject().containsKey(object);
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
		
		if (Proxy.isProxyClass(object.getClass()))
			return new OID(idForProxy(object));
		
		if (object.getClass().isArray()) return marshalArrayIfNecessary(object);
		if (object instanceof Collection) return marshalCollectionIfNecessary((Collection<?>)object);
		
		if (Immutable.isImmutable(object.getClass()))
			return object;
		
		throw new UnsupportedOperationException("Unable to marshal " + object.getClass());
//		return new OID(idFor(object));
	}


	private long idForProxy(Object proxy) {
		BubbleProxy bubble = (BubbleProxy)Proxy.getInvocationHandler(proxy);
		return bubble.id();
	}


	private Object marshalCollectionIfNecessary(Collection<?> collection) {
		if(!(collection instanceof List)) { throw new NotImplementedYet(); }
		List<Object> original = (List<Object>) collection;
		ArrayList<Object> result = new ArrayList<Object>(original.size());
		for(Object element : original) {
			Object marshaled = marshalIfNecessary(element);
			result.add(marshaled);
		}
		return result;
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
		Long result = getIdsByObject().get(object);
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
		if (object == null) return object;
		if(object instanceof OID) return unmarshal(((OID) object)._id);
		if(object instanceof MarshalledArray) return unmarshalTypedArray((MarshalledArray) object);
		if(object instanceof Collection) return  unmarshalCollection((Collection<?>) object);
		if (!Immutable.isImmutable(object.getClass())) throw new IllegalStateException("Mutable " + object.getClass() + " should have been marshaled.");
		return object;
	}


	private Object unmarshalCollection(Collection<?> collection) {
		if(!(collection instanceof List)) throw new NotImplementedYet();
		List<?> list = (List<?>) collection;
		List<Object> result = new ArrayList<Object>(list.size());
		for(Object element : list)
			result.add(unmarshal(element));
		return result;
	}


	private Object unmarshalTypedArray(MarshalledArray array) {
		Object result = Array.newInstance(array.type, array.elements.length);
		for (int i = 0; i < array.elements.length; i++)
			Array.set(result, i, unmarshal(array.elements[i]));
		return result;		
	}


	Object unmarshal(long id) {
		Object result = getObjectsById().get(id);
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


	private Map<Object, Long> getIdsByObject() {
		migrateIfNecessary();
		return idsByObject;
	}


	private Map<Long, Object> getObjectsById() {
		migrateIfNecessary();
		return objectsById;
	}


	private void migrateIfNecessary() {
		if (refToAvoidGc != null) return;
		LogInfra.getLogger(this).log(Level.INFO, "a");
		Object first = unmarshal(1);
		LogInfra.getLogger(this).log(Level.INFO, "b");
		LogInfra.getLogger(this).log(Level.SEVERE, "First object lost. Garbage collection was too fast. :~(");
		if (first == null) throw new Error("First object lost. Garbage collection was too fast. :~(");
		refToAvoidGc = first;
		
		if (idsByObject != null) return;
		idsByObject = new MapMaker().weakKeys().concurrencyLevel(1).makeMap(); //Make final after migration;
		objectsById = new MapMaker().weakValues().concurrencyLevel(1).makeMap(); //Make final after migration;
		idsByObject.putAll(_idsByObject);
		objectsById.putAll(_objectsById);
		_idsByObject.clear();
		_objectsById.clear();
	}
	
}
