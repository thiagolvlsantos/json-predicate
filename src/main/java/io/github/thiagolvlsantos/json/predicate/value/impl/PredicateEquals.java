package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;
import io.github.thiagolvlsantos.json.predicate.value.IAccess;

public class PredicateEquals extends AbstractPredicateValue {

	public PredicateEquals(String key, JsonNode value, IAccess access) {
		super(key, value, access);
	}

	@Override
	public boolean test(Object t) {
		Object tmp = unwrapp(t);
		return String.valueOf(tmp).equals(value.asText());
	}
}