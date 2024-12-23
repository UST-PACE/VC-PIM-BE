package com.ust.retail.store.pim.controller.promotion;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.catalogs.DiscountsCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionFilterDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionFilterResultDTO;
import com.ust.retail.store.pim.service.promotion.PromotionService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromotionControllerTest {
	@Mock
	private PromotionService mockPromotionService;
	@Mock
	private DiscountsCatalog mockDiscountsCatalog;

	@InjectMocks
	private PromotionController controller;

	@Test
	void createReturnsExpected() {
		PromotionDTO request = new PromotionDTO();
		when(mockPromotionService.saveOrUpdate(request)).thenReturn(new PromotionDTO());

		PromotionDTO result = controller.create(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		PromotionDTO request = new PromotionDTO();
		when(mockPromotionService.saveOrUpdate(request)).thenReturn(new PromotionDTO());

		PromotionDTO result = controller.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void deleteByIdReturnsExpected() {
		GenericResponse result = controller.deleteById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockPromotionService.findById(1L)).thenReturn(new PromotionDTO());

		PromotionDTO result = controller.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByFiltersReturnsExpected() {
		PromotionFilterDTO request = new PromotionFilterDTO();
		when(mockPromotionService.getPromotionsByFilters(request)).thenReturn(Page.empty());

		Page<PromotionFilterResultDTO> result = controller.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getPromotionTypesReturnsExpected() {
		when(mockDiscountsCatalog.getCatalogOptions()).thenReturn(List.of());

		List<CatalogDTO> result = controller.getPromotionTypes();

		assertThat(result, is(notNullValue()));
	}
}
