package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;

public class PredicateContains extends AbstractPredicateValue {

	public PredicateContains(String key, JsonNode value, IAccess access) {
		super(key, value, access);
	}

	@Override
	public boolean test(Object t) {
		Object tmp = unwrapp(t);
		return String.valueOf(tmp).contains(value.asText());
	}
}