package io.github.thiagolvlsantos.json.predicate;

import com.fasterxml.jackson.databind.JsonNode;

public interface IRewritter {

	JsonNode rewrite(JsonNode source);
}