package io.github.thiagolvlsantos.json.predicate.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import io.github.thiagolvlsantos.git.commons.properties.PropertyUtils;
import io.github.thiagolvlsantos.json.predicate.IPredicate;
import io.github.thiagolvlsantos.json.predicate.IPredicateManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PredicateManagerDefault implements IPredicateManager {

	private Map<String, Class<? extends IPredicate>> map = new LinkedHashMap<>();

	@SuppressWarnings("unchecked")
	@SneakyThrows
	public PredicateManagerDefault() {
		Properties p = PropertyUtils.merged("json-predicate.properties");
		for (Entry<Object, Object> e : p.entrySet()) {
			String[] ops = String.valueOf(e.getKey()).split(",");
			Class<?> type = Class.forName(String.valueOf(e.getValue()));
			for (String op : ops) {
				log.trace("OP:{}, CLASS:{}", op, type);
				map.put(op, (Class<? extends IPredicate>) type);
			}
		}
		log.info("OPS:{}", map);
	}

	@Override
	public void put(String id, Class<? extends IPredicate> clazz) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null.");
		}
		map.put(id.toLowerCase(), clazz);
	}

	@Override
	public Class<? extends IPredicate> get(String id) {
		if (id == null) {
			return null;
		}
		return map.get(id.toLowerCase());
	}
}