package io.github.thiagolvlsantos.json.predicate.value;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractPredicateValue implements IPredicateValue {

	protected String key;
	protected JsonNode value;
	protected IAccess access;

	protected AbstractPredicateValue(String key, JsonNode value, IAccess access) {
		this.key = key;
		this.value = value;
		this.access = access;
	}

	protected Object unwrapp(Object source) {
		Object object = access.get(source, key);
		converted(object);
		if (object instanceof byte[]) {
			return new String((byte[]) object);
		}
		return object;
	}

	protected Object converted(Object reference) {
		System.out.println(reference != null ? reference.getClass() : null);
		return reference;
	}

	@Override
	public String toString() {
		return key + " " + getClass().getSimpleName() + " " + value.asText();
	}
}