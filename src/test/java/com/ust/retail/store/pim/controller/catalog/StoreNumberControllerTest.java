package com.ust.retail.store.pim.controller.catalog;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.dto.security.StoreNumberInfoDTO;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreNumberControllerTest {
	private static final String STORE_NUMBER_NAME = "My Store Number";

	@Mock
	private StoreNumberService mockStoreNumberService;

	@InjectMocks
	private StoreNumberController controller;

	@Test
	void createReturnsExpectedWheInputIsValid() {
		StoreNumberDTO dto = new StoreNumberDTO(null, STORE_NUMBER_NAME);
		when(mockStoreNumberService.create(any()))
				.then(invocation -> new StoreNumberDTO(1L, invocation.<StoreNumberDTO>getArgument(0).getStoreName()));

		StoreNumberDTO result = controller.create(dto);

		assertThat(result, allOf(
				hasProperty("storeNumId", equalTo(1L)),
				hasProperty("storeName", equalTo(STORE_NUMBER_NAME))
		));
	}

	@Test
	void updateReturnsExpectedWhenInputIsValid() {
		StoreNumberDTO dto = new StoreNumberDTO(1L, STORE_NUMBER_NAME);
		when(mockStoreNumberService.update(any()))
				.then(invocation -> invocation.<StoreNumberDTO>getArgument(0));

		StoreNumberDTO result = controller.update(dto);

		assertThat(result, allOf(
				hasProperty("storeNumId", equalTo(1L)),
				hasProperty("storeName", equalTo(STORE_NUMBER_NAME))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenObjectFound() {
		StoreNumberDTO dto = new StoreNumberDTO(1L, STORE_NUMBER_NAME);
		when(mockStoreNumberService.findById(1L)).thenReturn(dto);

		StoreNumberDTO result = controller.findById(1L);

		assertThat(result, allOf(
				hasProperty("storeNumId", equalTo(1L)),
				hasProperty("storeName", equalTo(STORE_NUMBER_NAME))
		));
	}

	@Test
	void findByFiltersReturnsExpectedWhenResultsFound() {
		StoreNumberDTO dto1 = new StoreNumberDTO(1L, STORE_NUMBER_NAME);
		StoreNumberDTO dto6 = new StoreNumberDTO(6L, "Store Number 6");
		List<StoreNumberDTO> searchResults = List.of(dto1, dto6);
		StoreNumberDTO dto = new StoreNumberDTO(null, "Store Number");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("storeName");
		dto.setOrderDir("desc");
		when(mockStoreNumberService.getStoreNumbersByFilters(dto))
				.thenReturn(new PageImpl<>(searchResults));

		Page<StoreNumberDTO> result = controller.findByFilters(dto);

		assertThat(result.getTotalElements(), is(2L));
		assertThat(result.getContent(), contains(dto1, dto6));
	}

	@Test
	void findAutoCompleteOptionsReturnExpectedWhenResultsFound() {
		StoreNumberDTO dto1 = new StoreNumberDTO(1L, "My Store Number 1");
		StoreNumberDTO dto6 = new StoreNumberDTO(6L, "Store Number 6");
		List<StoreNumberDTO> searchResults = List.of(dto1, dto6);
		when(mockStoreNumberService.getAutocomplete("Store Number")).thenReturn(searchResults);

		List<StoreNumberDTO> result = controller.findAutoCompleteOptions("Store Number");

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void loadCatalogReturnExpectedWhenResultsFound() {
		StoreNumberDTO dto1 = new StoreNumberDTO(1L, STORE_NUMBER_NAME);
		StoreNumberDTO dto6 = new StoreNumberDTO(6L, "Store Number 6");
		List<StoreNumberDTO> results = List.of(dto1, dto6);
		when(mockStoreNumberService.load()).thenReturn(results);

		List<StoreNumberDTO> result = controller.loadCatalog();

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void loadByVendorMasterIdReturnExpectedWhenResultsFound() {
		StoreNumberDTO dto1 = new StoreNumberDTO(1L, STORE_NUMBER_NAME);
		StoreNumberDTO dto6 = new StoreNumberDTO(6L, "Store Number 6");
		List<StoreNumberDTO> results = List.of(dto1, dto6);
		when(mockStoreNumberService.loadByVendorMasterId(1L)).thenReturn(results);

		List<StoreNumberDTO> result = controller.loadCatalogByVendorMasterId(1L);

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void changeStoreNumberReturnExpectedWhenResultsFound() {
		StoreNumberDTO dto = new StoreNumberDTO(1L, STORE_NUMBER_NAME);
		when(mockStoreNumberService.findById(1L)).thenReturn(dto);

		StoreNumberInfoDTO result = controller.changeStoreNumber(1L);

		assertThat(result, allOf(
				hasProperty("storeNumberId", equalTo(1L)),
				hasProperty("storeNumberName", equalTo(STORE_NUMBER_NAME))
		));
	}
}
