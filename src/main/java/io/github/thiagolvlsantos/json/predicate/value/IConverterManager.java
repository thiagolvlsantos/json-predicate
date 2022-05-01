package io.github.thiagolvlsantos.json.predicate.value;

public interface IConverterManager {

	void put(Class<?> type, Class<? extends IConverter> clazz);

	Class<? extends IConverter> get(Class<?> type);
}