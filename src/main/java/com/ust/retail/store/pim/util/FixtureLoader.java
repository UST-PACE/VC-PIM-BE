package com.ust.retail.store.pim.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class FixtureLoader {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Config config;

	public FixtureLoader() {
		config = new Config();
		config.objects = new ArrayList<>();
	}

	public FixtureLoader(String configFile) throws Exception {
		InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(configFile);
		config = objectMapper.readValue(resourceAsStream, Config.class);
	}

	public FixtureLoader(Class<?> testClass) throws Exception {
		this(String.format("%s.json", testClass.getCanonicalName().replaceAll("\\.", "\\/")));
	}

	public <T> Optional<T> getObject(String id, Class<T> type) {
		return config.getValue(id)
				.map(configValue -> objectMapper.convertValue(configValue.definition, type));
	}

	public <T> Optional<T> getObject(String id, TypeReference<T> typeReference) {
		return config.getValue(id)
				.map(configValue -> objectMapper.convertValue(configValue.definition, typeReference));
	}

	public <T> Optional<T> getObjectFromString(String json, Class<T> type) {
		try {
			return Optional.ofNullable(objectMapper.convertValue(objectMapper.readTree(json), type));
		} catch (IOException e) {
			log.error("Error parsing content", e);
			return Optional.empty();
		}
	}

	public <T> Optional<T> getObjectFromString(String json, TypeReference<T> type) {
		try {
			return Optional.ofNullable(objectMapper.convertValue(objectMapper.readTree(json), type));
		} catch (IOException e) {
			log.error("Error parsing content", e);
			return Optional.empty();
		}
	}

	public void disableAnnotations() {
		objectMapper.disable(MapperFeature.USE_ANNOTATIONS);
	}

	public void enableAnnotations() {
		objectMapper.enable(MapperFeature.USE_ANNOTATIONS);
	}

	@Getter
	@Setter
	private static class Config {
		private List<ConfigObject> objects;

		public Optional<ConfigObject> getValue(String id) {
			return objects.stream()
					.filter(object -> Objects.equals(object.getId(), id))
					.findFirst();
		}
	}

	@Getter
	@Setter
	private static class ConfigObject {
		private String id;
		private JsonNode definition;
	}
}
