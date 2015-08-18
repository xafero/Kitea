package com.xafero.kitea.collections;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationKind;
import com.xafero.kitea.collections.impl.*;
import com.xafero.kitea.collections.impl.ObservableIterable;

public class ObservablesTest {

	@Test
	public void testDecorateIterable() {
		SimpleModificationListener<String> listener = new SimpleModificationListener<String>();
		// Create enumerable
		Iterable<String> iterable = new LinkedList<String>(Arrays.asList("Hello", "World"));
		ObservableIterable<String> observe = Observables.decorate(iterable);
		assertNotNull(observe);
		observe.addModificationListener(listener);
		// First item
		Iterator<String> it = observe.iterator();
		assertTrue(it.hasNext());
		assertEquals("Hello", it.next());
		it.remove();
		assertEquals(1, ((Collection<String>) iterable).size());
		// Second item
		assertTrue(it.hasNext());
		assertEquals("World", it.next());
		it.remove();
		assertTrue(((Collection<String>) iterable).isEmpty());
		// Check listener
		ModificationEvent<String>[] events = listener.getEvents();
		assertEquals(2, events.length);
		assertEquals(observe, events[0].getSource());
		assertEquals(ModificationKind.Remove, events[0].getKind());
		assertEquals("Hello", events[0].getOldItem());
		assertEquals("World", events[1].getOldItem());
		// Some tests about listeners
		assertEquals(listener, observe.getModificationListeners()[0]);
		observe.removeModificationListener(listener);
		assertEquals(0, observe.getModificationListeners().length);
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
		// First item
		assertEquals("World", list.remove(1));
	}

	@Test
	public void testDecorateMap() {
		Map<String, String> map = new HashMap<String, String>();
		ObservableMap<String, String> observe = Observables.decorate(map);
		assertNotNull(observe);
	}
}