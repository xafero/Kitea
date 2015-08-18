package com.xafero.kitea.collections.impl;

import java.util.Iterator;

public class ObservableIterable<T> implements Iterable<T> {

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

		public ObservableIterator(Iterator<T> iterator) {
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
			System.out.println("FIRE REMOVE: " + last);
		}
	}

	public static <E> ObservableIterable<E> wrap(Iterable<E> iterable) {
		return new ObservableIterable<E>(iterable);
	}
}