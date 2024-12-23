package com.ust.retail.store.pim.controller.catalog;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.dto.catalog.DistributorDTO;
import com.ust.retail.store.pim.service.catalog.DistributorService;

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
class DistributorControllerTest {
	private static final String DISTRIBUTOR_NAME = "My Distributor";

	@Mock
	private DistributorService mockDistributorService;

	@InjectMocks
	private DistributorController controller;

	@Test
	void createReturnsExpectedWheInputIsValid() {
		DistributorDTO dto = new DistributorDTO(null, DISTRIBUTOR_NAME);
		when(mockDistributorService.saveOrUpdate(any()))
				.then(invocation -> new DistributorDTO(1L, invocation.<DistributorDTO>getArgument(0).getDistributorName()));

		DistributorDTO result = controller.create(dto);

		assertThat(result, allOf(
				hasProperty("distributorId", equalTo(1L)),
				hasProperty("distributorName", equalTo(DISTRIBUTOR_NAME))
		));
	}

	@Test
	void updateReturnsExpectedWhenInputIsValid() {
		DistributorDTO dto = new DistributorDTO(1L, DISTRIBUTOR_NAME);
		when(mockDistributorService.saveOrUpdate(any()))
				.then(invocation -> invocation.<DistributorDTO>getArgument(0));

		DistributorDTO result = controller.update(dto);

		assertThat(result, allOf(
				hasProperty("distributorId", equalTo(1L)),
				hasProperty("distributorName", equalTo(DISTRIBUTOR_NAME))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenObjectFound() {
		DistributorDTO dto = new DistributorDTO(1L, DISTRIBUTOR_NAME);
		when(mockDistributorService.findById(1L)).thenReturn(dto);

		DistributorDTO result = controller.findById(1L);

		assertThat(result, allOf(
				hasProperty("distributorId", equalTo(1L)),
				hasProperty("distributorName", equalTo(DISTRIBUTOR_NAME))
		));
	}

	@Test
	void findByFiltersReturnsExpectedWhenResultsFound() {
		DistributorDTO dto1 = new DistributorDTO(1L, DISTRIBUTOR_NAME);
		DistributorDTO dto6 = new DistributorDTO(6L, "Distributor 6");
		List<DistributorDTO> searchResults = List.of(dto1, dto6);
		DistributorDTO dto = new DistributorDTO(null, "Distributor");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("distributorName");
		dto.setOrderDir("desc");
		when(mockDistributorService.getDistributorsByFilters(dto))
				.thenReturn(new PageImpl<>(searchResults));

		Page<DistributorDTO> result = controller.findByFilters(dto);

		assertThat(result.getTotalElements(), is(2L));
		assertThat(result.getContent(), contains(dto1, dto6));
	}

	@Test
	void findAutoCompleteOptionsReturnExpectedWhenResultsFound() {
		DistributorDTO dto1 = new DistributorDTO(1L, "My Distributor 1");
		DistributorDTO dto6 = new DistributorDTO(6L, "Distributor 6");
		List<DistributorDTO> searchResults = List.of(dto1, dto6);
		when(mockDistributorService.getAutocomplete("Distributor")).thenReturn(searchResults);

		List<DistributorDTO> result = controller.findAutoCompleteOptions("Distributor");

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void loadCatalogReturnExpectedWhenResultsFound() {
		DistributorDTO dto1 = new DistributorDTO(1L, DISTRIBUTOR_NAME);
		DistributorDTO dto6 = new DistributorDTO(6L, "Distributor 6");
		List<DistributorDTO> results = List.of(dto1, dto6);
		when(mockDistributorService.load()).thenReturn(results);

		List<DistributorDTO> result = controller.loadCatalog();

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}
}
