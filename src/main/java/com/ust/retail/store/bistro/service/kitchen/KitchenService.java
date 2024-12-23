package com.ust.retail.store.bistro.service.kitchen;

import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionLineDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionRequestDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionTossDTO;
import com.ust.retail.store.bistro.dto.kitchen.KitchenExecutionWastageLineDTO;
import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.bistro.model.wastage.WastageModel;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.bistro.repository.wastage.WastageRepository;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.StoreFacade;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.engine.inventory.BistroProduceInventory;
import com.ust.retail.store.pim.engine.inventory.BistroTossingInventory;
import com.ust.retail.store.pim.engine.inventory.BistroWasteWholeDishInventory;
import com.ust.retail.store.pim.engine.inventory.InventoryEngine;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.repository.inventory.InventoryHistoryRepository;
import com.ust.retail.store.pim.util.DateUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KitchenService {

	private final WastageRepository wastageRepository;
	private final RecipeRepository recipeRepository;
	private final BistroWasteWholeDishInventory bistroWasteWholeDishInventory;
	private final BistroTossingInventory bistroTossingInventory;
	private final BistroProduceInventory bistroProduceInventory;
	private final InventoryHistoryRepository inventoryHistoryRepository;
	private final EntityManager entityManager;
	private final AuthenticationFacade authenticationFacade;
	private final StoreFacade storeFacade;

	public KitchenService(WastageRepository wastageRepository,
						  RecipeRepository recipeRepository,
						  BistroWasteWholeDishInventory bistroWasteWholeDishInventory,
						  BistroTossingInventory bistroTossingInventory,
						  BistroProduceInventory bistroProduceInventory,
						  InventoryHistoryRepository inventoryHistoryRepository,
						  EntityManager entityManager,
						  AuthenticationFacade authenticationFacade,
						  StoreFacade storeFacade) {
		this.wastageRepository = wastageRepository;
		this.recipeRepository = recipeRepository;
		this.bistroWasteWholeDishInventory = bistroWasteWholeDishInventory;
		this.bistroTossingInventory = bistroTossingInventory;
		this.bistroProduceInventory = bistroProduceInventory;
		this.inventoryHistoryRepository = inventoryHistoryRepository;
		this.entityManager = entityManager;
		this.authenticationFacade = authenticationFacade;
		this.storeFacade = storeFacade;
	}

	public Page<KitchenExecutionLineDTO> loadExecutionFor(KitchenExecutionRequestDTO requestDTO) {
		return recipeRepository.findCurrentByStoreAndRecipeName(
						requestDTO.getStoreNumId(),
						requestDTO.getRecipeName(),
						Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
						DateUtils.getCurrentTimeAsInt(),
						requestDTO.createPageable())
				.map(r -> new KitchenExecutionLineDTO(
						r.getRecipeId(),
						r.getRelatedUpcMaster().getProductName(),
						r.isDisplayExternally()));
	}

	public Page<KitchenExecutionWastageLineDTO> loadWastageFor(KitchenExecutionRequestDTO requestDTO) {
		return wastageRepository.findCurrentByStoreAndRecipeName(
						requestDTO.getStoreNumId(),
						requestDTO.getRecipeName(),
						Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
						DateUtils.getCurrentTimeAsInt(),
						new Date(),
						requestDTO.createPageable())
				.map(model -> new KitchenExecutionWastageLineDTO().parseToDTO(model));
	}

	public Page<KitchenExecutionTossDTO> loadTossingFor(KitchenExecutionRequestDTO requestDTO) {

		Page<RecipeModel> recipes = recipeRepository.findCurrentByStoreAndRecipeName(
				requestDTO.getStoreNumId(),
				requestDTO.getRecipeName(),
				Calendar.getInstance().get(Calendar.DAY_OF_WEEK),
				DateUtils.getCurrentTimeAsInt(),
				requestDTO.createPageable());
		List<Long> upcs = recipes.getContent().stream()
				.map(r -> r.getRelatedUpcMaster().getUpcMasterId())
				.collect(Collectors.toUnmodifiableList());
		Map<Long, Double> tossingCount = inventoryHistoryRepository.getTossingForPeriodAndUpcList(
						DateUtils.atStartOfDay(new Date()),
						DateUtils.atEndOfDay(new Date()),
						upcs,
						BistroTossingInventory.OPERATION_MODULE,
						InventoryEngine.OPERATION_TYPE_TOSSING)
				.stream().collect(Collectors.toMap(t -> t.get(0, Long.class), t -> t.get(1, Double.class)));

		return recipes.map(r -> new KitchenExecutionTossDTO(
				r.getRecipeId(),
				r.getRelatedUpcMaster().getProductName(),
				tossingCount.getOrDefault(r.getRelatedUpcMaster().getUpcMasterId(), 0d).intValue()));
	}

	@Transactional
	public void registerWastage(KitchenExecutionWastageLineDTO wastage) {

		WastageModel model = wastage.createModel(authenticationFacade.getCurrentUserId());
		wastageRepository.saveAndFlush(model);

		if (wastage.isWholeDish()) {
			entityManager.refresh(model);
			bistroWasteWholeDishInventory.execute(
					List.of(new Item(
							1d,
							model.getRecipe().getRelatedUpcMaster().getUpcMasterId(),
							storeFacade.getStoreLocationForSales()
					)),
					List.of(),
					model.getWastageId());
		}
	}

	public void deleteWastage(Long wastageId) {
		WastageModel model = wastageRepository.findById(wastageId)
				.orElseThrow(() -> new ResourceNotFoundException("Wastage", "id", wastageId));
		if (model.isWholeDish()) {
			bistroProduceInventory.execute(
					List.of(new Item(
							1d,
							model.getRecipe().getRelatedUpcMaster().getUpcMasterId(),
							storeFacade.getStoreLocationForSales()
					)),
					List.of(),
					wastageId);
		}
		wastageRepository.deleteById(wastageId);
	}

	@Transactional
	public void registerToss(KitchenExecutionTossDTO tossDTO) {
		RecipeModel recipe = recipeRepository.findById(tossDTO.getRecipeId())
				.orElseThrow(() -> new ResourceNotFoundException("Recipe", "id", tossDTO.getRecipeId()));

		bistroTossingInventory.execute(
				List.of(new Item(
						tossDTO.getPortions().doubleValue(),
						recipe.getRelatedUpcMaster().getUpcMasterId(),
						storeFacade.getStoreLocationForSales())),
				List.of(),
				tossDTO.getRecipeId());
	}
}
