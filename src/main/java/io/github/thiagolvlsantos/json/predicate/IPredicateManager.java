package io.github.thiagolvlsantos.json.predicate;

public interface IPredicateManager {

	void put(String id, Class<? extends IPredicate> clazz);

	Class<? extends IPredicate> get(String id);
}