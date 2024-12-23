package com.ust.retail.store.pim.service.catalog;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.catalog.StoreLocationDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;
import com.ust.retail.store.pim.repository.catalog.StoreLocationRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreLocationServiceTest {
	@Mock
	private StoreLocationRepository mockStoreLocationRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private StoreLocationService service;

	@Test
	void saveReturnsExpected() {
		StoreLocationDTO request = new StoreLocationDTO(null, "StoreLocation", 1L, null);
		when(mockStoreLocationRepository.save(any())).then(invocation -> invocation.getArgument(0));

		StoreLocationDTO result = service.save(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		StoreLocationDTO request = new StoreLocationDTO(1L, "StoreLocation", 1L, null);
		when(mockStoreLocationRepository.findById(1L)).thenReturn(Optional.of(new StoreLocationModel(1L, 1L, "Store Location", 1L)));
		when(mockStoreLocationRepository.save(any())).then(invocation -> invocation.getArgument(0));

		StoreLocationDTO result = service.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockStoreLocationRepository.findById(1L)).thenReturn(Optional.of(new StoreLocationModel(1L, 1L, "Store Location", 1L)));

		StoreLocationDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenStoreNumberNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void getStoreLocationsByFiltersReturnsExpected() {
		StoreLocationDTO request = new StoreLocationDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");

		when(mockStoreLocationRepository.findByFilters(any(), any(), any())).thenReturn(new PageImpl<>(List.of()));

		Page<StoreLocationDTO> result = service.getStoreLocationsByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadReturnsExpected() {
		when(mockStoreLocationRepository.findByStoreNumId(1L)).thenReturn(List.of(new StoreLocationDTO(1L, "Store Location", 1L, null)));

		List<StoreLocationDTO> result = service.load(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findStoreLocationForSalesReturnsExpected() {
		when(mockStoreLocationRepository.findByFrontSaleTrueAndStoreNumberStoreNumId(1L)).thenReturn(new StoreLocationModel(1L, 1L, "", 1L));
		Long result = service.findStoreLocationForSales(1L);

		assertThat(result, is(notNullValue()));
	}
}
