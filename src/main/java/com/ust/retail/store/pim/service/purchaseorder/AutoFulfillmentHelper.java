package com.ust.retail.store.pim.service.purchaseorder;

import java.util.Map;
import java.util.Objects;

import com.ust.retail.store.pim.dto.purchaseorder.operation.FulfillmentCandidateDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderAddProductRequestDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderFulfillmentRequestDTO;
import com.ust.retail.store.pim.dto.purchaseorder.operation.PurchaseOrderModifyLineItemRequestDTO;

public abstract class AutoFulfillmentHelper {

	PurchaseOrderFulfillmentRequestDTO evaluateCandidate(FulfillmentCandidateDTO candidate, Map<String, Long> createdOrders) {
		Long purchaseOrderId = createdOrders.getOrDefault(String.format("%d_%d", candidate.getVendorMasterId(), candidate.getStoreNumId()), candidate.getPurchaseOrderId());
		return new PurchaseOrderFulfillmentRequestDTO(
				candidate.getVendorMasterId(),
				candidate.getStoreNumId(),
				candidate.getUpcMasterId(),
				shouldFulfill(candidate) || Objects.nonNull(candidate.getPurchaseOrderDetailId())? purchaseOrderId : null,
				candidate.getPurchaseOrderDetailId(),
				Objects.isNull(purchaseOrderId) && shouldFulfill(candidate),
				shouldUpdateLineItem(candidate),
				shouldFulfill(candidate),
				shouldRemoveLineItem(candidate),
				calculateAmountToRequest(candidate)
		);
	}

	PurchaseOrderAddProductRequestDTO getNewOrderLineItem(PurchaseOrderFulfillmentRequestDTO request) {
		return new PurchaseOrderAddProductRequestDTO(
				null,
				request.getVendorMasterId(),
				request.getStoreNumId(),
				request.getUpcMasterId(),
				0,
				0,
				request.getAmountToRequest(),
				true
		);
	}

	PurchaseOrderModifyLineItemRequestDTO getUpdateDetailLineItem(PurchaseOrderFulfillmentRequestDTO request) {
		return new PurchaseOrderModifyLineItemRequestDTO(
				request.getPurchaseOrderDetailId(),
				0,
				0,
				request.getAmountToRequest());
	}

	PurchaseOrderAddProductRequestDTO getAppendDetailLineItem(PurchaseOrderFulfillmentRequestDTO request) {
		return new PurchaseOrderAddProductRequestDTO(
				request.getPurchaseOrderId(),
				request.getVendorMasterId(),
				request.getStoreNumId(),
				request.getUpcMasterId(),
				0,
				0,
				request.getAmountToRequest(),
				true
		);
	}

	protected abstract boolean shouldFulfill(FulfillmentCandidateDTO candidate);

	protected abstract boolean shouldUpdateLineItem(FulfillmentCandidateDTO candidate);

	protected abstract boolean shouldRemoveLineItem(FulfillmentCandidateDTO candidate);

	protected abstract int calculateAmountToRequest(FulfillmentCandidateDTO candidate);

	protected abstract double getAmountToCoverFulfillment(FulfillmentCandidateDTO candidate);
}
