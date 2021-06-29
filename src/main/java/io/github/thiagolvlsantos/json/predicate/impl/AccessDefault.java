package io.github.thiagolvlsantos.json.predicate.impl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;

import io.github.thiagolvlsantos.json.predicate.IAccess;

public class AccessDefault implements IAccess {

	@Override
	public Object get(Object source, String path) {
		try {
			return BeanUtils.getProperty(source, path);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}