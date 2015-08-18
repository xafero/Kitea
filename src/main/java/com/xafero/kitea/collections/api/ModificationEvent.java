package com.xafero.kitea.collections.api;

import java.util.EventObject;

public final class ModificationEvent<T> extends EventObject {

	private static final long serialVersionUID = 6005957767623784061L;

	private T oldItem;
	private ModificationKind kind;

	public ModificationEvent(Object source) {
		super(source);
	}

	public T getOldItem() {
		return oldItem;
	}

	public ModificationKind getKind() {
		return kind;
	}

	public ModificationEvent<T> oldItem(T oldItem) {
		this.oldItem = oldItem;
		return this;
	}

	public ModificationEvent<T> kind(ModificationKind kind) {
		this.kind = kind;
		return this;
	}

	@Override
	public String toString() {
		return "ModificationEvent [" + (oldItem != null ? "oldItem=" + oldItem + ", " : "")
				+ (kind != null ? "kind=" + kind : "") + "]";
	}
}