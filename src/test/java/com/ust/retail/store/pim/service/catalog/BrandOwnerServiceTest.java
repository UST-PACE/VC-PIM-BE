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
import com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.BrandOwnerModel;
import com.ust.retail.store.pim.repository.catalog.BrandOwnerRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandOwnerServiceTest {
	@Mock
	private BrandOwnerRepository mockBrandOwnerRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private BrandOwnerService service;

	@Test
	void saveOrUpdateReturnsExpected() {
		BrandOwnerDTO request = new BrandOwnerDTO(null, "Brand");
		when(mockBrandOwnerRepository.save(any())).then(invocation -> invocation.getArgument(0));

		BrandOwnerDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockBrandOwnerRepository.findById(1L)).thenReturn(Optional.of(new BrandOwnerModel(1L)));

		BrandOwnerDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenStoreNumberNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void getBrandOwnersByFiltersReturnsExpected() {
		BrandOwnerDTO request = new BrandOwnerDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");

		when(mockBrandOwnerRepository.findByFilters(any(), any())).thenReturn(new PageImpl<>(List.of()));

		Page<BrandOwnerDTO> result = service.getBrandOwnersByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getAutocompleteReturnsExpected() {
		List<BrandOwnerDTO> result = service.getAutocomplete("Brand");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadReturnsExpected() {
		when(mockBrandOwnerRepository.findAll()).thenReturn(List.of(new BrandOwnerModel(1L)));

		List<BrandOwnerDTO> result = service.load();

		assertThat(result, is(notNullValue()));
	}
}
