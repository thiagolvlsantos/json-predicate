package io.github.thiagolvlsantos.json.predicate;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.Parameterized.Parameter;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DeserializerTest {

	private static List<Map<String, Object>> projects;

	@Parameter
	public String expression;

	@BeforeClass
	public static void map() {
		projects = new LinkedList<>();

		Map<String, Object> map = new HashMap<>();
		map.put("name", "projectAlfa");
		projects.add(map);

		map = new HashMap<>();
		map.put("name", "projectBeta");
		projects.add(map);

		System.out.println("PROJECTS:" + projects);
	}

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Rule rule = mapper.readValue(Files.readAllBytes(Paths.get("src/test/resources/example_rule.json")), Rule.class);
		List<Map<String, Object>> filtered = projects.stream().filter(p -> rule.getCondition().test(p))
				.collect(Collectors.toList());
		assertEquals(1, filtered.size());
		assertEquals("projectAlfa", filtered.get(0).get("name"));
	}

	@Test
	public void testRepeat() throws Exception {
		test();
	}
}
