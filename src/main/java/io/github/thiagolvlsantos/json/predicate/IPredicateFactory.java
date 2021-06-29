package io.github.thiagolvlsantos.json.predicate;

import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.exceptions.JsonPredicateException;

public interface IPredicateFactory {

	Predicate<Object> read(byte[] content) throws JsonPredicateException;

	Predicate<Object> read(JsonNode content) throws JsonPredicateException;
}