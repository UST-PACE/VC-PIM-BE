package com.ust.retail.store.pim.dto.inventory.reception.operation;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceivingResumeResponseDTO {
	private String salesRepName;
	private String salesRepEmail;
	private String escalationContactName;
	private String escalationContactEmail;
	private Long storeNumId;
	private String vendorName;
	private String receiverName;
	private Date receptionDate;
	private String purchaseOrderNumber;

	@Setter
	private List<ReceivingResumeResponseDetailDTO> details;

	public ReceivingResumeResponseDTO(String salesRepName,
									  String salesRepEmail,
									  String escalationContactName,
									  String escalationContactEmail,
									  Long storeNumId,
									  String vendorName,
									  String receiverName,
									  Date receptionDate,
									  String purchaseOrderNumber) {
		this.salesRepName = salesRepName;
		this.salesRepEmail = salesRepEmail;
		this.escalationContactName = escalationContactName;
		this.escalationContactEmail = escalationContactEmail;
		this.storeNumId = storeNumId;
		this.vendorName = vendorName;
		this.receiverName = receiverName;
		this.receptionDate = receptionDate;
		this.purchaseOrderNumber = purchaseOrderNumber;
	}
}
