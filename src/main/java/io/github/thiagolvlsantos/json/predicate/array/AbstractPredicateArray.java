package io.github.thiagolvlsantos.json.predicate.array;

import java.util.List;

import io.github.thiagolvlsantos.json.predicate.IPredicate;

public abstract class AbstractPredicateArray implements IPredicateArray {

	protected List<IPredicate> conditions;

	protected AbstractPredicateArray(List<IPredicate> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + conditions;
	}
}