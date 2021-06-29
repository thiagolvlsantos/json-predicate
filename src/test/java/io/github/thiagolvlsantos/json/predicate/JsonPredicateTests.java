package io.github.thiagolvlsantos.json.predicate;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.BeforeClass;
import org.junit.Test;

import io.github.thiagolvlsantos.json.predicate.impl.PredicateFactoryJson;

public class JsonPredicateTests {

	private IPredicateFactory factory = new PredicateFactoryJson();
	private static Map<String, Object> map;

	@BeforeClass
	public static void map() {
		map = new HashMap<>();
		map.put("a", 1);
		map.put("b", true);
		map.put("c", "any");
	}

	@Test
	public void testEquals() {
		Predicate<Object> pred = factory.read("{\"a\":{\"$eq\":1}}".getBytes());
		assertTrue(pred.test(map));
	}

	@Test
	public void testNotEquals() {
		Predicate<Object> pred = factory.read("{\"a\":{\"$ne\":2}}".getBytes());
		assertTrue(pred.test(map));
	}

	@Test
	public void testTrue() {
		Predicate<Object> pred = factory.read("{\"b\":{\"$eq\":true}}".getBytes());
		assertTrue(pred.test(map));
	}

	@Test
	public void testFalse() {
		Predicate<Object> pred = factory.read("{\"b\":{\"$ne\":false}}".getBytes());
		assertTrue(pred.test(map));
	}

	@Test
	public void testContains() {
		Predicate<Object> pred = factory.read("{\"c\":{\"$contains\":\"any\"}}".getBytes());
		assertTrue(pred.test(map));
	}

	@Test
	public void testNotContains() {
		Predicate<Object> pred = factory.read("{\"c\":{\"$ncontains\":\"other\"}}".getBytes());
		assertTrue(pred.test(map));
	}

	@Test
	public void testMatch() {
		Predicate<Object> pred = factory.read("{\"c\":{\"$match\":\"any\"}}".getBytes());
		assertTrue(pred.test(map));
	}

	@Test
	public void testNotMatch() {
		Predicate<Object> pred = factory.read("{\"c\":{\"$nmatch\":\"her\"}}".getBytes());
		assertTrue(pred.test(map));
	}
}
