package io.github.thiagolvlsantos.json.predicate.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.thiagolvlsantos.json.predicate.IRewriter;

public class RewriterDefault implements IRewriter {

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public JsonNode rewrite(JsonNode source) {
		try {
			// basic case, when not specified operator expected to be equals
			return mapper.readTree("{ \"$eq\": " + source.toPrettyString() + "}");
		} catch (JsonProcessingException e) {
			// remains original and get other validations later
			return source;
		}
	}
}