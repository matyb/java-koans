package com.sandwich.util;

import java.util.Map.Entry;

/**
 * Why on earth this wasn't around when JDK5 was released is just as bad as
 * string.isEmpty()'s absence jeesh!
 * 
 * @param <K>
 * @param <V>
 */
public class SimpleEntry<K,V> implements Entry<K,V>{

	private K k;
	private V v;
	
	public SimpleEntry(K k, V v){
		this.k = k;
		this.v = v;
	}
	
	public K getKey() {
		return k;
	}

	public V getValue() {
		return v;
	}

	public V setValue(V value) {
		V tmp = v;
		v = value;
		return tmp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((k == null) ? 0 : k.hashCode());
		result = prime * result + ((v == null) ? 0 : v.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if(!Entry.class.isAssignableFrom(SimpleEntry.class)){
			return false;
		}
		Entry<?,?> other = (Entry<?,?>) obj;
		if (k == null) {
			if (other.getKey() != null)
				return false;
		} else if (!k.equals(other.getKey()))
			return false;
		if (v == null) {
			if (other.getValue() != null)
				return false;
		} else if (!v.equals(other.getValue()))
			return false;
		return true;
	}
	
}
