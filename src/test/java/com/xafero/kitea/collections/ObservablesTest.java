package com.xafero.kitea.collections;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationKind;
import com.xafero.kitea.collections.impl.*;
import com.xafero.kitea.collections.impl.ObservableIterable;
import java.util.Map.*;
import java.util.AbstractMap.*;

public class ObservablesTest {

	@Test
	public void testDecorateIterable() throws IOException {
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
		assertEquals("Hello", events[0].getItem());
		assertEquals("World", events[1].getItem());
		// Some tests about listeners
		assertEquals(listener, observe.getModificationListeners()[0]);
		observe.removeModificationListener(listener);
		assertEquals(0, observe.getModificationListeners().length);
		listener.close();
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
	public void testDecorateMap() throws IOException {
		SimpleModificationListener<Entry<String, String>> listener = new SimpleModificationListener<Entry<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		ObservableMap<String, String> observe = Observables.decorate(map);
		assertNotNull(observe);
		observe.addModificationListener(listener);
		// Do something
		assertTrue(observe.isEmpty());
		observe.put("Hi", "There");
		assertEquals(1, observe.size());
		assertEquals(1, observe.values().size());
		assertEquals(1, observe.keySet().size());
		assertEquals(1, observe.entrySet().size());
		assertTrue(observe.containsKey("Hi"));
		assertTrue(observe.containsValue("There"));
		assertEquals("There", observe.get("Hi"));
		observe.remove("Hi");
		observe.putAll(Observables.toMap(new String[] { "TestMe" }, new String[] { "is good?" }));
		observe.clear();
		// Check events
		ModificationEvent<Entry<String, String>>[] events = listener.getEvents();
		assertEquals(4, events.length);
		// First event
		assertEquals(observe, events[0].getSource());
		assertEquals(ModificationKind.Add, events[0].getKind());
		assertEquals("Hi", events[0].getItem().getKey());
		assertEquals("There", events[0].getItem().getValue());
		// Second event
		assertEquals(observe, events[1].getSource());
		assertEquals(ModificationKind.Remove, events[1].getKind());
		assertEquals("Hi", events[1].getItem().getKey());
		assertEquals("There", events[1].getItem().getValue());
		// Third event
		assertEquals(observe, events[2].getSource());
		assertEquals(ModificationKind.Add, events[2].getKind());
		assertEquals("TestMe", events[2].getItem().getKey());
		assertEquals("is good?", events[2].getItem().getValue());
		// Fourth event
		assertEquals(observe, events[3].getSource());
		assertEquals(ModificationKind.Remove, events[3].getKind());
		assertEquals("TestMe", events[3].getItem().getKey());
		assertEquals("is good?", events[3].getItem().getValue());
		// Close this
		listener.close();
	}
}