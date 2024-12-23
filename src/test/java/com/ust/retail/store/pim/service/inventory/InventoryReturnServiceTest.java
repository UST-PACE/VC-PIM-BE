package com.ust.retail.store.pim.service.inventory;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.inventory.returns.operation.ReturnDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.InventoryReturnSumaryDTO;
import com.ust.retail.store.pim.dto.inventory.returns.screen.ReturnItemDTO;
import com.ust.retail.store.pim.exceptions.InvalidInventoryReturnReferenceException;
import com.ust.retail.store.pim.exceptions.InvalidUPCException;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.inventory.InventoryProductReturnRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryReturnServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private InventoryProductReturnRepository mockInventoryProductReturnRepository;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private InventoryReturnService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(InventoryReturnServiceTest.class);
	}

	@Test
	void startReturnsExpected() {
		ReturnDTO request = fixtureLoader.getObject("startRequest", ReturnDTO.class).orElse(new ReturnDTO());

		when(mockInventoryProductReturnRepository.save(any())).then(invocation -> invocation.getArgument(0));

		ReturnDTO result = service.start(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void returnItemsReturnsExpected() {
		ReturnItemDTO request = fixtureLoader.getObject("request", ReturnItemDTO.class).orElse(new ReturnItemDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		Optional<InventoryProductReturnModel> inventoryReturn = fixtureLoader.getObject("inventoryReturn", InventoryProductReturnModel.class);

		when(mockUpcMasterRepository.findByPrincipalUpc("CODE")).thenReturn(product);
		when(mockInventoryProductReturnRepository.findById(1L)).thenReturn(inventoryReturn);
		when(mockInventoryProductReturnRepository.save(any())).then(invocation -> invocation.getArgument(0));

		Boolean result = service.returnItems(request);

		assertThat(result, is(true));
	}

	@Test
	void returnItemsThrowsExceptionWhenProductNotFound() {
		ReturnItemDTO request = fixtureLoader.getObject("request", ReturnItemDTO.class).orElse(new ReturnItemDTO());

		assertThrows(InvalidUPCException.class, () -> service.returnItems(request));
	}

	@Test
	void returnItemsThrowsExceptionWhenProductReturnNotFound() {
		ReturnItemDTO request = fixtureLoader.getObject("request", ReturnItemDTO.class).orElse(new ReturnItemDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		when(mockUpcMasterRepository.findByPrincipalUpc("CODE")).thenReturn(product);

		assertThrows(InvalidInventoryReturnReferenceException.class, () -> service.returnItems(request));
	}

	@Test
	void getReturnSummaryReturnsExpected() {
		Optional<InventoryProductReturnModel> inventoryReturn = fixtureLoader.getObject("inventoryReturn", InventoryProductReturnModel.class);
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);
		when(mockUpcMasterRepository.findByPrincipalUpc("CODE")).thenReturn(product);
		when(mockInventoryProductReturnRepository.findById(1L)).thenReturn(inventoryReturn);

		InventoryReturnSumaryDTO result = service.getReturnSummary(1L);

		assertThat(result, is(notNullValue()));
		assertThat(result.getSumary(), Matchers.hasEntry(is("category1"), hasSize(1)));
	}

	@Test
	void getReturnSummaryThrowsExceptionWhenReturnNotFound() {
		assertThrows(InvalidInventoryReturnReferenceException.class, () -> service.getReturnSummary(1L));
	}

	@Test
	void getReturnSummaryThrowsExceptionWhenReturnProductNotFound() {
		Optional<InventoryProductReturnModel> inventoryReturn = fixtureLoader.getObject("inventoryReturn", InventoryProductReturnModel.class);
		when(mockInventoryProductReturnRepository.findById(1L)).thenReturn(inventoryReturn);

		assertThrows(InvalidUPCException.class, () -> service.getReturnSummary(1L));
	}

	@Test
	void finishReturnsExpected() {
		Optional<InventoryProductReturnModel> inventoryReturn = fixtureLoader.getObject("inventoryReturn", InventoryProductReturnModel.class);
		when(mockInventoryProductReturnRepository.findById(1L)).thenReturn(inventoryReturn);

		boolean result = service.finish(1L);

		assertThat(result, is(true));
	}

	@Test
	void finishThrowsExceptionWhenReturnNotFound() {
		assertThrows(InvalidInventoryReturnReferenceException.class, () -> service.finish(1L));
	}
}
