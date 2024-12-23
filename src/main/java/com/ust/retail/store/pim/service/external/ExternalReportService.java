package com.ust.retail.store.pim.service.external;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.ust.retail.store.bistro.dto.recipes.RecipeDetailDTO;
import com.ust.retail.store.bistro.service.recipes.RecipeService;
import com.ust.retail.store.pim.common.catalogs.ProductTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterTypeCatalog;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.report.sale.ExternalAdjustmentReportDTO;
import com.ust.retail.store.pim.dto.external.report.sale.ExternalInventoryReportDTO;
import com.ust.retail.store.pim.dto.external.report.sale.ExternalInventoryReportRequest;
import com.ust.retail.store.pim.engine.inventory.InventoryEngine;
import com.ust.retail.store.pim.repository.inventory.InventoryExternalReportRepository;
import com.ust.retail.store.pim.util.DateUtils;

@Service
public class ExternalReportService {
	private final InventoryExternalReportRepository inventoryExternalReportRepository;
	private final RecipeService recipeService;

	public ExternalReportService(InventoryExternalReportRepository inventoryExternalReportRepository,
								 RecipeService recipeService) {
		this.inventoryExternalReportRepository = inventoryExternalReportRepository;
		this.recipeService = recipeService;
	}

	public ExternalApiResponse<List<ExternalInventoryReportDTO>> getGroceryOnHandInventory(ExternalInventoryReportRequest request) {
		return new ExternalApiResponse<>(inventoryExternalReportRepository.getGroceryOnHandInventoryReport(
				request.getStoreNumberId(),
				request.getUpcList(),
				UpcMasterTypeCatalog.PIM_TYPE,
				ProductTypeCatalog.PRODUCT_TYPE_FG));
	}

	public ExternalApiResponse<List<ExternalInventoryReportDTO>> getBistroOnHandInventory(ExternalInventoryReportRequest request) {
		List<ExternalInventoryReportDTO> data = inventoryExternalReportRepository.getBistroOnHandInventoryReport(
				request.getStoreNumberId(),
				request.getUpcList(),
				UpcMasterTypeCatalog.BISTRO_TYPE,
				ProductTypeCatalog.PRODUCT_TYPE_FG);
		data.forEach(dto -> {
			List<RecipeDetailDTO> recipeIngredientsByUpcMasterId = recipeService.getRecipeIngredientsByUpcMasterId(dto.getUpcMasterId());
			dto.updateAverageCost(
					recipeIngredientsByUpcMasterId.stream()
							.map(RecipeDetailDTO::getCost)
							.filter(Objects::nonNull)
							.reduce(0d, Double::sum)
			);
		});
		return new ExternalApiResponse<>(data);
	}

	public ExternalApiResponse<List<ExternalAdjustmentReportDTO>> getShrinkageReportForDate(Date reportDate) {
		return new ExternalApiResponse<>(inventoryExternalReportRepository.getShrinkageReportForDates(
						DateUtils.atStartOfDay(reportDate),
						DateUtils.atEndOfDay(reportDate),
						InventoryEngine.AUTHORIZATION_STATUS_AUTHORIZED,
						InventoryEngine.OPERATION_TYPE_SHRINKAGE
				));
	}

}
