package io.github.thiagolvlsantos.json.predicate;

import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;

public interface IPredicateFactory {

	Predicate<Object> read(byte[] content) throws Exception;

	Predicate<Object> read(JsonNode content) throws Exception;
}