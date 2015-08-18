package com.xafero.kitea.collections.impl;

import java.util.Collection;
import java.util.Set;

public class ObservableSet<T> extends ObservableCollection<T>implements Set<T> {

	public ObservableSet(Collection<T> collection) {
		super(collection);
		// TODO Auto-generated constructor stub
	}

	public static <E> ObservableSet<E> wrap(Set<E> set) {
		return new ObservableSet<E>(set);
	}
}