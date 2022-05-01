package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;
import io.github.thiagolvlsantos.json.predicate.value.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.IConverter;

public class PredicateGreaterEquals extends AbstractPredicateValue {

	private PredicateGreater greater;
	private PredicateEquals equals;

	public PredicateGreaterEquals(String key, IAccess access, JsonNode value, IConverter converter) {
		super(key, access, value, converter);
		greater = new PredicateGreater(key, access, value, converter);
		equals = new PredicateEquals(key, access, value, converter);
	}

	@Override
	public boolean test(Object t) {
		return greater.test(t) || equals.test(t);
	}
}