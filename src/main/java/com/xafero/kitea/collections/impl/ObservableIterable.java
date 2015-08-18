package com.xafero.kitea.collections.impl;

import java.util.Iterator;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationKind;

public class ObservableIterable<T> extends ObservableContainer<T> implements Iterable<T> {

	protected final Iterable<T> iterable;

	public ObservableIterable(Iterable<T> iterable) {
		this.iterable = iterable;
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
					(new ModificationEvent<T>(ObservableIterable.this)).kind(ModificationKind.Remove).item(last));
		}
	}

	public static <E> ObservableIterable<E> wrap(Iterable<E> iterable) {
		return new ObservableIterable<E>(iterable);
	}
}