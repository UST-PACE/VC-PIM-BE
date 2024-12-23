package com.ust.retail.store.pim.util.serializers;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

@ExtendWith(MockitoExtension.class)
class DefaultDateSerializerTest {

	@InjectMocks
	private DefaultDateSerializer serializer;
	private Writer jsonWriter;
	private JsonGenerator generator;
	private SerializerProvider serializerProvider;

	@BeforeEach
	void setUp() throws Exception {
		jsonWriter = new StringWriter();
		generator = new JsonFactory().createGenerator(jsonWriter);
		serializerProvider = new ObjectMapper().getSerializerProvider();
	}

	@Test
	void serializeWorksAsExpected() throws Exception {
		ZonedDateTime zonedDateTime = LocalDate.of(2021, 12, 1).atStartOfDay().atZone(ZoneId.of("America/Mexico_City"));

		serializer.serialize(Date.from(zonedDateTime.toInstant()), generator, serializerProvider);
		generator.flush();

		assertThat(jsonWriter.toString(), is("\"01/12/2021\""));
	}

	@Test
	void defaultConstructorWorks() {
		DefaultDateSerializer subject = new DefaultDateSerializer();

		assertThat(subject.handledType(), is(nullValue()));
	}
}
