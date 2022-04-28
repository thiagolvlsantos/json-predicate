package io.github.thiagolvlsantos.json.predicate.value.impl;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.AbstractPredicateValue;

public class PredicateContains extends AbstractPredicateValue {

	public PredicateContains(String key, JsonNode value, IAccess access) {
		super(key, value, access);
	}

	@Override
	public boolean test(Object t) {
		Object tmp = unwrapp(t);
		return check(tmp);
	}

	protected boolean check(Object tmp) {
		if (tmp instanceof Collection<?>) {
			for (Iterator<?> iterator = ((Collection<?>) tmp).iterator(); iterator.hasNext();) {
				Object obj = (Object) iterator.next();
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
		return String.valueOf(tmp).contains(value.asText());
	}
}