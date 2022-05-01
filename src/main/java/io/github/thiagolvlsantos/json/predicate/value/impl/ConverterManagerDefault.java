package io.github.thiagolvlsantos.json.predicate.value.impl;

import java.util.HashMap;
import java.util.Map;

import io.github.thiagolvlsantos.json.predicate.value.IConverter;
import io.github.thiagolvlsantos.json.predicate.value.IConverterManager;

public class ConverterManagerDefault implements IConverterManager {

	private Map<Class<?>, Class<? extends IConverter>> map = new HashMap<>();

	public ConverterManagerDefault() {
	}

	@Override
	public void put(Class<?> type, Class<? extends IConverter> clazz) {
		if (type == null) {
			throw new IllegalArgumentException("Type cannot be null.");
		}
		map.put(type, clazz);
	}

	@Override
	public Class<? extends IConverter> get(Class<?> type) {
		if (type == null) {
			return null;
		}
		return map.get(type);
	}
}