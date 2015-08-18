package com.xafero.kitea.collections.impl;

import java.util.LinkedList;
import java.util.List;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationListener;

public abstract class ObservableContainer<T> {

	protected final List<ModificationListener<T>> listeners = new LinkedList<ModificationListener<T>>();

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
}