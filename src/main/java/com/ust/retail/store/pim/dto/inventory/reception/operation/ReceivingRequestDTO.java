package com.ust.retail.store.pim.dto.inventory.reception.operation;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnReceive;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.model.inventory.PoReceiveDetailModel;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceivingRequestDTO {

	@NotNull(message = "Purchase OrderDetail Id is Mandatory.", groups = { OnReceive.class})
	private Long purchaseOrderDetailId;
	
	@Valid
	@NotNull(message = "Item details are Mandatory.", groups = { OnReceive.class})
	private Item item;
	
	private List<ReceptionWarningDTO> warnings;
	
	public PoReceiveDetailModel createModel(Long userReceiverId) {
		
		PoReceiveDetailModel receiveDetail = new PoReceiveDetailModel(purchaseOrderDetailId, item.getStoreLocationId(),item.getQty(),item.getBatchNumber(),this.hasWarnings(),item.getExpirationDate(), userReceiverId);
		
		if(warnings!=null && !warnings.isEmpty()) {
			for(ReceptionWarningDTO currentWarning: warnings) {
				receiveDetail.add(currentWarning);
			}
		}
		
		return receiveDetail;
	}
	
	private boolean hasWarnings() {
		return (warnings!=null && !warnings.isEmpty()) ? true : false;
	}
	
}
