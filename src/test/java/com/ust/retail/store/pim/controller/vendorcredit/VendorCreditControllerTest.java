package com.ust.retail.store.pim.controller.vendorcredit;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.ust.retail.store.pim.common.catalogs.InventoryProductReturnStatusCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.productreturn.CalculateCreditsDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnFilterDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnFilterResultDTO;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationDTO;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationResultDTO;
import com.ust.retail.store.pim.service.vendorcredit.VendorCreditService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendorCreditControllerTest {
	@Mock
	private VendorCreditService mockVendorCreditService;
	@Mock
	private InventoryProductReturnStatusCatalog mockInventoryProductReturnStatusCatalog;

	@InjectMocks
	private VendorCreditController controller;

	@Test
	void loadVendorCreditStatusCatalogReturnsExpected() {
		when(mockInventoryProductReturnStatusCatalog.getCatalogOptions()).thenReturn(List.of());

		List<CatalogDTO> result = controller.loadVendorCreditStatusCatalog();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByFiltersReturnsExpected() {
		ProductReturnFilterDTO request = new ProductReturnFilterDTO();
		when(mockVendorCreditService.getVendorCreditsByFilters(request)).thenReturn(Page.empty());

		Page<ProductReturnFilterResultDTO> result = controller.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockVendorCreditService.findById(1L)).thenReturn(new ProductReturnDTO());

		ProductReturnDTO result = controller.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void authorizeCreditsReturnsExpected() {
		ProductReturnAuthorizationDTO request = new ProductReturnAuthorizationDTO();
		when(mockVendorCreditService.authorize(request)).thenReturn(new ProductReturnAuthorizationResultDTO());

		ProductReturnAuthorizationResultDTO result = controller.authorizeCredits(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void rejectCreditsReturnsExpected() {
		ProductReturnAuthorizationDTO request = new ProductReturnAuthorizationDTO();
		when(mockVendorCreditService.decline(request)).thenReturn(new ProductReturnAuthorizationResultDTO());

		ProductReturnAuthorizationResultDTO result = controller.rejectCredits(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void calculateCreditsReturnsExpected() {
		CalculateCreditsDTO request = new CalculateCreditsDTO();
		when(mockVendorCreditService.calculateCredits(request)).thenReturn(new CalculateCreditsDTO());

		CalculateCreditsDTO result = controller.calculateCredits(request);

		assertThat(result, is(notNullValue()));
	}
}
