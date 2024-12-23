package com.ust.retail.store.pim.util.deserializers;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

	@Override
	public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		if (!p.getText().isBlank()) {
			return new BigDecimal(p.getText().replaceAll("[^0-9.-]+", "").trim());
		}
		return null;
	}
}
