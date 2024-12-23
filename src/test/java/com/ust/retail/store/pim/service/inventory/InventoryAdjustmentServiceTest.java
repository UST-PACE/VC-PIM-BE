package com.ust.retail.store.pim.service.inventory;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventoryAdjustmentDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.InventoryAdjustmentSumaryDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.screens.StartDailyCountDTO;
import com.ust.retail.store.pim.exceptions.InvalidInventoryAdjustmentReferenceException;
import com.ust.retail.store.pim.exceptions.InvalidStatusChangeException;
import com.ust.retail.store.pim.exceptions.InvalidUPCException;
import com.ust.retail.store.pim.exceptions.InventoryAdjustmentProcessInProgressException;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentCategoryModel;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.inventory.InventoryAdjustmentRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryAdjustmentSubcategoryRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryAdjustmentServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private InventoryAdjustmentRepository mockInventoryAdjustmentRepository;
	@Mock
	private InventoryAdjustmentSubcategoryRepository mockInventoryAdjustmentSubcategoryRepository;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private InventoryAdjustmentService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(InventoryAdjustmentServiceTest.class);
	}

	@Test
	void startReturnsExpectedWhenNoActiveCountExists() {
		StartDailyCountDTO request = fixtureLoader.getObject("startRequest", StartDailyCountDTO.class).orElse(new StartDailyCountDTO());
		when(mockInventoryAdjustmentRepository.save(any())).then(invocation -> invocation.getArgument(0));

		InventoryAdjustmentDTO result = service.start(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void startThrowsExceptionWhenActiveCountExists() {
		StartDailyCountDTO request = fixtureLoader.getObject("startRequest", StartDailyCountDTO.class).orElse(new StartDailyCountDTO());
		when(mockInventoryAdjustmentSubcategoryRepository.getActiveCountsBySubcategory(1L, List.of(1L), 14000L)).thenReturn(List.of(new InventoryAdjustmentCategoryModel(new InventoryAdjustmentModel(1L), 1L, 14000L)));

		assertThrows(InventoryAdjustmentProcessInProgressException.class, () -> service.start(request));
	}

	@Test
	void interruptReturnsExpected() {
		StartDailyCountDTO request = fixtureLoader.getObject("startRequest", StartDailyCountDTO.class).orElse(new StartDailyCountDTO());
		when(mockInventoryAdjustmentSubcategoryRepository.getActiveCountsBySubcategory(1L, List.of(1L), 14000L)).thenReturn(List.of(new InventoryAdjustmentCategoryModel(new InventoryAdjustmentModel(1L), 1L, 14000L)));

		Boolean result = service.interrupt(request);

		assertThat(result, is(true));
	}

	@Test
	void interruptThrowsExceptionWhenNoActiveProcessExists() {
		StartDailyCountDTO request = fixtureLoader.getObject("startRequest", StartDailyCountDTO.class).orElse(new StartDailyCountDTO());

		assertThrows(InvalidStatusChangeException.class, () -> service.interrupt(request));
	}

	@Test
	void adjustInventoryReturnsExpected() {
		InventoryAdjustmentDTO request = fixtureLoader.getObject("adjustRequest", InventoryAdjustmentDTO.class).orElse(new InventoryAdjustmentDTO());
		Optional<InventoryAdjustmentModel> inventoryAdjustmentModel = fixtureLoader.getObject("adjustmentModel", InventoryAdjustmentModel.class);

		when(mockInventoryAdjustmentRepository.findById(3L)).thenReturn(inventoryAdjustmentModel);
		when(mockInventoryAdjustmentRepository.save(any())).then(invocation -> invocation.getArgument(0));

		Boolean result = service.adjustInventory(request);

		assertThat(result, is(true));
	}

	@Test
	void adjustInventoryThrowsExceptionWhenAdjustmentNotExists() {
		InventoryAdjustmentDTO request = fixtureLoader.getObject("adjustRequest", InventoryAdjustmentDTO.class).orElse(new InventoryAdjustmentDTO());

		assertThrows(InvalidInventoryAdjustmentReferenceException.class, () -> service.adjustInventory(request));
	}

	@Test
	void getAdjustmentSumaryReturnsExpected() {
		Optional<InventoryAdjustmentModel> inventoryAdjustmentModel = fixtureLoader.getObject("adjustmentModel", InventoryAdjustmentModel.class);
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockInventoryAdjustmentRepository.findById(3L)).thenReturn(inventoryAdjustmentModel);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);

		InventoryAdjustmentSumaryDTO result = service.getAdjustmentSumary(3L);

		assertThat(result.getSummary(), is(notNullValue()));
	}

	@Test
	void getAdjustmentSumaryThrowsExceptionWhenAdjustmentNotFound() {
		assertThrows(InvalidInventoryAdjustmentReferenceException.class, () -> service.getAdjustmentSumary(3L));
	}

	@Test
	void getAdjustmentSumaryThrowsExceptionWhenProductNotFound() {
		Optional<InventoryAdjustmentModel> inventoryAdjustmentModel = fixtureLoader.getObject("adjustmentModel", InventoryAdjustmentModel.class);
		when(mockInventoryAdjustmentRepository.findById(3L)).thenReturn(inventoryAdjustmentModel);

		assertThrows(InvalidUPCException.class, () -> service.getAdjustmentSumary(3L));
	}

	@Test
	void finishReturnsExpected() {
		Optional<InventoryAdjustmentModel> inventoryAdjustmentModel = fixtureLoader.getObject("adjustmentModel", InventoryAdjustmentModel.class);
		when(mockInventoryAdjustmentRepository.findById(3L)).thenReturn(inventoryAdjustmentModel);

		boolean result = service.finish(3L);

		assertThat(result, is(true));
	}

	@Test
	void finishThrowsExceptionWheAdjustmentNotFound() {
		assertThrows(InvalidInventoryAdjustmentReferenceException.class, () -> service.finish(3L));
	}
}
