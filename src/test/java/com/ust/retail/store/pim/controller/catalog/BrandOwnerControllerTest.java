package com.ust.retail.store.pim.controller.catalog;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO;
import com.ust.retail.store.pim.service.catalog.BrandOwnerService;

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
class BrandOwnerControllerTest {
	private static final String BRAND_OWNER_NAME = "My Brand Owner";

	@Mock
	private BrandOwnerService mockBrandOwnerService;

	@InjectMocks
	private BrandOwnerController controller;

	@Test
	void createReturnsExpectedWheInputIsValid() {
		BrandOwnerDTO dto = new BrandOwnerDTO(null, BRAND_OWNER_NAME);
		when(mockBrandOwnerService.saveOrUpdate(any()))
				.then(invocation -> new BrandOwnerDTO(1L, invocation.<BrandOwnerDTO>getArgument(0).getBrandOwnerName()));

		BrandOwnerDTO result = controller.create(dto);

		assertThat(result, allOf(
				hasProperty("brandOwnerId", equalTo(1L)),
				hasProperty("brandOwnerName", equalTo(BRAND_OWNER_NAME))
		));
	}

	@Test
	void updateReturnsExpectedWhenInputIsValid() {
		BrandOwnerDTO dto = new BrandOwnerDTO(1L, BRAND_OWNER_NAME);
		when(mockBrandOwnerService.saveOrUpdate(any()))
				.then(invocation -> invocation.<BrandOwnerDTO>getArgument(0));

		BrandOwnerDTO result = controller.update(dto);

		assertThat(result, allOf(
				hasProperty("brandOwnerId", equalTo(1L)),
				hasProperty("brandOwnerName", equalTo(BRAND_OWNER_NAME))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenObjectFound() {
		BrandOwnerDTO dto = new BrandOwnerDTO(1L, BRAND_OWNER_NAME);
		when(mockBrandOwnerService.findById(1L)).thenReturn(dto);

		BrandOwnerDTO result = controller.findById(1L);

		assertThat(result, allOf(
				hasProperty("brandOwnerId", equalTo(1L)),
				hasProperty("brandOwnerName", equalTo(BRAND_OWNER_NAME))
		));
	}

	@Test
	void findByFiltersReturnsExpectedWhenResultsFound() {
		BrandOwnerDTO dto1 = new BrandOwnerDTO(1L, BRAND_OWNER_NAME);
		BrandOwnerDTO dto6 = new BrandOwnerDTO(6L, "BrandOwner 6");
		List<BrandOwnerDTO> searchResults = List.of(dto1, dto6);
		BrandOwnerDTO dto = new BrandOwnerDTO(null, "BrandOwner");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("brandOwnerName");
		dto.setOrderDir("desc");
		when(mockBrandOwnerService.getBrandOwnersByFilters(dto))
				.thenReturn(new PageImpl<>(searchResults));

		Page<BrandOwnerDTO> result = controller.findByFilters(dto);

		assertThat(result.getTotalElements(), is(2L));
		assertThat(result.getContent(), contains(dto1, dto6));
	}

	@Test
	void findAutoCompleteOptionsReturnExpectedWhenResultsFound() {
		BrandOwnerDTO dto1 = new BrandOwnerDTO(1L, "My BrandOwner 1");
		BrandOwnerDTO dto6 = new BrandOwnerDTO(6L, "BrandOwner 6");
		List<BrandOwnerDTO> searchResults = List.of(dto1, dto6);
		when(mockBrandOwnerService.getAutocomplete("BrandOwner")).thenReturn(searchResults);

		List<BrandOwnerDTO> result = controller.findAutoCompleteOptions("BrandOwner");

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void loadCatalogReturnExpectedWhenResultsFound() {
		BrandOwnerDTO dto1 = new BrandOwnerDTO(1L, BRAND_OWNER_NAME);
		BrandOwnerDTO dto6 = new BrandOwnerDTO(6L, "BrandOwner 6");
		List<BrandOwnerDTO> results = List.of(dto1, dto6);
		when(mockBrandOwnerService.load()).thenReturn(results);

		List<BrandOwnerDTO> result = controller.loadCatalog();

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}
}
