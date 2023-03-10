package io.github.thiagolvlsantos.json.predicate.value.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import io.github.thiagolvlsantos.json.predicate.value.IAccess;
import io.github.thiagolvlsantos.json.predicate.value.IConverter;
import lombok.SneakyThrows;

public class ConverterDefault implements IConverter {

	private static final String VARIABLE = "@";
	private SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private DateTimeFormatter localDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private DateTimeFormatter localDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	@SneakyThrows
	@Override
	public Object convert(Object source, IAccess access, Object example, JsonNode value) {
		if (value instanceof ArrayNode) {
			ArrayNode arrayNode = (ArrayNode) value;
			List<Object> array = new ArrayList<>(arrayNode.size());
			for (int i = 0; i < arrayNode.size(); i++) {
				array.add(convert(source, access, example, arrayNode.get(i)));
			}
			return array.toArray(new Object[0]);
		} else {
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
				return toDate(text);
			} else if (example instanceof LocalDate) {
				return toLocalDate(text);
			} else if (example instanceof LocalDateTime) {
				return toLocalDateTime(text);
			}
		}
		return value.asText();
	}

	private Object toAccess(Object source, IAccess access, String path) {
		return access.get(source, path);
	}

	private Date toDate(String str) throws ParseException {
		return date.parse(str);
	}

	private LocalDate toLocalDate(String str) {
		return LocalDate.parse(str, localDate);
	}

	private LocalDateTime toLocalDateTime(String str) {
		return LocalDateTime.parse(str, localDateTime);
	}
}