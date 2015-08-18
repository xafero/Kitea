package com.xafero.kitea.collections.impl;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationKind;

public class ObservableMap<K, V> extends ObservableContainer<Entry<K, V>> implements Map<K, V> {

	private final Map<K, V> map;

	public ObservableMap(Map<K, V> map) {
		this.map = map;
	}

	@Override
	public void clear() {
		// Make a copy of current values
		Set<Entry<K, V>> toClear = map.entrySet();
		@SuppressWarnings("unchecked")
		Entry<K, V>[] copy = toClear.toArray(new Entry[toClear.size()]);
		// Then clear the original map
		map.clear();
		// Fire removals
		for (Entry<K, V> entry : copy)
			fireModificationListeners(
					(new ModificationEvent<Entry<K, V>>(this)).kind(ModificationKind.Remove).item(entry));
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return Collections.unmodifiableSet(map.entrySet());
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return Collections.unmodifiableSet(map.keySet());
	}

	@Override
	public V put(K key, V value) {
		V copy = map.put(key, value);
		if (copy != null)
			fireModificationListeners((new ModificationEvent<Entry<K, V>>(this)).kind(ModificationKind.Remove)
					.item(new SimpleImmutableEntry<K, V>(key, copy)));
		if (value != null)
			fireModificationListeners((new ModificationEvent<Entry<K, V>>(this)).kind(ModificationKind.Add)
					.item(new SimpleImmutableEntry<K, V>(key, value)));
		return copy;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> newMap) {
		for (Entry<? extends K, ? extends V> e : newMap.entrySet())
			put(e.getKey(), e.getValue());
	}

	@SuppressWarnings("unchecked")
	@Override
	public V remove(Object key) {
		V copy = map.remove(key);
		fireModificationListeners((new ModificationEvent<Entry<K, V>>(this)).kind(ModificationKind.Remove)
				.item(new SimpleImmutableEntry<K, V>((K) key, copy)));
		return copy;
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<V> values() {
		return Collections.unmodifiableCollection(map.values());
	}

	public static <K, V> ObservableMap<K, V> wrap(Map<K, V> map) {
		return new ObservableMap<K, V>(map);
	}
}