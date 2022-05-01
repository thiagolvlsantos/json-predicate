package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.IConverter;

public class PredicateNotEquals extends PredicateEquals {

	public PredicateNotEquals(String key, IAccess access, JsonNode value, IConverter converter) {
		super(key, access, value, converter);
	}

	@Override
	public boolean test(Object t) {
		return !super.test(t);
	}
}