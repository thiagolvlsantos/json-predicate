package io.github.thiagolvlsantos.json.predicate.wrapper.impl;

import java.util.function.Predicate;

import io.github.thiagolvlsantos.json.predicate.wrapper.AbstractPredicateWrapper;

public class PredicateNot extends AbstractPredicateWrapper {

	public PredicateNot(Predicate<Object> condition) {
		super(condition);
	}

	@Override
	public boolean test(Object t) {
		return !condition.test(t);
	}
}