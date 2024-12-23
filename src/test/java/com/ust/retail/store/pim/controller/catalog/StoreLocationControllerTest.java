package com.ust.retail.store.pim.controller.catalog;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.dto.catalog.StoreLocationDTO;
import com.ust.retail.store.pim.service.catalog.StoreLocationService;

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
class StoreLocationControllerTest {
	private static final String STORE_LOCATION_NAME = "My Store Location";

	@Mock
	private StoreLocationService mockStoreLocationService;

	@InjectMocks
	private StoreLocationController controller;

	@Test
	void createReturnsExpectedWheInputIsValid() {
		StoreLocationDTO dto = new StoreLocationDTO(null, STORE_LOCATION_NAME, 1L, null);
		when(mockStoreLocationService.save(any()))
				.then(invocation -> new StoreLocationDTO(1L, invocation.<StoreLocationDTO>getArgument(0).getStoreLocationName(), 1L, null));

		StoreLocationDTO result = controller.create(dto);

		assertThat(result, allOf(
				hasProperty("storeLocationId", equalTo(1L)),
				hasProperty("storeLocationName", equalTo(STORE_LOCATION_NAME))
		));
	}

	@Test
	void updateReturnsExpectedWhenInputIsValid() {
		StoreLocationDTO dto = new StoreLocationDTO(1L, STORE_LOCATION_NAME, 1L, null);
		when(mockStoreLocationService.update(any()))
				.then(invocation -> invocation.<StoreLocationDTO>getArgument(0));

		StoreLocationDTO result = controller.update(dto);

		assertThat(result, allOf(
				hasProperty("storeLocationId", equalTo(1L)),
				hasProperty("storeLocationName", equalTo(STORE_LOCATION_NAME))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenObjectFound() {
		StoreLocationDTO dto = new StoreLocationDTO(1L, STORE_LOCATION_NAME, 1L, null);
		when(mockStoreLocationService.findById(1L)).thenReturn(dto);

		StoreLocationDTO result = controller.findById(1L);

		assertThat(result, allOf(
				hasProperty("storeLocationId", equalTo(1L)),
				hasProperty("storeLocationName", equalTo(STORE_LOCATION_NAME))
		));
	}

	@Test
	void findByFiltersReturnsExpectedWhenResultsFound() {
		StoreLocationDTO dto1 = new StoreLocationDTO(1L, STORE_LOCATION_NAME, 1L, null);
		StoreLocationDTO dto6 = new StoreLocationDTO(6L, "Store Location 6", 1L, null);
		List<StoreLocationDTO> searchResults = List.of(dto1, dto6);
		StoreLocationDTO dto = new StoreLocationDTO(null, "Store Location", 1L, null);
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("storeLocationName");
		dto.setOrderDir("desc");
		when(mockStoreLocationService.getStoreLocationsByFilters(dto))
				.thenReturn(new PageImpl<>(searchResults));

		Page<StoreLocationDTO> result = controller.findByFilters(dto);

		assertThat(result.getTotalElements(), is(2L));
		assertThat(result.getContent(), contains(dto1, dto6));
	}

	@Test
	void loadCatalogReturnExpectedWhenResultsFound() {
		StoreLocationDTO dto1 = new StoreLocationDTO(1L, STORE_LOCATION_NAME, 1L, null);
		StoreLocationDTO dto6 = new StoreLocationDTO(6L, "Store Location 6", 1L, null);
		List<StoreLocationDTO> results = List.of(dto1, dto6);
		when(mockStoreLocationService.load(1L)).thenReturn(results);

		List<StoreLocationDTO> result = controller.loadCatalog(1L);

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}
}
