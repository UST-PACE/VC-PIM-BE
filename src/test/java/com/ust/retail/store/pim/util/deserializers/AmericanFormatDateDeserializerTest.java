package com.ust.retail.store.pim.util.deserializers;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AmericanFormatDateDeserializerTest {
	@Mock
	private JsonParser parser;
	@Mock
	private DeserializationContext context;

	@InjectMocks
	private AmericanFormatDateDeserializer deserializer;

	@Test
	void deserializeReturnsNullWhenBlankString() throws Exception {
		when(parser.getText()).thenReturn("");

		Date result = deserializer.deserialize(parser, context);

		assertThat(result, is(nullValue()));
	}

	@Test
	void deserializeReturnsNullWhenInvalid() throws Exception {
		when(parser.getText()).thenReturn("INVALID");

		assertThrows(RuntimeException.class, () -> deserializer.deserialize(parser, context));
	}
}
