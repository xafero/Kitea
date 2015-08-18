package com.xafero.kitea.collections.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationKind;

public class ObservableList<T> extends ObservableCollection<T> implements List<T> {

	protected final List<T> list;

	public ObservableList(List<T> list) {
		super(list);
		this.list = list;
	}

	@Override
	public void add(int index, T element) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T remove(int index) {
		T removed = list.remove(index);
		if (removed != null)
			fireModificationListeners((new ModificationEvent<T>(this)).kind(ModificationKind.Remove).item(removed));
		return removed;
	}

	@Override
	public T set(int index, T element) {
		T replaced = list.set(index, element);
		if (replaced != null)
			fireModificationListeners((new ModificationEvent<T>(this)).kind(ModificationKind.Remove).item(replaced));
		if (element != null)
			fireModificationListeners((new ModificationEvent<T>(this)).kind(ModificationKind.Add).item(element));
		return replaced;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return Collections.unmodifiableList(list.subList(fromIndex, toIndex));
	}

	public static <E> ObservableList<E> wrap(List<E> list) {
		return new ObservableList<E>(list);
	}
}