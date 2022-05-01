package io.github.thiagolvlsantos.json.predicate.value;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractPredicateValue implements IPredicateValue {

	protected String key;
	protected IAccess access;

	protected JsonNode value;
	protected IConverter converter;

	protected AbstractPredicateValue(String key, IAccess access, JsonNode value, IConverter converter) {
		this.key = key;
		this.access = access;
		this.value = value;
		this.converter = converter;
	}

	protected Object left(Object source) {
		Object left = access.get(source, key);
		if (left instanceof byte[]) {
			left = new String((byte[]) left);
		}
		return left;
	}

	protected Object right(Object left) {
		return converter.convert(left, value);
	}

	@Override
	public String toString() {
		return key + " " + getClass().getSimpleName() + " " + value.asText();
	}
}