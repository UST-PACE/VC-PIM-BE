package com.ust.retail.store.bistro.service.kitchen;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionTossDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionWastageLineDTO;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.bistro.model.wastage.WastageModel;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.bistro.repository.wastage.WastageRepository;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.StoreFacade;
import com.ust.retail.store.pim.engine.inventory.BistroProduceInventory;
import com.ust.retail.store.pim.engine.inventory.BistroTossingInventory;
import com.ust.retail.store.pim.engine.inventory.BistroWasteWholeDishInventory;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.repository.inventory.InventoryRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KitchenServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private WastageRepository mockWastageRepository;
	@Mock
	private RecipeRepository mockRecipeRepository;
	@Mock
	private InventoryRepository mockInventoryRepository;
	@Mock
	private BistroWasteWholeDishInventory mockBistroWasteWholeDishInventory;
	@Mock
	private BistroTossingInventory mockBistroTossingInventory;
	@Mock
	private BistroProduceInventory mockBistroProduceInventory;
	@Mock
	private EntityManager mockEntityManager;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;
	@Mock
	private StoreFacade mockStoreFacade;

	@InjectMocks
	private KitchenService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(KitchenServiceTest.class);
	}

/*
	@Test
	void loadExecutionForReturnsExpected() {
		List<InventoryModel> inventoryList = fixtureLoader.getObject("inventoryList", new InventoryListReference()).orElse(List.of());
		when(mockInventoryRepository.findByStoreLocationIdAndUpcMasterIdIn(any(), any())).thenReturn(inventoryList);

		KitchenExecutionDTO result = service.loadExecutionFor(new Date());

		assertThat(result, is(notNullValue()));
	}
*/

/*
	@Test
	void loadExecutionForThrowsExceptionWhenNoProduceFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.loadExecutionFor(new Date()));
	}
*/

	@Test
	void registerWastageCompletesSuccessfully() {
		KitchenExecutionWastageLineDTO request = fixtureLoader.getObject("wastageRequest", KitchenExecutionWastageLineDTO.class).orElse(new KitchenExecutionWastageLineDTO());

		assertDoesNotThrow(() -> service.registerWastage(request));
	}

	@Test
	void registerWastageDecreasesInventoryWhenWholeDish() {
		RecipeModel recipe = fixtureLoader.getObject("recipe", RecipeModel.class).orElse(new RecipeModel());
		Mockito.doAnswer(invocation -> {
			WastageModel wastage = invocation.getArgument(0);
			ReflectionTestUtils.setField(wastage, "recipe", recipe);
			return wastage;
		}).when(mockEntityManager).refresh(any());
		KitchenExecutionWastageLineDTO request = fixtureLoader.getObject("wastageRequestWholeDish", KitchenExecutionWastageLineDTO.class).orElse(new KitchenExecutionWastageLineDTO());

		assertDoesNotThrow(() -> service.registerWastage(request));
	}

	@Test
	void deleteWastageCompletesSuccessfully() {
		Optional<WastageModel> wastage = fixtureLoader.getObject("wastage", WastageModel.class);
		when(mockWastageRepository.findById(1L)).thenReturn(wastage);

		assertDoesNotThrow(() -> service.deleteWastage(1L));
	}

	@Test
	void deleteWastageIncreasesInventoryWhenWasWholeDish() {
		Optional<WastageModel> wastage = fixtureLoader.getObject("wastageWholeDish", WastageModel.class);
		when(mockWastageRepository.findById(1L)).thenReturn(wastage);

		assertDoesNotThrow(() -> service.deleteWastage(1L));
	}

	@Test
	void deleteWastageThrowsExceptionWhenResourceNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.deleteWastage(1L));
	}

	@Test
	void registerTossCompletesSuccessfully() {
		KitchenExecutionTossDTO request = fixtureLoader.getObject("tossRequest", KitchenExecutionTossDTO.class).orElse(new KitchenExecutionTossDTO());
		Optional<RecipeModel> recipe = fixtureLoader.getObject("recipe", RecipeModel.class);
		when(mockRecipeRepository.findById(1L)).thenReturn(recipe);

		assertDoesNotThrow(() -> service.registerToss(request));
	}

	@Test
	void registerTossThrowsExceptionWhenRecipeNotFound() {
		KitchenExecutionTossDTO request = fixtureLoader.getObject("tossRequest", KitchenExecutionTossDTO.class).orElse(new KitchenExecutionTossDTO());

		assertThrows(ResourceNotFoundException.class, () -> service.registerToss(request));
	}

	private static class InventoryListReference extends TypeReference<List<InventoryModel>> {
	}
}
