package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;

public class PredicateLowerEquals extends AbstractPredicateValue {

	private PredicateLower lower;
	private PredicateEquals equals;

	public PredicateLowerEquals(String key, JsonNode value, IAccess access) {
		super(key, value, access);
		lower = new PredicateLower(key, value, access);
		equals = new PredicateEquals(key, value, access);
	}

	@Override
	public boolean test(Object t) {
		return lower.test(t) || equals.test(t);
	}
}