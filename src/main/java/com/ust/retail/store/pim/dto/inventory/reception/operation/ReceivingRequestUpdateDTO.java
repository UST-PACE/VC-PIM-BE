package com.ust.retail.store.pim.dto.inventory.reception.operation;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnReceive;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.model.inventory.PoReceiveDetailModel;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceivingRequestUpdateDTO {

	@NotNull(message = "Purchase OrderDetail Id is Mandatory.", groups = { OnReceive.class,OnUpdate.class})
	private Long purchaseOrderDetailId;
	
	@NotNull(message = "Purchase Order Receive Details Id is Mandatory.", groups = { OnReceive.class,OnUpdate.class})
	private Long poReceiveDetailId;
	
	@NotNull(message = "Previews qty Id is Mandatory.", groups = { OnUpdate.class})
	private Double prevQty;
	
	@NotNull(message = "Previews storelocation Id is Mandatory.", groups = { OnUpdate.class})
	private Long prevStoreLocation;
	
	@Valid
	@NotNull(message = "Item details are Mandatory.", groups = { OnReceive.class,OnUpdate.class})
	private Item item;
	
	private List<ReceptionWarningDTO> warnings;
	
	public PoReceiveDetailModel createModel(Long userReceiverId) {
		
		PoReceiveDetailModel receiveDetail = new PoReceiveDetailModel(poReceiveDetailId,purchaseOrderDetailId, item.getStoreLocationId(),item.getQty(),item.getBatchNumber(),hasWarnings(),item.getExpirationDate(), userReceiverId);
		
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
