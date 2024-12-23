package com.ust.retail.store.pim.engine.inventory;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.inventory.QtyOperationResultDTO;
import com.ust.retail.store.pim.repository.inventory.InventoryHistoryRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryAdjustmentTest {
	@Mock
	private InventoryRepository mockInventoryRepository;
	@Mock
	private InventoryHistoryRepository mockInventoryHistoryRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private InventoryAdjustment engine;

	@Test
	void executeCompletesSuccessfully() {
		List<Item> items = List.of(new Item(30d, 1L, 1L));
		List<Item> shrinkageItems = List.of(new Item(2d, 1L, 1L, true));
		when(mockInventoryRepository.save(any())).then(invocation -> invocation.getArgument(0));

		assertDoesNotThrow(() -> engine.execute(items, shrinkageItems, 1L));
	}

	@Test
	void getItemFinalQtyReturnsExpected() {
		QtyOperationResultDTO result = engine.getItemFinalQty(null, null);

		assertThat(result, is(nullValue()));
	}
}
