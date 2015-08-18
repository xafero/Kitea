package com.xafero.kitea.collections;

import java.io.Closeable;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationListener;

public class SimpleModificationListener<T> implements ModificationListener<T>, Closeable {

	private final Queue<ModificationEvent<T>> queue;

	public SimpleModificationListener() {
		this.queue = new LinkedList<ModificationEvent<T>>();
	}

	@Override
	public void onModification(ModificationEvent<T> e) {
		queue.offer(e);
	}

	@SuppressWarnings("unchecked")
	public ModificationEvent<T>[] getEvents() {
		return queue.toArray(new ModificationEvent[queue.size()]);
	}

	@Override
	public void close() throws IOException {
		queue.clear();
	}
}