package com.ust.retail.store.pim.service.purchaseorder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.dto.purchaseorder.operation.FulfillmentCandidateDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;

@Component
public class DefaultAutoFulfillmentHelper extends AutoFulfillmentHelper {
	private final UnitConverter unitConverter;
	private final UpcMasterRepository upcMasterRepository;

	public DefaultAutoFulfillmentHelper(UnitConverter unitConverter, UpcMasterRepository upcMasterRepository) {
		this.unitConverter = unitConverter;
		this.upcMasterRepository = upcMasterRepository;
	}

	@Override
	protected boolean shouldFulfill(FulfillmentCandidateDTO candidate) {
		return candidate.getStockMin() > getQtyInUnits(candidate) + candidate.getTotalAmount();
	}

	@Override
	protected boolean shouldUpdateLineItem(FulfillmentCandidateDTO candidate) {
		return Objects.nonNull(candidate.getPurchaseOrderDetailId()) && shouldFulfill(candidate);
	}

	@Override
	protected boolean shouldRemoveLineItem(FulfillmentCandidateDTO candidate) {
		return Objects.nonNull(candidate.getPurchaseOrderDetailId())
				&& getQtyInUnits(candidate) >= candidate.getStockMin();
	}

	@Override
	protected int calculateAmountToRequest(FulfillmentCandidateDTO candidate) {
		return candidate.getTotalAmount() + BigDecimal.valueOf(getAmountToCoverFulfillment(candidate))
				.setScale(0, RoundingMode.CEILING)
				.intValueExact();
	}

	@Override
	protected double getAmountToCoverFulfillment(FulfillmentCandidateDTO candidate) {
		double amount = getQtyInUnits(candidate) + candidate.getTotalAmount() - candidate.getStockMin();
		return amount < 0? Math.abs(amount) : 0.0;
	}

	private Double getQtyInUnits(FulfillmentCandidateDTO candidate) {
		UpcMasterModel upcMaster = upcMasterRepository.findById(candidate.getUpcMasterId()).orElseThrow();
		double qtyInUnits = candidate.getQty() / getConvertedContentPerUnit(upcMaster);
		candidate.updateQtyInUnits(qtyInUnits);
		return qtyInUnits;
	}

	private Double getConvertedContentPerUnit(UpcMasterModel upcMasterModel) {
		Double contentPerUnit = upcMasterModel.getContentPerUnit();
		Long contentPerUnitUomId = upcMasterModel.getContentPerUnitUom().getCatalogId();
		Long inventoryUnitId = upcMasterModel.getInventoryUnit().getCatalogId();
		return unitConverter.convert(contentPerUnitUomId, inventoryUnitId, contentPerUnit);
	}

}
