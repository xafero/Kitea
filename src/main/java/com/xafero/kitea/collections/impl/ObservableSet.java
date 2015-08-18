package com.xafero.kitea.collections.impl;

import java.util.Set;

public class ObservableSet<T> extends ObservableCollection<T> implements Set<T> {

	public ObservableSet(Set<T> set) {
		super(set);
	}

	public static <E> ObservableSet<E> wrap(Set<E> set) {
		return new ObservableSet<E>(set);
	}
}