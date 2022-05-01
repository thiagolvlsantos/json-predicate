package io.github.thiagolvlsantos.json.predicate.value.impl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.beanutils.PropertyUtils;

import io.github.thiagolvlsantos.json.predicate.exceptions.JsonPredicateException;
import io.github.thiagolvlsantos.json.predicate.value.IAccess;

public class AccessDefault implements IAccess {

	@Override
	public Object get(Object source, String path) {
		try {
			return PropertyUtils.getProperty(source, path);
		} catch (NestedNullException e) {
			return null;
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new JsonPredicateException(e.getMessage(), e);
		}
	}
}