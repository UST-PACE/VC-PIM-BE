package com.ust.retail.store.pim.util.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomIntegerDeserializer extends JsonDeserializer<Integer> {

	@Override
	public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		if (!p.getText().isBlank()) {
			return Integer.parseInt(p.getText().replaceAll("[^0-9.-]+", "").trim());
		}
		return null;
	}
}
