package io.github.thiagolvlsantos.json.predicate.value.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;
import io.github.thiagolvlsantos.json.predicate.value.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.IConverter;

public class PredicateMemberOf extends AbstractPredicateValue {

	public PredicateMemberOf(String key, IAccess access, JsonNode value, IConverter converter) {
		super(key, access, value, converter);
	}

	@Override
	public boolean test(Object t) {
		Object left = left(t);
		return check(left, right(t, left));
	}

	protected boolean check(Object base, Object tmp) {
		List<Object> left = toList(base);
		List<Object> right = toList(tmp);
		return left.stream().allMatch(v -> right.contains(v));
	}

	protected List<Object> toList(Object obj) {
		List<Object> result = new LinkedList<>();
		if (obj instanceof Collection<?>) {
			result.addAll((Collection<?>) obj);
		}
		if (obj != null && obj.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(obj); i++) {
				result.add(Array.get(obj, i));
			}
		}
		if (result.isEmpty()) {
			result.add(obj);
		}
		return result;
	}
}