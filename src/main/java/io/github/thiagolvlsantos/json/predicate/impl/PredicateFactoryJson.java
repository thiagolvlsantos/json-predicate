package io.github.thiagolvlsantos.json.predicate.impl;

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
import io.github.thiagolvlsantos.json.predicate.value.IPredicateValue;
import io.github.thiagolvlsantos.json.predicate.wrapper.IPredicateWrapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Slf4j
public class PredicateFactoryJson implements IPredicateFactory {

	private ObjectMapper mapper = new ObjectMapper();
	private IPredicateManager manager = new PredicateManagerDefault();
	private IAccess access = new AccessDefault();

	@Override
	public Predicate<Object> read(byte[] content) throws Exception {
		return read(mapper.readTree(content));
	}

	@Override
	public Predicate<Object> read(JsonNode content) throws Exception {
		return predicate("\t", (JsonNode) content);
	}

	private Predicate<Object> predicate(String gap, JsonNode tree) throws Exception {
		List<Predicate<Object>> result = new LinkedList<>();
		Iterator<Entry<String, JsonNode>> fields = tree.fields();
		while (fields.hasNext()) {
			Entry<String, JsonNode> n = fields.next();
			String key = n.getKey();
			JsonNode value = n.getValue();
			if (key.startsWith("$")) {
				Class<? extends IPredicate> type = manager.get(key);
				if (type == null) {
					throw new RuntimeException("Invalid list operator: " + key + " for " + tree);
				}
				if (IPredicateArray.class.isAssignableFrom(type)) {
					List<Predicate<Object>> list = new LinkedList<>();
					if (value.isArray()) {
						if (log.isDebugEnabled()) {
							log.debug(gap + " LIST>" + type.getSimpleName() + "  " + key + ": " + value);
						}
						for (int i = 0; i < value.size(); i++) {
							list.add(predicate("\t" + gap, value.get(i)));
						}
					} else {
						throw new RuntimeException("Invalid list operator value: '" + value + "' is not a list.");
					}
					result.add(type.getConstructor(List.class).newInstance(list));
				} else if (IPredicateWrapper.class.isAssignableFrom(type)) {
					result.add(type.getConstructor(Predicate.class).newInstance(predicate("\t" + gap, value)));
				} else {
					throw new RuntimeException("Invalid list operator: " + key + " is not a list.");
				}
			} else {
				Iterator<Entry<String, JsonNode>> fs = value.fields();
				while (fs.hasNext()) {
					Entry<String, JsonNode> f = fs.next();
					String op = f.getKey();
					JsonNode va = f.getValue();
					Class<? extends IPredicate> type = manager.get(op);
					if (type == null) {
						throw new RuntimeException("Invalid group operator: " + op + " for " + va);
					}
					if (IPredicateValue.class.isAssignableFrom(type)) {
						if (log.isDebugEnabled()) {
							log.debug(gap + " VALUE>" + type.getSimpleName() + " " + key + ": " + value);
						}
						result.add(type.getConstructor(String.class, JsonNode.class, IAccess.class).newInstance(key, va,
								access));
					} else {
						throw new RuntimeException("Invalid group operator: " + op + " is not a value.");
					}
				}
			}
		}
		return result.size() > 1 ? new PredicateAnd(result) : result.get(0);
	}
}