package com.xafero.kitea.collections.api;

import java.util.EventListener;

public interface ModificationListener<T> extends EventListener {

	void onModification(ModificationEvent<T> e);

}