package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;
import io.github.thiagolvlsantos.json.predicate.value.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.IConverter;

public class PredicateLowerEquals extends AbstractPredicateValue {

	private PredicateLower lower;
	private PredicateEquals equals;

	public PredicateLowerEquals(String key, IAccess access, JsonNode value, IConverter converter) {
		super(key, access, value, converter);
		lower = new PredicateLower(key, access, value, converter);
		equals = new PredicateEquals(key, access, value, converter);
	}

	@Override
	public boolean test(Object t) {
		return lower.test(t) || equals.test(t);
	}
}