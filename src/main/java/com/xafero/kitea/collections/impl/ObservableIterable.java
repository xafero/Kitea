package com.xafero.kitea.collections.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationKind;
import com.xafero.kitea.collections.api.ModificationListener;

public class ObservableIterable<T> implements Iterable<T> {

	protected final Iterable<T> iterable;
	protected final List<ModificationListener<T>> listeners;

	public ObservableIterable(Iterable<T> iterable) {
		this.iterable = iterable;
		this.listeners = new LinkedList<ModificationListener<T>>();
	}

	@Override
	public Iterator<T> iterator() {
		return new ObservableIterator(iterable.iterator());
	}

	private class ObservableIterator implements Iterator<T> {

		private final Iterator<T> iterator;
		private T last;

		private ObservableIterator(Iterator<T> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public T next() {
			return last = iterator.next();
		}

		@Override
		public void remove() {
			iterator.remove();
			fireModificationListeners(
					(new ModificationEvent<T>(ObservableIterable.this)).kind(ModificationKind.Remove).oldItem(last));
		}
	}

	protected synchronized void fireModificationListeners(ModificationEvent<T> event) {
		for (ModificationListener<T> listener : listeners)
			listener.onModification(event);
	}

	public synchronized void addModificationListener(ModificationListener<T> listener) {
		listeners.add(listener);
	}

	@SuppressWarnings("unchecked")
	public synchronized ModificationListener<T>[] getModificationListeners() {
		return listeners.toArray(new ModificationListener[listeners.size()]);
	}

	public synchronized void removeModificationListener(ModificationListener<T> listener) {
		listeners.remove(listener);
	}

	public static <E> ObservableIterable<E> wrap(Iterable<E> iterable) {
		return new ObservableIterable<E>(iterable);
	}
}