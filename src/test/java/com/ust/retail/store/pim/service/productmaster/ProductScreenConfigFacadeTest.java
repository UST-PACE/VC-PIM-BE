package com.ust.retail.store.pim.service.productmaster;

import java.util.List;

import com.ust.retail.store.pim.common.catalogs.*;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.dto.catalog.BrandOwnerDTO;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.catalog.ProductGroupDTO;
import com.ust.retail.store.pim.dto.upcmaster.ProductFilterScreenConfigDTO;
import com.ust.retail.store.pim.dto.upcmaster.ProductScreenConfigDTO;
import com.ust.retail.store.pim.service.catalog.BrandOwnerService;
import com.ust.retail.store.pim.service.catalog.ProductGroupService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class ProductScreenConfigFacadeTest {
	@Mock
	private BrandOwnerService mockBrandOwnerService;
	@Mock
	private ProductTypeCatalog mockProductTypeCatalog;
	@Mock
	private ProductGroupService mockProductGroupService;
	@Mock
	private UpcMasterTypeCatalog mockUpcMasterTypeCatalog;
	@Mock
	private ProductUnitCatalog mockProductUnitCatalog;
	@Mock
	private CountryCatalog mockCountryCatalog;
	@Mock
	private PackageTypeCatalog mockPackageTypeCatalog;
	@Mock
	private MainEntryTypeCatalog mockMainEntryTypeCatalog;
	@Mock
	private UpcSellingChannelCatalog mockUpcSellingChannelCatalog;
	@Mock
	private UnitConverter mockUnitConverter;

	@InjectMocks
	private ProductScreenConfigFacade facade;

	@Test
	void getScreenConfigReturnsExpected() {
		ProductScreenConfigDTO result = facade.getScreenConfig();

		MatcherAssert.assertThat(result, Matchers.allOf(
				hasProperty("brandOwnerList", emptyCollectionOf(BrandOwnerDTO.class)),
				hasProperty("productTypeList", emptyCollectionOf(CatalogDTO.class)),
				hasProperty("productGroupList", emptyCollectionOf(ProductGroupDTO.class)),
				hasProperty("productUnitList", emptyCollectionOf(CatalogDTO.class)),
				hasProperty("countryOfOriginList", emptyCollectionOf(CatalogDTO.class)),
				hasProperty("packageTypeList", emptyCollectionOf(CatalogDTO.class))
		));
	}

	@Test
	void getScreenConfigReturnsExpectedUnitList() {
		when(mockProductUnitCatalog.getCatalogOptions())
				.thenReturn(List.of(
						new CatalogDTO(105L, "BUYING_SELLING_UNIT", "ft"),
						new CatalogDTO(128L, "BUYING_SELLING_UNIT", "m"),
						new CatalogDTO(129L, "BUYING_SELLING_UNIT", "in")
				));
		ProductScreenConfigDTO result = facade.getScreenConfig();

		assertThat(result, hasProperty("productUnitList", hasSize(3)));
	}

	@Test
	void getFilterScreenConfigReturnsExpected() {
		ProductFilterScreenConfigDTO result = facade.getFilterScreenConfig();

		assertThat(result, allOf(
				hasProperty("brandOwnerList", emptyCollectionOf(BrandOwnerDTO.class)),
				hasProperty("productTypeList", emptyCollectionOf(CatalogDTO.class))
		));
	}
}
