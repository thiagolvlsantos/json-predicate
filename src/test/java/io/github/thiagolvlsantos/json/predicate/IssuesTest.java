package io.github.thiagolvlsantos.json.predicate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.Assert;
import org.junit.Test;

import io.github.thiagolvlsantos.json.predicate.impl.PredicateFactoryJson;

public class IssuesTest {

	// Issue: #1
	@Test
	public void testSimpleMatch() throws Exception {
		PredicateFactoryJson factory = new PredicateFactoryJson();
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
		mapData.put("source", "weather2");
		pred = factory.read(rule.getBytes());
		Assert.assertTrue(pred.test(mapData));
		mapData.put("source", "weather3");
		Assert.assertTrue(pred.test(mapData));
		mapData.put("source", "anyvalue");
		Assert.assertFalse(pred.test(mapData));
	}
	@Test
	public void testEqualsWithoutOpMatch() throws Exception {
		PredicateFactoryJson factory = new PredicateFactoryJson();
		Map<String, Object> mapData = new HashMap<>();
		String rule ="{\"source\":\"weather2\"}";
                // positive test
		mapData.put("source", "weather2");
		Predicate<Object> pred = factory.read(rule.getBytes());
		Assert.assertTrue(pred.test(mapData));
		// negative test
		mapData.put("source", "weather3");
		Assert.assertFalse(pred.test(mapData));
	}
}
