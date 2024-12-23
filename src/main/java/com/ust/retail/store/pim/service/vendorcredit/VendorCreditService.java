package com.ust.retail.store.pim.service.vendorcredit;

import com.ust.retail.store.pim.common.catalogs.InventoryProductReturnStatusCatalog;
import com.ust.retail.store.pim.dto.productreturn.*;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationDTO;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationLineDTO;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationResultDTO;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationResultLineDTO;
import com.ust.retail.store.pim.engine.inventory.InventoryEngine;
import com.ust.retail.store.pim.engine.inventory.VendorCredits;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcNotSuppliedByVendorException;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnDetailModel;
import com.ust.retail.store.pim.model.inventory.InventoryProductReturnModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import com.ust.retail.store.pim.model.vendorcredits.VendorCreditModel;
import com.ust.retail.store.pim.repository.inventory.InventoryProductReturnDetailRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryProductReturnRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcVendorDetailsRepository;
import com.ust.retail.store.pim.repository.vendorcredits.VendorCreditRepository;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class VendorCreditService {
	private final InventoryProductReturnRepository inventoryProductReturnRepository;
	private final InventoryProductReturnDetailRepository inventoryProductReturnDetailRepository;
	private final VendorCreditRepository vendorCreditRepository;
	private final InventoryService inventoryService;
	private final UpcVendorDetailsRepository upcVendorDetailsRepository;

	public VendorCreditService(InventoryProductReturnRepository inventoryProductReturnRepository,
							   InventoryProductReturnDetailRepository inventoryProductReturnDetailRepository,
							   VendorCreditRepository vendorCreditRepository, InventoryService inventoryService,
							   UpcVendorDetailsRepository upcVendorDetailsRepository) {
		this.inventoryProductReturnRepository = inventoryProductReturnRepository;
		this.inventoryProductReturnDetailRepository = inventoryProductReturnDetailRepository;
		this.vendorCreditRepository = vendorCreditRepository;
		this.inventoryService = inventoryService;
		this.upcVendorDetailsRepository = upcVendorDetailsRepository;
	}

	public ProductReturnDTO findById(Long returnId) {

		InventoryProductReturnModel returnModel = inventoryProductReturnRepository.findById(returnId)
				.orElseThrow(() -> new ResourceNotFoundException("Vendor Credit", "returnId", returnId));

		ProductReturnDTO dto = new ProductReturnDTO().parseToDTO(returnModel);

		dto.setDetails(getCreditDetailWithStatus(returnId));

		return dto;
	}

	public Page<ProductReturnFilterResultDTO> getVendorCreditsByFilters(ProductReturnFilterDTO dto) {
		return inventoryProductReturnRepository
				.findByFilters(dto.getVendorName(), dto.getVendorCode(), dto.getBrandOwnerId(), dto.getProductTypeId(),
						dto.getProductGroupId(), dto.getProductCategoryId(), dto.getProductSubcategoryId(),
						dto.getProductItemId(), dto.getProductName(), dto.getStatusId(), dto.createPageable())
				.map(m -> new ProductReturnFilterResultDTO().parseToDTO(m));
	}

	@Transactional
	public ProductReturnAuthorizationResultDTO authorize(ProductReturnAuthorizationDTO dto) {
		ProductReturnAuthorizationResultDTO result = new ProductReturnAuthorizationResultDTO();

		for (ProductReturnAuthorizationLineDTO line : dto.getLines()) {
			result.getResults().add(processAuthorizeLine(line));
		}

		result.setStatusId(updateProductReturnStatus(dto.getReturnId()));
		return result;
	}

	@Transactional
	public ProductReturnAuthorizationResultDTO decline(ProductReturnAuthorizationDTO dto) {
		ProductReturnAuthorizationResultDTO result = new ProductReturnAuthorizationResultDTO();

		for (ProductReturnAuthorizationLineDTO line : dto.getLines()) {
			ProductReturnAuthorizationResultLineDTO resultLine = processRejectLine(line);
			result.getResults().add(resultLine);
		}

		result.setStatusId(updateProductReturnStatus(dto.getReturnId()));
		return result;
	}

	private long updateProductReturnStatus(Long returnId) {
		long productReturnStatusId = getProductReturnStatusId(returnId);
		inventoryProductReturnRepository.findById(returnId).ifPresent(productReturn -> {
			productReturn.setStatus(productReturnStatusId);
			inventoryProductReturnRepository.save(productReturn);
		});
		return productReturnStatusId;
	}

	private long getProductReturnStatusId(Long returnId) {
		return getCreditDetailWithStatus(returnId).stream()
				.anyMatch(d -> Objects.equals(d.getStatusId(), InventoryEngine.AUTHORIZATION_STATUS_PENDING))
				? InventoryProductReturnStatusCatalog.INVENTORY_PRODUCT_RETURN_STATUS_PARTIAL_REVIEW
				: InventoryProductReturnStatusCatalog.INVENTORY_PRODUCT_RETURN_STATUS_REVIEWED;
	}

	private List<ProductReturnDetailDTO> getCreditDetailWithStatus(Long returnId) {
		return inventoryProductReturnRepository.findCreditDetailWithStatus(returnId, VendorCredits.OPERATION_MODULE,
				InventoryEngine.OPERATION_TYPE_VENDOR_CREDIT);
	}

	private ProductReturnAuthorizationResultLineDTO processAuthorizeLine(ProductReturnAuthorizationLineDTO line) {

		Optional<InventoryProductReturnDetailModel> returnDetailOptional = inventoryProductReturnDetailRepository
				.findById(line.getReturnDetailId());

		ProductReturnAuthorizationResultLineDTO resultLine;

		if (returnDetailOptional.isEmpty()) {
			resultLine = new ProductReturnAuthorizationResultLineDTO(line.getInventoryHistoryId(),
					line.getReturnDetailId(), false, "Return detail ID not found");
		} else {
			boolean successful = inventoryService.authorizeByInventoryHistoryId(line.getInventoryHistoryId());

			if (successful) {
				applyCreditToVendor(line);
			}
			resultLine = new ProductReturnAuthorizationResultLineDTO(line.getInventoryHistoryId(),
					line.getReturnDetailId(), successful, null);
		}
		return resultLine;
	}

	private ProductReturnAuthorizationResultLineDTO processRejectLine(ProductReturnAuthorizationLineDTO line) {
		Optional<InventoryProductReturnDetailModel> returnDetailOptional = inventoryProductReturnDetailRepository
				.findById(line.getReturnDetailId());

		ProductReturnAuthorizationResultLineDTO resultLine;

		if (returnDetailOptional.isEmpty()) {
			resultLine = new ProductReturnAuthorizationResultLineDTO(line.getInventoryHistoryId(),
					line.getReturnDetailId(), false, "Return detail ID not found");
		} else {
			resultLine = new ProductReturnAuthorizationResultLineDTO(line.getInventoryHistoryId(),
					line.getReturnDetailId(),
					inventoryService.rejectByInventoryHistoryId(line.getInventoryHistoryId()), null);
		}
		return resultLine;
	}

	private void applyCreditToVendor(ProductReturnAuthorizationLineDTO line) {


		ReturnDetailInfoDTO returnDetailInfoDTO = calculateCredit(line.getCreditInfo());

		returnDetailInfoDTO.getReturnDetail().setCredit(returnDetailInfoDTO.getCredit(), line.getVendorMasterId());
		inventoryProductReturnDetailRepository.save(returnDetailInfoDTO.getReturnDetail());

		VendorCreditModel vendorCredit = vendorCreditRepository.findByVendorMasterVendorMasterId(line.getVendorMasterId());
		vendorCredit.updateAvailableCredit(returnDetailInfoDTO.getCredit());
		vendorCreditRepository.saveAndFlush(vendorCredit);
	}

	public CalculateCreditsDTO calculateCredits(@Valid CalculateCreditsDTO dto) {

		dto.getCreditsInfo().forEach(currentInfo -> currentInfo.setCredit(calculateCredit(currentInfo).getCredit()));

		return dto;
	}

	private ReturnDetailInfoDTO calculateCredit(CreditInfoDTO currentInfo) {
		UpcVendorDetailsModel vendorDetail = upcVendorDetailsRepository
				.findByUpcMasterUpcMasterIdAndVendorMasterVendorMasterId(currentInfo.getUpcMasterId(),
						currentInfo.getVendorMasterId())
				.orElseThrow(() -> new UpcNotSuppliedByVendorException(currentInfo.getUpcMasterId(),
						currentInfo.getVendorMasterId()));

		InventoryProductReturnDetailModel returnedItem = inventoryProductReturnDetailRepository
				.findById(currentInfo.getReturnDetailId())
				.orElseThrow(() -> new UpcNotSuppliedByVendorException(currentInfo.getUpcMasterId(),
						currentInfo.getVendorMasterId()));

		Long storeNumId = returnedItem.getStoreLocation().getStoreNumber().getStoreNumId();
		Double totalCreditPerProduct = vendorDetail.getProductCost(storeNumId) * returnedItem.getQty();

		return new ReturnDetailInfoDTO(returnedItem, totalCreditPerProduct);
	}
}
