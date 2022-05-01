package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;
import io.github.thiagolvlsantos.json.predicate.value.IAccess;

public class PredicateGreater extends AbstractPredicateValue {

	public PredicateGreater(String key, JsonNode value, IAccess access) {
		super(key, value, access);
	}

	@Override
	public boolean test(Object t) {
		Object tmp = unwrapp(t);
		if (tmp instanceof Short) {
			return ((Number) tmp).shortValue() > value.asInt();
		} else if (tmp instanceof Integer) {
			return ((Number) tmp).intValue() > value.asInt();
		} else if (tmp instanceof Long) {
			return ((Number) tmp).longValue() > value.asLong();
		} else if (tmp instanceof Float) {
			return ((Number) tmp).floatValue() > value.asDouble();
		} else if (tmp instanceof Double) {
			return ((Number) tmp).doubleValue() > value.asDouble();
		}
		return String.valueOf(tmp).compareTo(value.asText()) > 0;
	}
}