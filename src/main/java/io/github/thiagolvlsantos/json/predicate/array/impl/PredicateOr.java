package io.github.thiagolvlsantos.json.predicate.array.impl;

import java.util.List;
import java.util.function.Predicate;

import io.github.thiagolvlsantos.json.predicate.array.AbstractPredicateArray;

public class PredicateOr extends AbstractPredicateArray {

	public PredicateOr(List<Predicate<Object>> conditions) {
		super(conditions);
	}

	@Override
	public boolean test(Object obj) {
		return conditions.stream().anyMatch(p -> p.test(obj));
	}
}