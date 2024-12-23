package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.common.catalogs.ProductUnitCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductUnitControllerTest {

	@Mock
	private UnitConverter mockUnitConverter;
	@Mock
	private ProductUnitCatalog mockProductUnitCatalog;

	@InjectMocks
	private ProductUnitController controller;

	@Test
	void loadUnitCatalogForIdReturnsExpected() {
		when(mockUnitConverter.findConvertableUnitsFor(1L))
				.thenReturn(List.of(
						new CatalogDTO(1L, null, null),
						new CatalogDTO(2L, null, null)));
		List<CatalogDTO> result = controller.loadUnitCatalogForId(1L);

		assertThat(result, hasSize(2));
		assertThat(result, contains(List.of(
				hasProperty("catalogId", is(1L)),
				hasProperty("catalogId", is(2L)))));
	}

	@Test
	void loadUnitCatalogForIdReturnsExpectedWhenNonConvertableUnit() {
		when(mockProductUnitCatalog.findCatalogById(1L))
				.thenReturn(new CatalogDTO(1L, null, null));

		List<CatalogDTO> result = controller.loadUnitCatalogForId(1L);

		assertThat(result, hasSize(1));
		assertThat(result, contains(List.of(
				hasProperty("catalogId", is(1L)))));
	}
}
