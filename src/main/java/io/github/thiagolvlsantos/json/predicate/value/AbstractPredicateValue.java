package io.github.thiagolvlsantos.json.predicate.value;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.IAccess;

public abstract class AbstractPredicateValue implements IPredicateValue {

	protected String key;
	protected JsonNode value;
	protected IAccess access;

	public AbstractPredicateValue(String key, JsonNode value, IAccess access) {
		this.key = key;
		this.value = value;
		this.access = access;
	}

	protected Object unwrapp(Object source) {
		return access.get(source, key);
	}

	@Override
	public String toString() {
		return key + " " + getClass().getSimpleName() + " " + value.asText();
	}
}