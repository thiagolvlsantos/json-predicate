package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;

public class PredicateGreaterEquals extends AbstractPredicateValue {

	private PredicateGreater greater;
	private PredicateEquals equals;

	public PredicateGreaterEquals(String key, JsonNode value, IAccess access) {
		super(key, value, access);
		greater = new PredicateGreater(key, value, access);
		equals = new PredicateEquals(key, value, access);
	}

	@Override
	public boolean test(Object t) {
		return greater.test(t) || equals.test(t);
	}
}