package io.github.thiagolvlsantos.json.predicate.value.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.exceptions.JsonPredicateException;
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
		if (tmp instanceof Collection<?>) {
			for (Iterator<?> iterator = ((Collection<?>) tmp).iterator(); iterator.hasNext();) {
				Object obj = iterator.next();
				if (innerCheck(base, obj)) {
					return true;
				}
			}
			return false;
		}
		if (tmp != null && tmp.getClass().isArray()) {
			for (int i = 0; i < Array.getLength(tmp); i++) {
				Object obj = Array.get(tmp, i);
				if (innerCheck(base, obj)) {
					return true;
				}
			}
			return false;
		}
		throw new JsonPredicateException("Value is neither a Collection nor an Array. Received: '" + tmp + "' of type "
				+ (tmp != null ? tmp.getClass().getName() : null), null);
	}

	protected boolean innerCheck(Object base, Object tmp) {
		if (tmp instanceof String) {
			return String.valueOf(base).equals(tmp);
		} else {
			return Objects.equals(base, tmp);
		}
	}
}