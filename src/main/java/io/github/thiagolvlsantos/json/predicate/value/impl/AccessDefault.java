package io.github.thiagolvlsantos.json.predicate.value.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

import io.github.thiagolvlsantos.json.predicate.exceptions.JsonPredicateException;
import io.github.thiagolvlsantos.json.predicate.value.IAccess;

public class AccessDefault implements IAccess {
	@SuppressWarnings("unchecked")
	@Override
	public Object get(Object source, String path) {
		try {
			// Get the key first from the current object and then from the child
			if (source instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) source;
				// Only when source is map, the format of key is xxx.ab. The flat format is supported
				Object obj = map.get(path);
				if (obj != null) {
					return obj;
				}
			}
			return PropertyUtils.getProperty(source, path);
		} catch (NestedNullException e) {
			return null;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new JsonPredicateException(e.getMessage(), e);
		}
	}
}