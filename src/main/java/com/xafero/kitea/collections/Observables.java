package com.xafero.kitea.collections;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xafero.kitea.collections.impl.ObservableCollection;
import com.xafero.kitea.collections.impl.ObservableIterable;
import com.xafero.kitea.collections.impl.ObservableList;
import com.xafero.kitea.collections.impl.ObservableMap;
import com.xafero.kitea.collections.impl.ObservableSet;

public final class Observables {

	private Observables() {
	}

	public static <T> ObservableIterable<T> decorate(Iterable<T> iterable) {
		return ObservableIterable.wrap(iterable);
	}

	public static <T> ObservableCollection<T> decorate(Collection<T> collection) {
		return ObservableCollection.wrap(collection);
	}

	public static <T> ObservableSet<T> decorate(Set<T> set) {
		return ObservableSet.wrap(set);
	}

	public static <T> ObservableList<T> decorate(List<T> list) {
		return ObservableList.wrap(list);
	}

	public static <K, V> ObservableMap<K, V> decorate(Map<K, V> map) {
		return ObservableMap.wrap(map);
	}
}