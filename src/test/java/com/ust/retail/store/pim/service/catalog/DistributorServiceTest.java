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
import com.ust.retail.store.pim.dto.catalog.DistributorDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.DistributorModel;
import com.ust.retail.store.pim.repository.catalog.DistributorRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DistributorServiceTest {
	@Mock
	private DistributorRepository mockDistributorRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private DistributorService service;

	@Test
	void saveOrUpdateReturnsExpected() {
		DistributorDTO request = new DistributorDTO(null, "Distributor");
		when(mockDistributorRepository.save(any())).then(invocation -> invocation.getArgument(0));

		DistributorDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockDistributorRepository.findById(1L)).thenReturn(Optional.of(new DistributorModel(1L)));

		DistributorDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenStoreNumberNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void getDistributorsByFiltersReturnsExpected() {
		DistributorDTO request = new DistributorDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");

		when(mockDistributorRepository.findByFilters(any(), any())).thenReturn(new PageImpl<>(List.of()));

		Page<DistributorDTO> result = service.getDistributorsByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getAutocompleteReturnsExpected() {
		List<DistributorDTO> result = service.getAutocomplete("Brand");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadReturnsExpected() {
		when(mockDistributorRepository.findAll()).thenReturn(List.of(new DistributorModel(1L)));

		List<DistributorDTO> result = service.load();

		assertThat(result, is(notNullValue()));
	}
}
