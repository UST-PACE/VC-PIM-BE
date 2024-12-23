package com.ust.retail.store.pim.dto.report;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class PurchaseOrderReportDTO {
	private Date reportDate;
	private String storeName;
	private String storeManager;

	@Setter
	private Double amountOrdersDrafted;
	@Setter
	private Double amountOrdersOrdered;
	@Setter
	private Double amountOrdersPendingApproval;
	@Setter
	private Double amountOrdersReceived;

	@Setter
	private Integer ordersWTD;
	@Setter
	private Integer ordersMTD;

	@Setter
	private List<PurchaseOrderDraftedDTO> draftedList;
	@Setter
	private List<PurchaseOrderPendingApprovalDTO> pendingApprovalList;
	@Setter
	private List<PurchaseOrderOrderedDTO> orderedList;
	@Setter
	private List<PurchaseOrderReceptionDTO> receptionList;

	public PurchaseOrderReportDTO(Date reportDate, String storeName, String storeManager) {
		this.reportDate = reportDate;
		this.storeName = storeName;
		this.storeManager = storeManager;
	}

}
