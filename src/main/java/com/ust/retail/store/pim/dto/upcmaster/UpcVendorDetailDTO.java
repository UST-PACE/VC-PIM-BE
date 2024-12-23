package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.catalogs.ProductTypeCatalog;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class UpcVendorDetailDTO {

	@NotNull(message = "upcVendorDetailId is mandatory.", groups = {OnUpdate.class})
	private Long upcVendorDetailId;

	@NotNull(message = "upcMasterId is mandatory.", groups = {OnUpdate.class})
	private Long upcMasterId;
	private String upc;
	private String productName;

	@NotNull(message = "Vendor id is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long vendorMasterId;
	private String vendorCode;
	private String vendorName;

	@NotNull(message = "packageType Id is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long packageTypeId;

	private Double salePrice;

	private Integer unitsPerCase;

	private Integer casesPerPallet;

	private Double moq;

	private String caseUpc;

	private Double caseWeight;

	private Double caseLength;

	private Double caseHeight;

	private Double caseWidth;

	private Double palletWeight;

	private Double palletLength;

	private Double palletHeight;

	private Double palletWidth;

	private Long countryOfOriginId;

	private Boolean defaultVendor;

	private Boolean rawMaterial;

	private Set<UpcVendorStoreCostDTO> storeCosts;


	public UpcVendorDetailsModel createModel(Long userId) {
		return new UpcVendorDetailsModel(
				this.upcVendorDetailId,
				this.unitsPerCase,
				this.caseUpc,
				this.caseWeight,
				this.caseLength,
				this.caseHeight,
				this.caseWidth,
				this.casesPerPallet,
				this.palletWeight,
				this.palletLength,
				this.palletHeight,
				this.palletWidth,
				this.moq,
				this.upcMasterId,
				this.vendorMasterId,
				this.packageTypeId,
				this.countryOfOriginId,
				this.defaultVendor,
				this.storeCosts,
				userId
		);
	}


	public UpcVendorDetailDTO parseToDTO(UpcVendorDetailsModel upcVendorDetailsModel) {
		this.upcVendorDetailId = upcVendorDetailsModel.getUpcVendorDetailId();
		this.salePrice = upcVendorDetailsModel.getUpcMaster().getSalePrice();
		this.unitsPerCase = upcVendorDetailsModel.getUnitsPerCase();
		this.caseUpc = upcVendorDetailsModel.getCaseUpc();
		this.caseWeight = upcVendorDetailsModel.getCaseWeight();
		this.caseLength = upcVendorDetailsModel.getCaseLength();
		this.caseHeight = upcVendorDetailsModel.getCaseHeight();
		this.caseWidth = upcVendorDetailsModel.getCaseWidth();
		this.casesPerPallet = upcVendorDetailsModel.getCasesPerPallet();
		this.palletWeight = upcVendorDetailsModel.getPalletWeight();
		this.palletLength = upcVendorDetailsModel.getPalletLength();
		this.palletHeight = upcVendorDetailsModel.getPalletHeight();
		this.palletWidth = upcVendorDetailsModel.getPalletWidth();
		this.moq = upcVendorDetailsModel.getMoq();
		this.upcMasterId = upcVendorDetailsModel.getUpcMaster().getUpcMasterId();
		this.upc = upcVendorDetailsModel.getUpcMaster().getPrincipalUpc();
		this.productName = upcVendorDetailsModel.getUpcMaster().getProductName();
		this.vendorMasterId = upcVendorDetailsModel.getVendorMaster().getVendorMasterId();
		this.vendorCode = upcVendorDetailsModel.getVendorMaster().getVendorCode();
		this.vendorName = upcVendorDetailsModel.getVendorMaster().getVendorName();
		this.packageTypeId = upcVendorDetailsModel.getPackageType().getCatalogId();
		this.countryOfOriginId = upcVendorDetailsModel.getCountryOfOrigin().getCatalogId();
		this.defaultVendor = upcVendorDetailsModel.getDefaultVendor();
		Optional.ofNullable(upcVendorDetailsModel.getUpcMaster().getProductType())
				.ifPresent(productType -> this.rawMaterial = Objects.equals(productType.getCatalogId(), ProductTypeCatalog.PRODUCT_TYPE_RM));

		this.storeCosts = Optional.ofNullable(upcVendorDetailsModel.getStoreCosts()).orElse(Set.of()).stream()
				.map(vc -> new UpcVendorStoreCostDTO().parseToDTO(vc))
				.sorted(Comparator.comparingLong(UpcVendorStoreCostDTO::getStoreNumId))
				.collect(Collectors.toCollection(LinkedHashSet::new));

		return this;
	}
}

