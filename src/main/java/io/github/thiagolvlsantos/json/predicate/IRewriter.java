package io.github.thiagolvlsantos.json.predicate;

import com.fasterxml.jackson.databind.JsonNode;

public interface IRewriter {

	JsonNode rewrite(JsonNode source);
}