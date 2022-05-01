package io.github.thiagolvlsantos.json.predicate.value.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;
import io.github.thiagolvlsantos.json.predicate.value.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.IConverter;

public class PredicateContains extends AbstractPredicateValue {

	public PredicateContains(String key, IAccess access, JsonNode value, IConverter converter) {
		super(key, access, value, converter);
	}

	@Override
	public boolean test(Object t) {
		Object left = left(t);
		return check(left);
	}

	protected boolean check(Object tmp) {
		if (tmp instanceof Collection<?>) {
			for (Iterator<?> iterator = ((Collection<?>) tmp).iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
				if (check(obj)) {
					return true;
				}
			}
			return false;
		}
		if (tmp != null && tmp.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(tmp); i++) {
				Object obj = Array.get(tmp, i);
				if (check(obj)) {
					return true;
				}
			}
			return false;
		}
		return innerCheck(tmp);
	}

	protected boolean innerCheck(Object tmp) {
		if (tmp instanceof String) {
			return String.valueOf(tmp).contains(String.valueOf(right(tmp)));
		} else {
			return tmp.equals(right(tmp));
		}
	}
}