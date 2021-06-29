package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.IAccess;

public class PredicateNotEquals extends PredicateEquals {

	public PredicateNotEquals(String key, JsonNode value, IAccess access) {
		super(key, value, access);
	}

	@Override
	public boolean test(Object t) {
		return !super.test(t);
	}
}