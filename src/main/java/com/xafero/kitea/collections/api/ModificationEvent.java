package com.xafero.kitea.collections.api;

import java.util.EventObject;

public final class ModificationEvent<T> extends EventObject {

	private static final long serialVersionUID = 6005957767623784061L;

	private T item;
	private ModificationKind kind;

	public ModificationEvent(Object source) {
		super(source);
	}

	public T getItem() {
		return item;
	}

	public ModificationKind getKind() {
		return kind;
	}

	public ModificationEvent<T> item(T item) {
		this.item = item;
		return this;
	}

	public ModificationEvent<T> kind(ModificationKind kind) {
		this.kind = kind;
		return this;
	}

	@Override
	public String toString() {
		return "ModificationEvent [" + (item != null ? "oldItem=" + item + ", " : "")
				+ (kind != null ? "kind=" + kind : "") + "]";
	}
}