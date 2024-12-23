package com.ust.retail.store.pim.service.external;

import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.common.catalogs.ProductTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterTypeCatalog;
import com.ust.retail.store.pim.dto.external.product.ExternalProductByStoreAndSkuListRequest;
import com.ust.retail.store.pim.dto.external.product.ExternalProductByStoreRequest;
import com.ust.retail.store.pim.dto.external.product.ExternalProductDTO;
import com.ust.retail.store.pim.dto.upcmaster.ApplicableTaxDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.InventoryModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.inventory.InventoryRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.service.tax.TaxService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExternalProductService {
	private final UpcMasterRepository upcMasterRepository;
	private final TaxService taxService;
	private final InventoryRepository inventoryRepository;
	private final UnitConverter unitConverter;

	public ExternalProductService(UpcMasterRepository upcMasterRepository,
								  TaxService taxService,
								  InventoryRepository inventoryRepository,
								  UnitConverter unitConverter) {
		this.upcMasterRepository = upcMasterRepository;
		this.taxService = taxService;
		this.inventoryRepository = inventoryRepository;
		this.unitConverter = unitConverter;
	}

	public List<ExternalProductDTO> findByFilters(ExternalProductByStoreRequest request) {
		return upcMasterRepository.findByStoreAndFilters(
						request.getStoreId(),
						request.getSearchKey(),
						request.getGroupId(),
						request.getCategoryId(),
						request.getSubcategoryId(),
						request.getChannelId(),
						UpcMasterTypeCatalog.PIM_TYPE,
						ProductTypeCatalog.PRODUCT_TYPE_FG,
						UpcMasterStatusCatalog.UPC_MASTER_STATUS_DISCONTINUE_TRADING,
						request.getVcItem(),
						request.isPageable() ? request.createSimplePageable() : null)
				.map(m -> new ExternalProductDTO()
						.parseToDTO(m, request.getStoreId(), unitConverter)
						.setApplicableTaxes(taxService.findApplicableTaxesForProductAtStore(m.getUpcMasterId(), request.getStoreId()).stream()
								.map(tax -> new ApplicableTaxDTO(
										tax.getTaxTypeName(),
										Stream.of(tax.getProductGroupName(), tax.getProductCategoryName(), tax.getProductSubcategoryName())
												.filter(Objects::nonNull)
												.collect(Collectors.joining("/")),
										tax.getPercentage()))
								.collect(Collectors.toUnmodifiableSet()))
				)
				.getContent();
	}

	public ExternalProductDTO findProductByStore(ExternalProductByStoreRequest request) {
		return upcMasterRepository.findProductByStore(
						request.getStoreId(),
						request.getChannelId(),
						request.getSku(),
						UpcMasterTypeCatalog.PIM_TYPE,
						ProductTypeCatalog.PRODUCT_TYPE_FG,
						UpcMasterStatusCatalog.UPC_MASTER_STATUS_DISCONTINUE_TRADING)
				.map(m -> new ExternalProductDTO()
						.parseToDTO(m, request.getStoreId(), unitConverter)
						.setApplicableTaxes(taxService.findApplicableTaxesForProductAtStore(m.getUpcMasterId(), request.getStoreId()).stream()
								.map(tax -> new ApplicableTaxDTO(
										tax.getTaxTypeName(),
										Stream.of(tax.getProductGroupName(), tax.getProductCategoryName(), tax.getProductSubcategoryName())
												.filter(Objects::nonNull)
												.collect(Collectors.joining("/")),
										tax.getPercentage()))
								.collect(Collectors.toUnmodifiableSet())))
				.orElseThrow(() -> new ResourceNotFoundException("UPC Master", "sku", request.getSku()));
	}

	public List<ExternalProductDTO> findByStoreAndSkuList(ExternalProductByStoreAndSkuListRequest request) {
		List<String> skuList = request.getProductList().stream()
				.map(ExternalProductByStoreAndSkuListRequest.SkuDetail::getSku)
				.collect(Collectors.toList());

		skuList.addAll(request.getSkuList());

		List<ExternalProductDTO> result = inventoryRepository.findByStoreNumIdAndPrincipalUpcIn(
						request.getStoreId(),
						skuList,
						UpcMasterTypeCatalog.PIM_TYPE,
						ProductTypeCatalog.PRODUCT_TYPE_FG,
						UpcMasterStatusCatalog.UPC_MASTER_STATUS_DISCONTINUE_TRADING).stream()
				.collect(Collectors.groupingBy(InventoryModel::getUpcMaster))
				.entrySet().stream()
				.map(entry -> new ExternalProductDTO().parseToDTO(entry.getKey(), request.getStoreId(), unitConverter)
						.setStoreLocationInventory(entry.getValue(), unitConverter))
				.collect(Collectors.toList());
		result.forEach(p -> request.getProductList().stream()
				.filter(p1 -> p1.getSku().equals(p.getSku()))
				.findFirst()
				.ifPresent(p1 -> p.updatePrice(p1.getAmount())));
		return result;
	}

	public void updateImageTrained(Long upcMasterId, boolean value) {
		UpcMasterModel model = upcMasterRepository.findById(upcMasterId)
				.orElseThrow(() -> new ResourceNotFoundException("UPC Master", "id", upcMasterId));

		model.setImageAsTrained(value);

		upcMasterRepository.save(model);
	}
}
