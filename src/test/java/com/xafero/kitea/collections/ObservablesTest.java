package com.xafero.kitea.collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.xafero.kitea.collections.api.ModificationEvent;
import com.xafero.kitea.collections.api.ModificationKind;
import com.xafero.kitea.collections.impl.ObservableCollection;
import com.xafero.kitea.collections.impl.ObservableIterable;
import com.xafero.kitea.collections.impl.ObservableList;
import com.xafero.kitea.collections.impl.ObservableMap;
import com.xafero.kitea.collections.impl.ObservableSet;

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
	public void testDecorateCollection() throws IOException {
		SimpleModificationListener<String> listener = new SimpleModificationListener<String>();
		Collection<String> collection = new LinkedList<String>(Arrays.asList("Hello", "World"));
		ObservableCollection<String> observe = Observables.decorate(collection);
		assertNotNull(observe);
		observe.addModificationListener(listener);
		// Read-only things
		assertTrue(observe.contains("Hello"));
		assertTrue(observe.containsAll(Arrays.asList("World")));
		assertFalse(observe.isEmpty());
		assertEquals(2, observe.size());
		assertEquals(2, observe.toArray().length);
		assertEquals(2, observe.toArray(new String[0]).length);
		// Write tests
		observe.add("Testme");
		observe.remove("Testme");
		observe.removeAll(Arrays.asList("World"));
		observe.addAll(Arrays.asList("Crazy"));
		observe.retainAll(Arrays.asList("Hello"));
		observe.clear();
		// Check events
		ModificationEvent<String>[] events = listener.getEvents();
		assertEquals(6, events.length);
		// First event
		assertEquals(observe, events[0].getSource());
		assertEquals(ModificationKind.Add, events[0].getKind());
		assertEquals("Testme", events[0].getItem());
		// Second event
		assertEquals(observe, events[1].getSource());
		assertEquals(ModificationKind.Remove, events[1].getKind());
		assertEquals("Testme", events[1].getItem());
		// Third event
		assertEquals(observe, events[2].getSource());
		assertEquals(ModificationKind.Remove, events[2].getKind());
		assertEquals("World", events[2].getItem());
		// Fourth event
		assertEquals(observe, events[3].getSource());
		assertEquals(ModificationKind.Add, events[3].getKind());
		assertEquals("Crazy", events[3].getItem());
		// Fifth event
		assertEquals(observe, events[4].getSource());
		assertEquals(ModificationKind.Remove, events[4].getKind());
		assertEquals("Crazy", events[4].getItem());
		// Sixth event
		assertEquals(observe, events[5].getSource());
		assertEquals(ModificationKind.Remove, events[5].getKind());
		assertEquals("Hello", events[5].getItem());
		// Release resources
		listener.close();
	}

	@Test
	public void testDecorateSet() throws IOException {
		SimpleModificationListener<String> listener = new SimpleModificationListener<String>();
		Set<String> set = new HashSet<String>(Arrays.asList("World"));
		ObservableSet<String> observe = Observables.decorate(set);
		assertNotNull(observe);
		observe.addModificationListener(listener);
		// Write tests
		observe.add("Hello");
		observe.add("Hello");
		observe.addAll(Arrays.asList("Hello"));
		observe.retainAll(Arrays.asList("World"));
		observe.remove("World");
		observe.removeAll(Arrays.asList("World"));
		observe.retainAll(Collections.emptySet());
		// Check events
		ModificationEvent<String>[] events = listener.getEvents();
		assertEquals(3, events.length);
		// First event
		assertEquals(observe, events[0].getSource());
		assertEquals(ModificationKind.Add, events[0].getKind());
		assertEquals("Hello", events[0].getItem());
		// Second event
		assertEquals(observe, events[1].getSource());
		assertEquals(ModificationKind.Remove, events[1].getKind());
		assertEquals("Hello", events[1].getItem());
		// Third event
		assertEquals(observe, events[2].getSource());
		assertEquals(ModificationKind.Remove, events[2].getKind());
		assertEquals("World", events[2].getItem());
		// Release resources
		listener.close();
		// Just print to call toString
		System.out.println(Arrays.toString(events));
		System.out.println(Arrays.toString(ModificationKind.values()));
		assertEquals(ModificationKind.Add, ModificationKind.valueOf("Add"));
	}

	@Test
	public void testDecorateList() throws IOException {
		SimpleModificationListener<String> listener = new SimpleModificationListener<String>();
		List<String> list = new LinkedList<String>(Arrays.asList("Hello", "World"));
		ObservableList<String> observe = Observables.decorate(list);
		assertNotNull(observe);
		observe.addModificationListener(listener);
		// First item
		observe.remove(1);
		observe.set(0, "Crazy");
		assertEquals("Crazy", observe.get(0));
		assertEquals(0, observe.indexOf("Crazy"));
		assertEquals(0, observe.lastIndexOf("Crazy"));
		assertEquals(1, observe.subList(0, 1).size());
		observe.add(0, null);
		observe.set(0, null);
		observe.remove(0);
		observe.addAll(0, Arrays.asList("Testme"));
		// Check events
		ModificationEvent<String>[] events = listener.getEvents();
		assertEquals(4, events.length);
		// First event
		assertEquals(observe, events[0].getSource());
		assertEquals(ModificationKind.Remove, events[0].getKind());
		assertEquals("World", events[0].getItem());
		// Second event
		assertEquals(observe, events[1].getSource());
		assertEquals(ModificationKind.Remove, events[1].getKind());
		assertEquals("Hello", events[1].getItem());
		// Third event
		assertEquals(observe, events[2].getSource());
		assertEquals(ModificationKind.Add, events[2].getKind());
		assertEquals("Crazy", events[2].getItem());
		// Fourth event
		assertEquals(observe, events[3].getSource());
		assertEquals(ModificationKind.Add, events[3].getKind());
		assertEquals("Testme", events[3].getItem());
		// Release resources
		listener.close();
	}

	@Test
	public void testErrors() {
		ObservableList<String> list = Observables.decorate(new LinkedList<String>());
		try {
			list.listIterator();
		} catch (UnsupportedOperationException uoe) {
			System.out.println(uoe.getMessage());
		}
		try {
			list.listIterator(0);
		} catch (UnsupportedOperationException uoe) {
			System.out.println(uoe.getMessage());
		}
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
		observe.put("Crazy", "seeing");
		observe.put("Crazy", "being");
		observe.put("Crazy", null);
		// Check events
		ModificationEvent<Entry<String, String>>[] events = listener.getEvents();
		assertEquals(8, events.length);
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
		// Sixth event
		assertEquals(observe, events[5].getSource());
		assertEquals(ModificationKind.Remove, events[5].getKind());
		assertEquals("Crazy", events[5].getItem().getKey());
		assertEquals("seeing", events[5].getItem().getValue());
		// Seventh event
		assertEquals(observe, events[6].getSource());
		assertEquals(ModificationKind.Add, events[6].getKind());
		assertEquals("Crazy", events[6].getItem().getKey());
		assertEquals("being", events[6].getItem().getValue());
		// Close this
		listener.close();
	}
}