package com.ust.retail.store.pim.service.vendorcredit;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.pim.dto.productreturn.CalculateCreditsDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnDetailDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnFilterDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnFilterResultDTO;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationDTO;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationResultDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcNotSuppliedByVendorException;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnDetailModel;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import com.ust.retail.store.pim.model.vendorcredits.VendorCreditModel;
import com.ust.retail.store.pim.repository.inventory.InventoryProductReturnDetailRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryProductReturnRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcVendorDetailsRepository;
import com.ust.retail.store.pim.repository.vendorcredits.VendorCreditRepository;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendorCreditServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private InventoryProductReturnRepository mockInventoryProductReturnRepository;
	@Mock
	private InventoryProductReturnDetailRepository mockInventoryProductReturnDetailRepository;
	@Mock
	private VendorCreditRepository mockVendorCreditRepository;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private UpcVendorDetailsRepository mockUpcVendorDetailsRepository;

	@InjectMocks
	private VendorCreditService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(VendorCreditServiceTest.class);
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<InventoryProductReturnModel> productReturn = fixtureLoader.getObject("productReturn", InventoryProductReturnModel.class);

		when(mockInventoryProductReturnRepository.findById(1L)).thenReturn(productReturn);

		ProductReturnDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenIdNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void getVendorCreditsByFiltersReturnsExpected() {
		ProductReturnFilterDTO request = new ProductReturnFilterDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");

		InventoryProductReturnModel productReturn = fixtureLoader.getObject("productReturn", InventoryProductReturnModel.class).orElse(new InventoryProductReturnModel());

		when(mockInventoryProductReturnRepository.findByFilters(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(new PageImpl<>(List.of(productReturn)));

		Page<ProductReturnFilterResultDTO> result = service.getVendorCreditsByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void authorizeReturnsExpected() {
		ProductReturnAuthorizationDTO request = fixtureLoader.getObject("productReturnRequest", ProductReturnAuthorizationDTO.class).orElse(new ProductReturnAuthorizationDTO());
		Optional<UpcVendorDetailsModel> upcVendorDetailsModel = fixtureLoader.getObject("vendorDetails", UpcVendorDetailsModel.class);
		Optional<InventoryProductReturnDetailModel> inventoryProductReturnDetailModel = fixtureLoader.getObject("productReturnDetailModel", InventoryProductReturnDetailModel.class);
		when(mockInventoryProductReturnDetailRepository.findById(any())).then(invocation -> {
			if (!Objects.equals(invocation.<Long>getArgument(0), 1L)) {
				return inventoryProductReturnDetailModel;
			}
			return Optional.empty();
		});

		when(mockInventoryService.authorizeByInventoryHistoryId(any())).thenReturn(true, false);
		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L))
				.thenReturn(upcVendorDetailsModel);
		when(mockVendorCreditRepository.findByVendorMasterVendorMasterId(1L)).thenReturn(new VendorCreditModel());
		when(mockInventoryProductReturnRepository.findCreditDetailWithStatus(any(), any(), any())).thenReturn(List.of(new ProductReturnDetailDTO()));
		when(mockInventoryProductReturnRepository.findById(any())).thenReturn(Optional.of(new InventoryProductReturnModel()));

		ProductReturnAuthorizationResultDTO result = service.authorize(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void declineReturnsExpected() {
		ProductReturnAuthorizationDTO request = fixtureLoader.getObject("productReturnRequest", ProductReturnAuthorizationDTO.class).orElse(new ProductReturnAuthorizationDTO());
		Optional<InventoryProductReturnDetailModel> inventoryProductReturnDetailModel = fixtureLoader.getObject("productReturnDetailModel", InventoryProductReturnDetailModel.class);
		when(mockInventoryProductReturnDetailRepository.findById(any())).then(invocation -> {
			if (!Objects.equals(invocation.<Long>getArgument(0), 1L)) {
				return inventoryProductReturnDetailModel;
			}
			return Optional.empty();
		});

		List<ProductReturnDetailDTO> returnDetailsWithNotPendingStatus = fixtureLoader.getObject("returnDetailsWithNotPendingStatus", new ReturnDetailListReference()).orElse(List.of());
		when(mockInventoryProductReturnRepository.findCreditDetailWithStatus(any(), any(), any())).thenReturn(returnDetailsWithNotPendingStatus);
		when(mockInventoryProductReturnRepository.findById(any())).thenReturn(Optional.of(new InventoryProductReturnModel()));

		ProductReturnAuthorizationResultDTO result = service.decline(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void calculateCreditReturnsExpected() {
		CalculateCreditsDTO request = fixtureLoader.getObject("calculateCreditRequest", CalculateCreditsDTO.class).orElse(new CalculateCreditsDTO());
		Optional<UpcVendorDetailsModel> upcVendorDetailsModel = fixtureLoader.getObject("vendorDetails", UpcVendorDetailsModel.class);

		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L)).thenReturn(upcVendorDetailsModel);
		Optional<InventoryProductReturnDetailModel> inventoryProductReturnDetailModel = fixtureLoader.getObject("productReturnDetailModel", InventoryProductReturnDetailModel.class);
		when(mockInventoryProductReturnDetailRepository.findById(any())).thenReturn(inventoryProductReturnDetailModel);

		CalculateCreditsDTO result = service.calculateCredits(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void calculateCreditThrowsExceptionWhenVendorDetailNotFound() {
		CalculateCreditsDTO request = fixtureLoader.getObject("calculateCreditRequest", CalculateCreditsDTO.class).orElse(new CalculateCreditsDTO());

		assertThrows(UpcNotSuppliedByVendorException.class, () -> service.calculateCredits(request));
	}

	@Test
	void calculateCreditThrowsExceptionWhenReturnItemNotFound() {
		CalculateCreditsDTO request = fixtureLoader.getObject("calculateCreditRequest", CalculateCreditsDTO.class).orElse(new CalculateCreditsDTO());
		Optional<UpcVendorDetailsModel> upcVendorDetailsModel = fixtureLoader.getObject("vendorDetails", UpcVendorDetailsModel.class);

		when(mockUpcVendorDetailsRepository.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(1L, 1L)).thenReturn(upcVendorDetailsModel);

		assertThrows(UpcNotSuppliedByVendorException.class, () -> service.calculateCredits(request));
	}

	private static class ReturnDetailListReference extends TypeReference<List<ProductReturnDetailDTO>> {
	}
}
