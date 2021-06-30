package io.github.thiagolvlsantos.json.predicate.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.thiagolvlsantos.json.predicate.IAccess;
import io.github.thiagolvlsantos.json.predicate.IPredicate;
import io.github.thiagolvlsantos.json.predicate.IPredicateFactory;
import io.github.thiagolvlsantos.json.predicate.IPredicateManager;
import io.github.thiagolvlsantos.json.predicate.array.IPredicateArray;
import io.github.thiagolvlsantos.json.predicate.array.impl.PredicateAnd;
import io.github.thiagolvlsantos.json.predicate.exceptions.JsonPredicateException;
import io.github.thiagolvlsantos.json.predicate.value.IPredicateValue;
import io.github.thiagolvlsantos.json.predicate.wrapper.IPredicateWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class PredicateFactoryJson implements IPredicateFactory {

	private ObjectMapper mapper = new ObjectMapper();
	private IPredicateManager manager = new PredicateManagerDefault();
	private IAccess access = new AccessDefault();

	@Override
	public Predicate<Object> read(byte[] content) throws JsonPredicateException {
		try {
			return read(mapper.readTree(content));
		} catch (JsonPredicateException | IOException e) {
			throw new JsonPredicateException(e.getMessage(), e);
		}
	}

	@Override
	public Predicate<Object> read(JsonNode content) throws JsonPredicateException {
		return predicate("\t", content);
	}

	private Predicate<Object> predicate(String gap, JsonNode tree) throws JsonPredicateException {
		List<Predicate<Object>> result = new LinkedList<>();
		Iterator<Entry<String, JsonNode>> fields = tree.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> n = fields.next();
			String key = n.getKey();
			JsonNode value = n.getValue();
			if (key.startsWith("$")) {
				operations(gap, tree, result, key, value);
			} else {
				fields(gap, result, key, value);
			}
		}
		return result.size() > 1 ? new PredicateAnd(result) : result.get(0);
	}

	private void operations(String gap, JsonNode tree, List<Predicate<Object>> result, String key, JsonNode value) {
		Class<? extends IPredicate> type = manager.get(key);
		if (type == null) {
			throw new JsonPredicateException("Invalid list operator: " + key + " for " + tree, null);
		}
		if (IPredicateArray.class.isAssignableFrom(type)) {
			predicateArray(gap, result, key, value, type);
		} else if (IPredicateWrapper.class.isAssignableFrom(type)) {
			predicateWrapper(gap, result, value, type);
		} else {
			throw new JsonPredicateException("Invalid list operator: " + key + " is not a list.", null);
		}
	}

	private void predicateArray(String gap, List<Predicate<Object>> result, String key, JsonNode value,
			Class<? extends IPredicate> type) {
		List<Predicate<Object>> list = new LinkedList<>();
		if (value.isArray()) {
			if (log.isDebugEnabled()) {
				log.debug(gap + " LIST>" + type.getSimpleName() + "  " + key + ": " + value);
			}
			for (int i = 0; i < value.size(); i++) {
				list.add(predicate("\t" + gap, value.get(i)));
			}
		} else {
			throw new JsonPredicateException("Invalid list operator value: '" + value + "' is not a list.", null);
		}
		try {
			result.add(type.getConstructor(List.class).newInstance(list));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new JsonPredicateException(e.getMessage(), e);
		}
	}

	private void predicateWrapper(String gap, List<Predicate<Object>> result, JsonNode value,
			Class<? extends IPredicate> type) {
		try {
			result.add(type.getConstructor(Predicate.class).newInstance(predicate("\t" + gap, value)));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new JsonPredicateException(e.getMessage(), e);
		}
	}

	private void fields(String gap, List<Predicate<Object>> result, String key, JsonNode value) {
		Iterator<Entry<String, JsonNode>> fs = value.fields();
		while (fs.hasNext()) {
			Entry<String, JsonNode> f = fs.next();
			String op = f.getKey();
			JsonNode va = f.getValue();
			Class<? extends IPredicate> type = manager.get(op);
			if (type == null) {
				throw new JsonPredicateException("Invalid group operator: " + op + " for " + va, null);
			}
			if (IPredicateValue.class.isAssignableFrom(type)) {
				fieldValue(gap, result, key, value, va, type);
			} else {
				throw new JsonPredicateException("Invalid group operator: " + op + " is not a value.", null);
			}
		}
	}

	private void fieldValue(String gap, List<Predicate<Object>> result, String key, JsonNode value, JsonNode va,
			Class<? extends IPredicate> type) {
		if (log.isDebugEnabled()) {
			log.debug(gap + " VALUE>" + type.getSimpleName() + " " + key + ": " + value);
		}
		try {
			result.add(type.getConstructor(String.class, JsonNode.class, IAccess.class).newInstance(key, va, access));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new JsonPredicateException(e.getMessage(), e);
		}
	}
}