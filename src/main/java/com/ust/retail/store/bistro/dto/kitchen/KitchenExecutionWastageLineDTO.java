package com.ust.retail.store.bistro.dto.kitchen;

import com.ust.retail.store.bistro.model.wastage.WastageModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
public class KitchenExecutionWastageLineDTO {
	private Long wastageId;
	private Long recipeId;
	private String recipeName;
	private boolean wholeDish;
	private Long wastageReasonId;
	private String wastageReason;
	private List<KitchenExecutionWastageDetailDTO> details;

	public WastageModel createModel(Long userId) {
		WastageModel wastageModel = new WastageModel(
				this.wastageId,
				new Date(),
				this.wholeDish,
				this.recipeId,
				this.wastageReasonId,
				userId);

		if (!this.wholeDish) {
			this.details.forEach(detail -> wastageModel.addDetail(detail.getIngredientUpcMasterId(), detail.getWasteAmount(), detail.getWasteUnitId()));
		}

		return wastageModel;
	}

	public KitchenExecutionWastageLineDTO parseToDTO(WastageModel model) {
		this.wastageId = model.getWastageId();
		this.recipeName = model.getRecipe().getRelatedUpcMaster().getProductName();
		this.wholeDish = model.isWholeDish();
		this.wastageReason = model.getWastageReason().getCatalogOptions();
		this.details = new ArrayList<>();

		model.getDetails().forEach(detail -> this.details.add(new KitchenExecutionWastageDetailDTO().parseToDTO(detail)));

		return this;
	}
}
