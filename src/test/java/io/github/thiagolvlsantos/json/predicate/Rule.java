package io.github.thiagolvlsantos.json.predicate;

import java.util.function.Predicate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.github.thiagolvlsantos.json.predicate.impl.PredicateDeserializer;
import lombok.Data;

@Data
public class Rule {

	private String name;
	@JsonDeserialize(using = PredicateDeserializer.class)
	private Predicate<Object> condition;
}
