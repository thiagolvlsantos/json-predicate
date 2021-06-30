package io.github.thiagolvlsantos.json.predicate;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.exceptions.JsonPredicateException;

public interface IPredicateFactory {

	IPredicate read(byte[] content) throws JsonPredicateException;

	IPredicate read(JsonNode content) throws JsonPredicateException;
}