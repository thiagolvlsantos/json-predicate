package io.github.thiagolvlsantos.json.predicate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import io.github.thiagolvlsantos.json.predicate.exceptions.JsonPredicateException;
import io.github.thiagolvlsantos.json.predicate.impl.PredicateFactoryJson;

public class IssuesTest {

	private PredicateFactoryJson factory = new PredicateFactoryJson();

	// Issue: #1
	@Test
	public void testSimpleMatch() throws Exception {
		Map<String, Object> mapData = new HashMap<>();
		String rule = null;

		// exact match
		rule = "{\n" + " \"$and\": [\n" + " {\n" + " \"source\": {\"$eq\":\"weather2\"}\n" + " }\n" + " ]\n" + "}";
		Predicate<Object> pred = factory.read(rule.getBytes());
		// positive test
		mapData.put("source", "weather2");
		Assert.assertTrue(pred.test(mapData));
		// negative test
		mapData.put("source", "weather3");
		Assert.assertFalse(pred.test(mapData));

		// example with contains 'weather'
		rule = "{\n" + " \"$and\": [\n" + " {\n" + " \"source\": {\"$contains\":\"weather\"}\n" + " }\n" + " ]\n" + "}";
		pred = factory.read(rule.getBytes());

		mapData.put("source", "weather2");
		Assert.assertTrue(pred.test(mapData));

		mapData.put("source", "weather3");
		Assert.assertTrue(pred.test(mapData));

		mapData.put("source", "anyvalue");
		Assert.assertFalse(pred.test(mapData));
	}

	// Syntatic suggar
	@Test
	public void testEqualsWithoutOpMatch() throws Exception {
		String rule = "{\"source\":\"weather2\"}";
		Predicate<Object> pred = factory.read(rule.getBytes());

		Map<String, Object> mapData = new HashMap<>();
		// positive test
		mapData.put("source", "weather2");
		Assert.assertTrue(pred.test(mapData));
		// negative test
		mapData.put("source", "weather3");
		Assert.assertFalse(pred.test(mapData));
	}

	// Issue #2
	@Test
	public void testMemberOfEmptyArray() throws Exception {
		String rule = "{\n" + " \"$and\": [\n" + " {\n" + " \"str\": {\n" + " \"$memberOf\": []\n" + " }\n" + " }\n"
				+ " ]\n" + "}";
		Predicate<Object> pred = factory.read(rule.getBytes());

		Map<String, Object> mapData = new HashMap<>();

		mapData.put("str", "ab");
		Assert.assertFalse(pred.test(mapData));
	}

	@Test
	public void testMemberOfEmptyList() throws Exception {
		String rule = "{\n" + " \"$and\": [\n" + " {\n" + " \"str\": {\n" + " \"$memberOf\": \"@mylist\"\n" + " }\n"
				+ " }\n" + " ]\n" + "}";
		Predicate<Object> pred = factory.read(rule.getBytes());

		Map<String, Object> mapData = new HashMap<>();
		mapData.put("mylist", new LinkedList<>());

		mapData.put("str", "ab");
		Assert.assertFalse(pred.test(mapData));
	}

	@Test
	public void testMemberOfInArray() throws Exception {
		String rule = "{\n" + " \"$and\": [\n" + " {\n" + " \"str\": {\n" + " \"$memberOf\": [\n" + " \"abc\",\n"
				+ " \"bc\",\n" + " \"ef\"\n" + " ]\n" + " }\n" + " }\n" + " ]\n" + "}";
		Predicate<Object> pred = factory.read(rule.getBytes());

		Map<String, Object> mapData = new HashMap<>();
		// positive test
		mapData.put("str", "abc");
		Assert.assertTrue(pred.test(mapData));

		// negative test
		mapData.put("str", "ab");
		Assert.assertFalse(pred.test(mapData));
	}

	@Test
	public void testMemberOfInList() throws Exception {
		String rule = "{\n" + " \"$and\": [\n" + " {\n" + " \"str\": {\n" + " \"$memberOf\": \"@mylist\"\n" + " }\n"
				+ " }\n" + " ]\n" + "}";
		Predicate<Object> pred = factory.read(rule.getBytes());

		Map<String, Object> mapData = new HashMap<>();
		mapData.put("mylist", Arrays.asList("abc", "bc", "ef"));

		// positive test
		mapData.put("str", "abc");
		Assert.assertTrue(pred.test(mapData));

		// negative test
		mapData.put("str", "ab");
		Assert.assertFalse(pred.test(mapData));
	}

	@Test
	public void testMemberOfNumbers() throws Exception {
		String rule = "{\n" + " \"$and\": [\n" + " {\n" + " \"str\": {\n" + " \"$memberOf\": [1,3,5]\n" + " }\n"
				+ " }\n" + " ]\n" + "}";
		Predicate<Object> pred = factory.read(rule.getBytes());

		Map<String, Object> mapData = new HashMap<>();

		// positive test
		mapData.put("str", Integer.valueOf(3));
		Assert.assertTrue(pred.test(mapData));

		// negative test
		mapData.put("str", Integer.valueOf(2));
		Assert.assertFalse(pred.test(mapData));
	}

	@org.junit.Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testMemberOfInvalidValue() throws Exception {
		String rule = "{\n" + " \"$and\": [\n" + " {\n" + " \"str\": {\n" + " \"$memberOf\": \"anyvalue\"\n" + " }\n"
				+ " }\n" + " ]\n" + "}";
		Predicate<Object> pred = factory.read(rule.getBytes());

		Map<String, Object> mapData = new HashMap<>();
		mapData.put("str", "abc");

		thrown.expect(JsonPredicateException.class);
		thrown.expectMessage(
				"Value is neither a Collection nor an Array. Received: 'anyvalue' of type java.lang.String");
		pred.test(mapData);
	}
}
