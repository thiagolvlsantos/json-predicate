package io.github.thiagolvlsantos.json.predicate;

import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
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

	private static IPredicateFactory factory = new PredicateFactoryJson();
	private static Map<String, Object> map;

	private static final int DAY = 24 * 60 * 60 * 1000;
	private static Date yesterday1 = new Date(System.currentTimeMillis() - DAY);
	private static Date today1 = new Date();
	private static Date tomorow1 = new Date(System.currentTimeMillis() + DAY);

	private static LocalDate yesterday2 = LocalDate.now().minusDays(1);
	private static LocalDate today2 = LocalDate.now();
	private static LocalDate tomorow2 = LocalDate.now().plusDays(1);

	private static LocalDateTime yesterday3 = LocalDateTime.now().minusDays(1);
	private static LocalDateTime today3 = LocalDateTime.now();
	private static LocalDateTime tomorow3 = LocalDateTime.now().plusDays(1);

	private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static DateTimeFormatter localDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static DateTimeFormatter localDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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
		map.put("k", (short) 0);
		map.put("l", 0L);
		map.put("m", (float) 0.5);
		map.put("n", (double) 1.0);
		map.put("d1", today1);
		map.put("d2", today2);
		map.put("d3", today3);
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
				"{\"k\":{\"$eq\":0}}", //
				"{\"l\":{\"$eq\":0}}", //
				"{\"m\":{\"$eq\":0.5}}", //
				"{\"n\":{\"$eq\":1.0}}", //
				"{\"d1\":{\"$>\":\"" + date.format(yesterday1) + "\"}}", //
				"{\"d2\":{\"$>\":\"" + localDate.format(yesterday2) + "\"}}", //
				"{\"d3\":{\"$>\":\"" + localDateTime.format(yesterday3) + "\"}}", //
				"{\"d1\":{\"$<\":\"" + date.format(tomorow1) + "\"}}", //
				"{\"d2\":{\"$<\":\"" + localDate.format(tomorow2) + "\"}}", //
				"{\"d3\":{\"$<\":\"" + localDateTime.format(tomorow3) + "\"}}", //
				"{\"m\":{\"$<\":\"@n\"}}", //
		};
	}

	@Test
	public void test() {
		Predicate<Object> pred = factory.read(expression.getBytes());
		boolean test = pred.test(map);
		assertTrue(test);
	}
}
