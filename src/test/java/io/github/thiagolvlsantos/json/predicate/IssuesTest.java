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
		String rule = "{\n" + " \"$and\": [\n" + " {\n" + " \"source\": {\"$eq\":\"weather2\"}\n" + " }\n" + " ]\n"
				+ "}";
		System.out.println(rule);
		Map<String, Object> mapData = new HashMap<>();
		mapData.put("source", "weather2");
		Predicate<Object> pred = new PredicateFactoryJson().read(rule.getBytes());
		boolean test = pred.test(mapData);
		Assert.assertTrue(test);
	}
}
