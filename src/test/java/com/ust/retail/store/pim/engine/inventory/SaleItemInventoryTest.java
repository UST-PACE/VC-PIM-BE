package com.ust.retail.store.pim.engine.inventory;

import java.util.List;
import java.util.Optional;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.util.FixtureLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.QtyOperationResultDTO;
import com.ust.retail.store.pim.exceptions.SalesInventoryException;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.repository.inventory.InventoryHistoryRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleItemInventoryTest {
	private static FixtureLoader fixtureLoader;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(SaleItemInventoryTest.class);
	}

	@Mock
	private InventoryRepository mockInventoryRepository;
	@Mock
	private InventoryHistoryRepository mockInventoryHistoryRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;
	@Mock
	private UnitConverter mockUnitConverter;

	@InjectMocks
	private SaleItemInventory engine;

	@Test
	void executeCompletesSuccessfully() {
		List<Item> items = List.of(new Item(30d, 1L, 1L));
		List<Item> shrinkageItems = null;
		when(mockInventoryRepository.save(any())).then(invocation -> invocation.getArgument(0));
		Optional<InventoryModel> inventory = fixtureLoader.getObject("inventory", InventoryModel.class);
		when(mockInventoryRepository.findByUpcMasterUpcMasterIdAndStoreLocationStoreLocationId(1L, 1L))
				.thenReturn(inventory);

		assertDoesNotThrow(() -> engine.execute(items, shrinkageItems, 1L));
	}

	@Test
	void executeThrowsExceptionWhenOutOfStock() {
		List<Item> items = List.of(new Item(60d, 1L, 1L));
		List<Item> shrinkageItems = null;
		Optional<InventoryModel> inventory = fixtureLoader.getObject("inventory", InventoryModel.class);
		when(mockInventoryRepository.findByUpcMasterUpcMasterIdAndStoreLocationStoreLocationId(1L, 1L))
				.thenReturn(inventory);
		when(mockUnitConverter.convert(any(), any(), any())).then(invocation -> invocation.getArgument(2));

		assertThrows(SalesInventoryException.class, () -> engine.execute(items, shrinkageItems, 1L));
	}

	@Test
	void getItemFinalQtyReturnsExpected() {
		QtyOperationResultDTO result = engine.getItemFinalQty(null, null, false);

		assertThat(result, is(nullValue()));
	}
}
