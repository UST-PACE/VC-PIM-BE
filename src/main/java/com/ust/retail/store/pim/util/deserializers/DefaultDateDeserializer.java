package com.ust.retail.store.pim.util.deserializers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class DefaultDateDeserializer extends StdDeserializer<Date> {
	private static final long serialVersionUID = 1L;

	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", new Locale("es", "MX"));

	public DefaultDateDeserializer() {
		this(null);
	}

	public DefaultDateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException, JsonProcessingException {
		if (!jsonparser.getText().isBlank()) {
			String date = jsonparser.getText();
			try {
				return formatter.parse(date);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}
}
