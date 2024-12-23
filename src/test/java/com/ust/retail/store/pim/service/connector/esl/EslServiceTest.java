package com.ust.retail.store.pim.service.connector.esl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EslServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private RestTemplate mockRestTemplate;

	@Mock
	private ResponseEntity<Object> mockResponse;

	private EslService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(EslServiceTest.class);
	}

	@BeforeEach
	void setUp() {
		service = new EslService(mockRestTemplate,
				"https://example.com",
				"https://example.com",
				true,
				"CLIENT_ID",
				"CLIENT_SECRET");
	}

	@Test
	void sendUpcReturnsCompletesSuccessfullyWhenFeatureDisabled() {
		UpcMasterModel upcMasterModel = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());
		service = new EslService(mockRestTemplate,
				"https://example.com",
				"https://example.com",
				false,
				"CLIENT_ID",
				"CLIENT_SECRET");

		assertDoesNotThrow(() -> service.sendUpc(upcMasterModel));
	}

	@Test
	void sendUpcReturnsCompletesSuccessfully() {
		UpcMasterModel upcMasterModel = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());
		when(mockRestTemplate.exchange(anyString(), any(), any(), eq(Object.class))).thenReturn(mockResponse);
		when(mockResponse.getStatusCode()).thenReturn(HttpStatus.OK);

		assertDoesNotThrow(() -> service.sendUpc(upcMasterModel));
	}

	@Test
	void sendUpcReturnsCompletesSuccessfullyWhenErrorReturned() {
		UpcMasterModel upcMasterModel = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());
		when(mockRestTemplate.exchange(anyString(), any(), any(), eq(Object.class))).thenReturn(mockResponse);
		when(mockResponse.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);

		assertDoesNotThrow(() -> service.sendUpc(upcMasterModel));
	}

	@Test
	void sendUpcReturnsCompletesSuccessfullyAndLogsErrorWhenExceptionOccurs() {
		UpcMasterModel upcMasterModel = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());
		when(mockRestTemplate.exchange(anyString(), any(), any(), eq(Object.class))).thenThrow(new RuntimeException("EXPECTED_TEST_EXCEPTION"));

		assertDoesNotThrow(() -> service.sendUpc(upcMasterModel));
	}
}
