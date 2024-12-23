package com.ust.retail.store.pim.service.external;

import java.util.List;
import java.util.Optional;

import com.ust.retail.store.pim.service.tax.TaxService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.dto.external.product.ExternalProductByStoreAndSkuListRequest;
import com.ust.retail.store.pim.dto.external.product.ExternalProductByStoreRequest;
import com.ust.retail.store.pim.dto.external.product.ExternalProductDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.inventory.InventoryRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalProductServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private InventoryRepository mockInventoryRepository;
	@Mock
	private UnitConverter mockUnitConverter;
	@Mock
	private TaxService mockTaxService;

	@InjectMocks
	private ExternalProductService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ExternalProductServiceTest.class);
	}

	@Test
	void findByFiltersReturnsExpected() {
		ExternalProductByStoreRequest request = new ExternalProductByStoreRequest();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");

		UpcMasterModel product = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());
		when(mockUpcMasterRepository.findByStoreAndFilters(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(),any())).thenReturn(new PageImpl<>(List.of(product)));

		List<ExternalProductDTO> result = service.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByStoreAndSkuListReturnsExpected() {
		InventoryModel inventory = fixtureLoader.getObject("inventory", InventoryModel.class).orElse(new InventoryModel());
		when(mockInventoryRepository.findByStoreNumIdAndPrincipalUpcIn(any(), any(), any(), any(), any())).thenReturn(List.of(inventory));

		ExternalProductByStoreAndSkuListRequest request = new ExternalProductByStoreAndSkuListRequest();

		List<ExternalProductDTO> result = service.findByStoreAndSkuList(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateImageTrainedCompletesSuccessfully() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);

		assertDoesNotThrow(() -> service.updateImageTrained(1L, true));
	}

	@Test
	void updateImageTrainedThrowsExceptionWhenProductNotFound() {

		assertThrows(ResourceNotFoundException.class, () -> service.updateImageTrained(1L, true));
	}
}
