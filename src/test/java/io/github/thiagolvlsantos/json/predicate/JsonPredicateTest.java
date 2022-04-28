package io.github.thiagolvlsantos.json.predicate;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
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
public class JsonPredicateTest {

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
		map.put("d", new String("Testing byte array!").getBytes());
		map.put("e", Arrays.asList("a", "b", "c"));
		map.put("f", Arrays.asList(1, 2, 3));
		map.put("g", new String[] { "a", "b", "c" });
		map.put("h", new int[] { 1, 2, 3 });
		map.put("i", new int[][] { new int[] { 1, 2 }, new int[] { 3, 4 } });
		map.put("j", new String[][] { new String[] { "alfa", "beta" }, new String[] { "gama", "teta" } });
		System.out.println("MAP:" + map);
	}

	@Parameters(name = "Expression: {0}")
	public static String[] expressions() {
		return new String[] { //
				"{\"a\":{\"$eq\":1}}", //
				"{\"a\":{\"$ne\":2}}", //
				"{\"$not\": {\"a\":{\"$ne\":1}}}", //
				"{\"a\":{\"$lt\":2}}", //
				"{\"a\":{\"$le\":1}}", //
				"{\"a\":{\"$gt\":0}}", //
				"{\"a\":{\"$ge\":1}}", //
				"{\"b\":{\"$eq\":true}}", //
				"{\"b\":{\"$ne\":false}}", //
				"{\"c\":{\"$contains\":\"any\"}}", //
				"{\"c\":{\"$ncontains\":\"other\"}}", //
				"{\"c\":{\"$match\":\"any\"}}", //
				"{\"c\":{\"$nmatch\":\"her\"}}", //
				"{\"$and\": [ {\"b\": {\"$eq\": true} }, {\"a\":{\"$eq\":1}} ] }", //
				"{\"$or\": [ {\"a\":{\"$eq\":1}}, {\"b\":{\"$eq\": false} } ] }", //
				"{\"d\":{\"$contains\":\"array\"}}", //
				"{\"e\":{\"$contains\":\"a\"}}", //
				"{\"f\":{\"$contains\":\"1\"}}", //
				"{\"g\":{\"$contains\":\"b\"}}", //
				"{\"h\":{\"$contains\":\"2\"}}", //
				"{\"i\":{\"$contains\":\"4\"}}", //
				"{\"j\":{\"$contains\":\"am\"}}", //
				"{\"j.[0]\":{\"$contains\":\"beta\"}}", //
				"{\"j.[0]\":{\"$ncontains\":\"gama\"}}", //
				"{\"j.[0].[0]\":{\"$eq\":\"alfa\"}}", //
				"{\"j.[0].[1]\":{\"$ne\":\"alfa\"}}", //
		};
	}

	@Test
	public void test() {
		Predicate<Object> pred = factory.read(expression.getBytes());
		boolean test = pred.test(map);
		assertTrue(test);
	}
}
