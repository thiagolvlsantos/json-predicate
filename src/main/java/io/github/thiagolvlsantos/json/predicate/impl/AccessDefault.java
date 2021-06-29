package io.github.thiagolvlsantos.json.predicate.impl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import io.github.thiagolvlsantos.json.predicate.IAccess;
import io.github.thiagolvlsantos.json.predicate.exceptions.JsonPredicateException;

public class AccessDefault implements IAccess {

	@Override
	public Object get(Object source, String path) {
		try {
			return BeanUtils.getProperty(source, path);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new JsonPredicateException(e.getMessage(), e);
		}
	}
}