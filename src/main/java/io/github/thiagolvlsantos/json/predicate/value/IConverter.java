package io.github.thiagolvlsantos.json.predicate.value;

import com.fasterxml.jackson.databind.JsonNode;

public interface IConverter {

	Object convert(Object example, JsonNode value);
}
