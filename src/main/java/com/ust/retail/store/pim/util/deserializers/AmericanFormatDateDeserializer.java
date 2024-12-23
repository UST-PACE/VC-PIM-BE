package com.ust.retail.store.pim.util.deserializers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class AmericanFormatDateDeserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = 1L;
	
	private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));

	public AmericanFormatDateDeserializer() {
		this(null);
	}

	public AmericanFormatDateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Date deserialize(JsonParser jsonparser, DeserializationContext context)
			throws IOException {
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
