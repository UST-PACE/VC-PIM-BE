package com.ust.retail.store.pim.dto.inventory;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ust.retail.store.pim.common.annotations.OnInventoryAdjustment;
import com.ust.retail.store.pim.common.annotations.OnReceive;
import com.ust.retail.store.pim.common.annotations.OnVendorCredits;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.util.deserializers.AmericanFormatDateDeserializer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {

	private Long inventoryId;

	@NotNull(message = "Product Qty is mandatory.", groups = { OnReceive.class,OnVendorCredits.class,OnInventoryAdjustment.class })
	private Double qty;

	@NotNull(message = "upcMasterId is mandatory.", groups = { OnReceive.class,OnVendorCredits.class,OnInventoryAdjustment.class})
	private Long upcMasterId;
	
	@NotNull(message = "Store Location ID is mandatory.", groups = { OnReceive.class,OnVendorCredits.class,OnInventoryAdjustment.class })
	private Long storeLocationId;
	
	@NotNull(message = "shrinkage is mandatory.", groups = { OnInventoryAdjustment.class })
	private boolean shrinkage;
	
	private String batchNumber;
	
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date expirationDate;

	public Item(Double qty, Long upcMasterId, Long storeLocationId) {
		this.qty = qty;
		this.upcMasterId = upcMasterId;
		this.storeLocationId = storeLocationId;
	}

	public Item(Double qty, Long upcMasterId, Long storeLocationId, boolean shrinkage) {
		this.qty = qty;
		this.upcMasterId = upcMasterId;
		this.storeLocationId = storeLocationId;
		this.shrinkage = shrinkage;
	}

	public InventoryModel createModel(Long currentUserId) {
		
		return new InventoryModel(inventoryId,0.0,upcMasterId,storeLocationId,currentUserId);

	}

	public Item parseToDTO(InventoryModel inventoryModel) {
		this.inventoryId = inventoryModel.getInventoryId();
		this.qty = inventoryModel.getQty();
		this.upcMasterId = inventoryModel.getUpcMaster().getUpcMasterId();
		this.storeLocationId = inventoryModel.getStoreLocation().getStoreLocationId();
		
		return this;
	}
	
	public Item changeItemQty(Double prevQty) {
		this.qty = prevQty;
		return this;
	}

	public void changeStoreLocation(Long storeLocationId) {
		this.storeLocationId = storeLocationId;
		
	}
}
