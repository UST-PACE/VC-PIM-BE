package com.ust.retail.store.pim.dto.productreturn;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductReturnDetailDTO {
	private Long returnDetailId;
	private String vendorName;
	private String productName;
	private String batchNum;
	private String storeName;
	private String storeLocation;
	private Double qty;
	private String returnReason;
	private Double credit;
	private Long statusId;
	private String status;
	private Long upcMasterId;
	private Long inventoryHistoryId;
}
