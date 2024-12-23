package com.ust.retail.store.pim.common.catalogs;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.service.catalog.CatalogService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendorContactTypeCatalogTest {
	@Mock
	private CatalogService mockCatalogService;

	@InjectMocks
	private VendorContactTypeCatalog catalog;
	private CatalogDTO dto;

	@BeforeEach
	void setUp() {
		dto = new CatalogDTO(1L, "CATALOG_NAME", "CATALOG_OPTION");
	}

	@Test
	void getCatalogOptionsReturnsExpected() {
		when(mockCatalogService.findByCatalogName(any())).thenReturn(List.of(dto));

		assertThat(catalog.getCatalogOptions(), contains(dto));
	}

	@Test
	void findCatalogByIdReturnsExpected() {
		when(mockCatalogService.findCatalogById(any())).thenReturn(dto);

		assertThat(catalog.findCatalogById(1L), is(dto));
	}
}
