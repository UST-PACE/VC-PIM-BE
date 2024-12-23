package com.ust.retail.store.pim.util.deserializers;

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
class CustomIntegerDeserializerTest {
	@Mock
	private JsonParser parser;
	@Mock
	private DeserializationContext context;

	@InjectMocks
	private CustomIntegerDeserializer deserializer;

	@Test
	void deserializeReturnsNullWhenTextIsEmpty() throws Exception {
		when(parser.getText()).thenReturn("");

		Integer result = deserializer.deserialize(parser, context);

		assertThat(result, is(nullValue()));
	}

	@Test
	void deserializeReturnsExpectedWhenStringHasComma() throws Exception {
		when(parser.getText()).thenReturn("12,345");

		Integer result = deserializer.deserialize(parser, context);

		assertThat(result, is(12345));
	}

	@Test
	void deserializeThrowsExceptionWhenTextIsInvalid() throws Exception {
		when(parser.getText()).thenReturn("INVALID", "12,345.67");

		assertThrows(NumberFormatException.class, () -> deserializer.deserialize(parser, context));
		assertThrows(NumberFormatException.class, () -> deserializer.deserialize(parser, context));
	}

}
