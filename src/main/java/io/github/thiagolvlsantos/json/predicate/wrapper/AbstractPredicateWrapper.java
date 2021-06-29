package io.github.thiagolvlsantos.json.predicate.wrapper;

import java.util.function.Predicate;

public abstract class AbstractPredicateWrapper implements IPredicateWrapper {

	protected Predicate<Object> condition;

	public AbstractPredicateWrapper(Predicate<Object> condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " (" + condition + ")";
	}
}