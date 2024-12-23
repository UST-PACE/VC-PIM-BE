package com.ust.retail.store.pim.service.purchaseorder;

import com.ust.retail.store.pim.common.catalogs.ItemStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.PoStatusCatalog;
import com.ust.retail.store.pim.dto.purchaseorder.PurchaseOrderDetailDTO;
import com.ust.retail.store.pim.exceptions.InvalidUPCException;
import com.ust.retail.store.pim.exceptions.ItemReceivingException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderDetailModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderDetailRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderDetailService {

	private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
	private final UpcMasterRepository upcMasterRepository;

	@Autowired
	public PurchaseOrderDetailService(PurchaseOrderDetailRepository purchaseOrderDetailRepository,
									  UpcMasterRepository upcMasterRepository) {
		super();
		this.purchaseOrderDetailRepository = purchaseOrderDetailRepository;
		this.upcMasterRepository = upcMasterRepository;
	}

	public PurchaseOrderDetailModel getPendingItemByCode(String code, Long purchaseOrderId, Long vendorMasterId, Boolean isUpdate) {

		UpcMasterModel product = upcMasterRepository.findByPrincipalUpc(code)
				.orElseThrow(() -> new InvalidUPCException(code));

		PurchaseOrderDetailModel poDetail = this.purchaseOrderDetailRepository
				.findByPurchaseOrderPurchaseOrderIdAndUpcMasterPrincipalUpc(purchaseOrderId, product.getPrincipalUpc())
				.orElseThrow(() -> new ResourceNotFoundException("Purchase Order - code was not found on PO", "purchaseOrderDetailId", purchaseOrderId));

		if (poDetail.getItemStatus().getCatalogId() == ItemStatusCatalog.ITEM_STATUS_RECEIVED && !isUpdate) {
			throw new ItemReceivingException(poDetail.getUpcMaster().getPrincipalUpc());
		}

		return poDetail;
	}

	public List<PurchaseOrderDetailDTO> findByUpcMasterIdInModifiableOrders(Long upcMasterId) {
		return purchaseOrderDetailRepository.findByUpcMasterUpcMasterIdAndPurchaseOrderStatusCatalogIdNotIn(
						upcMasterId,
						List.of(PoStatusCatalog.PO_STATUS_IN_RECEPTION,
								PoStatusCatalog.PO_STATUS_INCOMPLETE,
								PoStatusCatalog.PO_STATUS_COMPLETED)).stream()
				.map(m -> new PurchaseOrderDetailDTO().parseToDTO(m))
				.collect(Collectors.toList());
	}
}
