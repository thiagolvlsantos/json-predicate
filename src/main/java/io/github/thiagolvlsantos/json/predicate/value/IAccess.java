package io.github.thiagolvlsantos.json.predicate.value;

public interface IAccess {

	Object get(Object source, String path);

	Class<?> type(Object source, String path);
}
