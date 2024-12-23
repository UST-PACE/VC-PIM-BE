package com.ust.retail.store.pim.service.inventory;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.inventory.InventoryProductDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryTransferencesDTO;
import com.ust.retail.store.pim.dto.inventory.InventoryTransferencesFiltersDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.InventoryTransferModel;
import com.ust.retail.store.pim.repository.inventory.InventoryTransferRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryTransferenceServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private InventoryTransferRepository mockInventoryTransferRepository;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private InventoryTransferenceService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(InventoryTransferenceServiceTest.class);
	}

	@Test
	void saveOrUpdateReturnsExpected() {
		InventoryProductDTO inventoryProduct = fixtureLoader.getObject("inventoryProduct", InventoryProductDTO.class).orElse(new InventoryProductDTO());
		when(mockInventoryService.findInventoryByCodeAndStoreLocation("CODE", 1L)).thenReturn(inventoryProduct);
		when(mockInventoryTransferRepository.save(any())).then(invocation -> invocation.getArgument(0));

		InventoryTransferencesDTO request = fixtureLoader.getObject("request", InventoryTransferencesDTO.class).orElse(new InventoryTransferencesDTO());

		InventoryTransferencesDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<InventoryTransferModel> inventoryTransfer = fixtureLoader.getObject("inventoryTransfer", InventoryTransferModel.class);
		when(mockInventoryTransferRepository.findById(1L)).thenReturn(inventoryTransfer);

		InventoryTransferencesDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenTransferNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void getTransferenceByFiltersReturnsExpected() {
		InventoryTransferencesFiltersDTO request = new InventoryTransferencesFiltersDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");
		when(mockInventoryTransferRepository.findByFilters(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of()));

		Page<InventoryTransferencesFiltersDTO> result = service.getTransferenceByFilters(request);

		assertThat(result.getContent(), hasSize(0));
	}
}
