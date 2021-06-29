package io.github.thiagolvlsantos.json.predicate.impl;

import java.util.HashMap;
import java.util.Map;

import io.github.thiagolvlsantos.json.predicate.IPredicate;
import io.github.thiagolvlsantos.json.predicate.IPredicateManager;
import io.github.thiagolvlsantos.json.predicate.array.impl.PredicateAnd;
import io.github.thiagolvlsantos.json.predicate.array.impl.PredicateOr;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateContains;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateEquals;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateGreater;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateGreaterEquals;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateLower;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateLowerEquals;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateMatch;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateNotContains;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateNotEquals;
import io.github.thiagolvlsantos.json.predicate.value.impl.PredicateNotMatch;
import io.github.thiagolvlsantos.json.predicate.wrapper.impl.PredicateNot;

public class PredicateManagerDefault implements IPredicateManager {

	private Map<String, Class<? extends IPredicate>> map = new HashMap<>();

	public PredicateManagerDefault() {
		// arrays
		this.put("$and", PredicateAnd.class);
		this.put("$or", PredicateOr.class);
		// wrappers
		this.put("$not", PredicateNot.class);
		// values
		this.put("$eq", PredicateEquals.class);
		this.put("$ne", PredicateNotEquals.class);
		this.put("$gt", PredicateGreater.class);
		this.put("$ge", PredicateGreaterEquals.class);
		this.put("$lt", PredicateLower.class);
		this.put("$le", PredicateLowerEquals.class);
		this.put("$match", PredicateMatch.class);
		this.put("$nmatch", PredicateNotMatch.class);
		this.put("$contains", PredicateContains.class);
		this.put("$ncontains", PredicateNotContains.class);
	}

	@Override
	public void put(String id, Class<? extends IPredicate> clazz) {
		if (id == null) {
			throw new IllegalArgumentException("Id cannot be null.");
		}
		map.put(id.toLowerCase(), clazz);
	}

	@Override
	public Class<? extends IPredicate> get(String id) {
		if (id == null) {
			return null;
		}
		return map.get(id.toLowerCase());
	}
}