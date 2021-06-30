package io.github.thiagolvlsantos.json.predicate;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import io.github.thiagolvlsantos.json.predicate.impl.PredicateFactoryJson;

@RunWith(Parameterized.class)
public class JsonPredicateTests {

	private IPredicateFactory factory = new PredicateFactoryJson();
	private static Map<String, Object> map;

	@Parameter
	public String expression;

	@BeforeClass
	public static void map() {
		map = new HashMap<>();
		map.put("a", 1);
		map.put("b", true);
		map.put("c", "any");
	}

	@Parameters(name = "Expression: {0}")
	public static String[] expressions() {
		return new String[] { //
				"{\"a\":{\"$eq\":1}}", //
				"{\"a\":{\"$ne\":2}}", //
				"{\"b\":{\"$eq\":true}}", //
				"{\"b\":{\"$ne\":false}}", //
				"{\"c\":{\"$contains\":\"any\"}}", //
				"{\"c\":{\"$ncontains\":\"other\"}}", //
				"{\"c\":{\"$match\":\"any\"}}", //
				"{\"c\":{\"$nmatch\":\"her\"}}" //
		};
	}

	@Test
	public void test() {
		Predicate<Object> pred = factory.read(expression.getBytes());
		assertTrue(pred.test(map));
	}
}
