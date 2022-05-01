package io.github.thiagolvlsantos.json.predicate.value;

import com.fasterxml.jackson.databind.JsonNode;

public interface IConverter {

	Object convert(Object source, IAccess access, Object example, JsonNode value);
}
