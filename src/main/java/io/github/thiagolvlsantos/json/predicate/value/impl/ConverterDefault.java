package io.github.thiagolvlsantos.json.predicate.value.impl;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.IConverter;

public class ConverterDefault implements IConverter {

	@Override
	public Object convert(Object example, JsonNode value) {
		if (example instanceof Boolean) {
			return value.asBoolean();
		} else if (example instanceof Short) {
			return value.asInt();
		} else if (example instanceof Integer) {
			return value.asInt();
		} else if (example instanceof Long) {
			return value.asLong();
		} else if (example instanceof Float) {
			return value.asDouble();
		} else if (example instanceof Double) {
			return value.asDouble();
		}
		return value.asText();
	}
}