package com.ust.retail.store.pim.util.deserializers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomDateDeserializerTest {
	@Mock
	private JsonParser parser;
	@Mock
	private DeserializationContext context;

	@InjectMocks
	private CustomDateDeserializer deserializer;

	@Test
	void deserializeReturnsNullWhenTextIsEmpty() throws Exception {
		when(parser.getText()).thenReturn("");

		Date result = deserializer.deserialize(parser, context);

		assertThat(result, is(nullValue()));
	}

	@Test
	void deserializeReturnsExpectedWhenTextIsValidDate() throws Exception {
		ZonedDateTime dateTime = LocalDateTime.of(2021, 1, 1, 15, 0, 0).atZone(ZoneId.of("America/Mexico_City"));
		when(parser.getText()).thenReturn(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", new Locale("es", "MX")).format(dateTime));

		Date result = deserializer.deserialize(parser, context);

		assertThat(result.toInstant(), comparesEqualTo(dateTime.toInstant()));
	}

	@Test
	void deserializeThrowsExceptionWhenTextIsInvalidDate() throws Exception {
		when(parser.getText()).thenReturn("INVALID DATE");

		assertThrows(RuntimeException.class, () -> deserializer.deserialize(parser, context));
	}

	@Test
	void defaultConstructorWorks() {
		CustomDateDeserializer subject = new CustomDateDeserializer();

		assertThat(subject.handledType(), is(nullValue()));
	}
}
