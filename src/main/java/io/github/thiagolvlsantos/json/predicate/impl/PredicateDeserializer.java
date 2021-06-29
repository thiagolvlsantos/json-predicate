package io.github.thiagolvlsantos.json.predicate.impl;

import java.io.IOException;
import java.util.function.Predicate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import io.github.thiagolvlsantos.json.predicate.IPredicateFactory;
import io.github.thiagolvlsantos.json.predicate.exceptions.JsonPredicateException;

@SuppressWarnings("serial")
public class PredicateDeserializer extends StdDeserializer<Predicate<Object>> {

	private static Object lock = new Object();
	private static IPredicateFactory factory;

	public PredicateDeserializer() {
		this(null);
	}

	public PredicateDeserializer(Class<?> vc) {
		super(vc);
	}

	public static void setFactory(IPredicateFactory factory) {
		PredicateDeserializer.factory = factory;
	}

	@Override
	public Predicate<Object> deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException {
		synchronized (lock) {
			if (factory == null) {
				factory = new PredicateFactoryJson();
			}
			try {
				String json = jsonparser.readValueAsTree().toString();
				return factory.read(json.getBytes());
			} catch (Exception e) {
				throw new JsonPredicateException(e.getMessage(), e);
			}
		}
	}
}