package com.ust.retail.store.bistro.dto.recipes;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcStorePriceModel;
import lombok.Getter;

import java.util.Objects;
import java.util.Optional;

@Getter
public class RecipeStorePriceDTO {
	private Long storePriceId;
	private Long storeNumId;
	private String storeName;
	private Double foodCostPercentage;
	private Double totalCost;
	@JsonIgnore
	private Double indirectCost = 0d;
	private Double calculatedPrice = 0d;
	private Double salePrice;

	public RecipeStorePriceDTO parseToDTO(UpcStorePriceModel storePrice) {
		this.storePriceId = storePrice.getUpcStorePriceId();
		this.storeNumId = storePrice.getStoreNumber().getStoreNumId();
		this.storeName = storePrice.getStoreNumber().getStoreName();
		this.salePrice = storePrice.getSalePrice();

		return this;
	}

	public UpcStorePriceModel createModel(Long userId) {
		return new UpcStorePriceModel(storePriceId, storeNumId, salePrice, userId);
	}

	public RecipeStorePriceDTO setStoreInfo(StoreNumberDTO store) {
		this.storeNumId = store.getStoreNumId();
		this.storeName = store.getStoreName();

		return this;
	}

	public void merge(RecipeStorePriceDTO storePrice) {
		this.storePriceId = storePrice.getStorePriceId();
		this.salePrice = storePrice.getSalePrice();
		updateFoodCostPercentage();
	}

	public RecipeStorePriceDTO setCostInformation(Double totalCost, Double indirectCost, Double bufferPricePercentage) {
		this.indirectCost = indirectCost;
		this.totalCost = Math.round(totalCost * 100) / 100d;

		double compoundCost = this.totalCost + this.indirectCost;
		this.calculatedPrice = compoundCost * (100 + bufferPricePercentage) / 100;
		this.calculatedPrice = Math.round(this.calculatedPrice * 100) / 100d;

		updateFoodCostPercentage();

		return this;
	}

	private void updateFoodCostPercentage() {
		Optional.ofNullable(salePrice)
				.ifPresent(price -> {
					if (price > 0d) {
						double compoundCost = this.totalCost + this.indirectCost;
						this.foodCostPercentage = compoundCost / price * 100;
						this.foodCostPercentage = Math.round(this.foodCostPercentage * 100) / 100d;
					}
				});
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RecipeStorePriceDTO)) return false;
		RecipeStorePriceDTO that = (RecipeStorePriceDTO) o;
		return Objects.equals(storePriceId, that.storePriceId) || Objects.equals(storeNumId, that.storeNumId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(storeNumId);
	}
}
