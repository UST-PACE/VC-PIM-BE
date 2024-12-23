package com.ust.retail.store.pim.dto.inventory.reception.operation;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceivingResumeResponseDetailDTO {

	private Long poReceiveDetailId;
	private Long storeLocationId;
	private String productName;
	private String principalUPC;
	private String batchNumber;
	private String receiver;
	private Integer requestedQty;
	private Double receivedQty;
	private boolean hasError;

	private List<ReceptionWarningDTO> receivingWarnings;

	public ReceivingResumeResponseDetailDTO(Long poReceiveDetailId,
											Long storeLocationId,
											String productName,
											String principalUPC,
											String batchNumber,
											String receiver,
											Integer requestedQty,
											Double receivedQty,
											boolean hasError) {
		super();

		this.poReceiveDetailId = poReceiveDetailId;
		this.storeLocationId = storeLocationId;
		this.productName = productName;
		this.principalUPC = principalUPC;
		this.batchNumber = batchNumber;
		this.receiver = receiver;
		this.requestedQty = requestedQty;
		this.receivedQty = receivedQty;
		this.hasError = hasError;
	}
	
	public void addWarnings(List<ReceptionWarningDTO> warnings) {
		
		if(this.receivingWarnings == null) this.receivingWarnings = new ArrayList<>();
		
		this.receivingWarnings.addAll(warnings);
		
	}
	
}
