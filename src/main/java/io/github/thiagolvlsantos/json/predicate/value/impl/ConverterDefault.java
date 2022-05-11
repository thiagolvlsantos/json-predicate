package io.github.thiagolvlsantos.json.predicate.value.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.databind.JsonNode;

import io.github.thiagolvlsantos.json.predicate.value.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.IConverter;
import lombok.SneakyThrows;

public class ConverterDefault implements IConverter {

	private static final String VARIABLE = "@";
	private SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private DateTimeFormatter localDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private DateTimeFormatter localDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	@Override
	public Object convert(Object source, IAccess access, Object example, JsonNode value) {
		String text = value.asText();
		if (text.startsWith(VARIABLE)) {
			return toAccess(source, access, text.substring(1));
		} else if (example instanceof Boolean) {
			return value.asBoolean();
		} else if (example instanceof Short) {
			return (short) value.asInt();
		} else if (example instanceof Integer) {
			return value.asInt();
		} else if (example instanceof Long) {
			return value.asLong();
		} else if (example instanceof Float) {
			return (float) value.asDouble();
		} else if (example instanceof Double) {
			return value.asDouble();
		} else if (example instanceof Date) {
			return toDate(value.asText());
		} else if (example instanceof LocalDate) {
			return toLocalDate(value.asText());
		} else if (example instanceof LocalDateTime) {
			return toLocalDateTime(value.asText());
		}
		return value.asText();
	}

	@SneakyThrows
	private Object toAccess(Object source, IAccess access, String path) {
		return access.get(source, path);
	}

	@SneakyThrows
	private Date toDate(String str) {
		return date.parse(str);
	}

	@SneakyThrows
	private LocalDate toLocalDate(String str) {
		return LocalDate.parse(str, localDate);
	}

	@SneakyThrows
	private LocalDateTime toLocalDateTime(String str) {
		return LocalDateTime.parse(str, localDateTime);
	}
}