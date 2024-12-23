package com.ust.retail.store.pim.dto.purchaseorder.operation;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.util.deserializers.AmericanFormatDateDeserializer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PurchaseOrderFilterRequestDTO extends BaseFilterDTO {
	private Long vendorMasterId;
	private String vendorMasterCode;
	private String vendorMasterName;
	private Long storeNumId;
	private String purchaseOrderNum;
	private String principalUpc;
	private String productName;
	private Long statusId;
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date startSendAt;
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date endSendAt;
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date startCreateAt;
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date endCreateAt;
}
