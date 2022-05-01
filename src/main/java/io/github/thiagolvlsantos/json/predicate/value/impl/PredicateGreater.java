package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;
import io.github.thiagolvlsantos.json.predicate.value.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.IConverter;

public class PredicateGreater extends AbstractPredicateValue {

	public PredicateGreater(String key, IAccess access, JsonNode value, IConverter converter) {
		super(key, access, value, converter);
	}

	@Override
	public boolean test(Object t) {
		Object left = left(t);
		Object right = right(t, left);
		if (left instanceof Comparable && left.getClass() == right.getClass()) {
			return ((Comparable) left).compareTo(right) > 0;
		}
		if (left instanceof Number && right instanceof Number) {
			return ((Number) left).doubleValue() - ((Number) right).doubleValue() > 0;
		}
		return String.valueOf(left).compareTo(String.valueOf(right)) > 0;
	}
}