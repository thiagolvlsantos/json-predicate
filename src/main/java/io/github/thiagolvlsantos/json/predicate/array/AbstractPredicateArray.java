package io.github.thiagolvlsantos.json.predicate.array;

import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractPredicateArray implements IPredicateArray {

	protected List<Predicate<Object>> conditions;

	public AbstractPredicateArray(List<Predicate<Object>> conditions) {
		this.conditions = conditions;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + conditions;
	}
}