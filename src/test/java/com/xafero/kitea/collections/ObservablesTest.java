package com.xafero.kitea.collections;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xafero.kitea.collections.impl.*;
import com.xafero.kitea.collections.impl.ObservableIterable;

public class ObservablesTest {

	@Test
	public void testDecorateIterable() {
		Iterable<String> iterable = new LinkedList<String>(Arrays.asList("Hello", "World"));
		ObservableIterable<String> observe = Observables.decorate(iterable);
		assertNotNull(observe);
	}

	@Test
	public void testDecorateCollection() {
		Collection<String> collection = new LinkedList<String>(Arrays.asList("Hello", "World"));
		ObservableCollection<String> observe = Observables.decorate(collection);
		assertNotNull(observe);
	}

	@Test
	public void testDecorateSet() {
		Set<String> set = new HashSet<String>(Arrays.asList("Hello", "World"));
		ObservableSet<String> observe = Observables.decorate(set);
		assertNotNull(observe);
	}

	@Test
	public void testDecorateList() {
		List<String> list = new LinkedList<String>(Arrays.asList("Hello", "World"));
		ObservableList<String> observe = Observables.decorate(list);
		assertNotNull(observe);
	}

	@Test
	public void testDecorateMap() {
		Map<String, String> map = new HashMap<String, String>();
		ObservableMap<String, String> observe = Observables.decorate(map);
		assertNotNull(observe);
	}
}