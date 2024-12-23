package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcStorePriceModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorStoreCostModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class StorePriceDTO {
	private Long storePriceId;
	private Long storeNumId;
	private String storeName;
	private Double salePrice;
	private String vendorNameLowestCost;
	private Double lowestCost;
	private String vendorNameHighestCost;
	private Double highestCost;

	public StorePriceDTO parseToDTO(UpcStorePriceModel storePrice) {
		this.storePriceId = storePrice.getUpcStorePriceId();
		this.storeNumId = storePrice.getStoreNumber().getStoreNumId();
		this.storeName = storePrice.getStoreNumber().getStoreName();
		this.salePrice = storePrice.getSalePrice();

		return this;
	}

	public StorePriceDTO setLowestStoreCost(UpcVendorStoreCostModel lowest) {
		Optional.ofNullable(lowest).ifPresent(l -> {
			this.vendorNameLowestCost = l.getUpcVendorDetail().getVendorMaster().getVendorName();
			this.lowestCost = l.getCost();
		});
		return this;
	}

	public StorePriceDTO setHighestStoreCost(UpcVendorStoreCostModel highest) {
		Optional.ofNullable(highest).ifPresent(h -> {
			this.vendorNameHighestCost = h.getUpcVendorDetail().getVendorMaster().getVendorName();
			this.highestCost = h.getCost();
		});

		return this;
	}

	public UpcStorePriceModel createModel(Long userId) {
		return new UpcStorePriceModel(storePriceId, storeNumId, salePrice, userId);
	}

	public StorePriceDTO setStoreInfo(StoreNumberDTO store) {
		this.storeNumId = store.getStoreNumId();
		this.storeName = store.getStoreName();

		return this;
	}

	public StorePriceDTO merge(StorePriceDTO storePrice) {
		this.storePriceId = storePrice.getStorePriceId();
		this.salePrice = storePrice.getSalePrice();
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof StorePriceDTO)) return false;
		StorePriceDTO that = (StorePriceDTO) o;
		return Objects.equals(storePriceId, that.storePriceId) || Objects.equals(storeNumId, that.storeNumId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(storeNumId);
	}
}
