package com.ust.retail.store.pim.dto.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class UpcCsvDumpDTO {
	private String upc;
	private String sku;
	private String productName;
	private String productItem;
	private String productGroup;
	private String productCategory;
	private String productSubcategory;
	private String brand;
	private String status;
	private String productDescription;
	private boolean hasImage;
	private String storeSection;
	private Double height;
	private Double width;
	private Double depth;
	private Double contentPerUnit;
	private String contentPerUnitUom;
	private String countryOfOrigin;
	private String inventoryUnit;
	private Double salePrice;
	private Double taxPercentage;
	private Integer shelfLifeWh;
	private Integer shelfLifeShipment;
	private Integer shelfLifeCustomer;
	private String planogramLocation;
	private Double stockMin;
	private boolean expirationDateRequired;
	private boolean ageRestricted;
	private boolean imageTrained;
	private String vendorName;
	private String vendorEmail;
	private Double supplierPriceCase;
	private Double supplierPriceUnit;
	private Double vendorMoq;
	private Integer unitsPerCase;
	private String caseUpc;
	private Double caseWeight;
	private Double caseLength;
	private Double caseHeight;
	private Double caseWidth;
	private Integer casesPerPallet;
	private Double palletWeight;
	private Double palletLength;
	private Double palletHeight;
	private Double palletWidth;
	private Double inventoryOnHand;
	private List<DumpAdditionalFee> additionalFees;

	public UpcCsvDumpDTO(IUpcCsvDumpDTO dto) {
		this.upc = dto.getUpc();
		this.sku = dto.getSku();
		this.productName = dto.getProductName();
		this.productItem = dto.getProductItem();
		this.productGroup = dto.getProductGroup();
		this.productCategory = dto.getProductCategory();
		this.productSubcategory = dto.getProductSubcategory();
		this.brand = dto.getBrand();
		this.status = dto.getStatus();
		this.productDescription = dto.getProductDescription();
		this.hasImage = dto.getPictureUrl() != null;
		this.storeSection = dto.getStoreSection();
		this.height = dto.getHeight();
		this.width = dto.getWidth();
		this.depth = dto.getDepth();
		this.contentPerUnit = dto.getContentPerUnit();
		this.contentPerUnitUom = dto.getContentPerUnitUom();
		this.countryOfOrigin = dto.getCountryOfOrigin();
		this.inventoryUnit = dto.getInventoryUnit();
		this.salePrice = dto.getSalePrice();
		this.taxPercentage = dto.getTaxPercentage();
		this.shelfLifeWh = dto.getShelfLifeWh();
		this.shelfLifeShipment = dto.getShelfLifeShipment();
		this.shelfLifeCustomer = dto.getShelfLifeCustomer();
		this.planogramLocation = dto.getPlanogramLocation();
		this.stockMin = dto.getStockMin();
		this.expirationDateRequired = dto.getExpirationDateRequired();
		this.ageRestricted = dto.getAgeRestricted();
		this.imageTrained = dto.getImageTrained();
		this.vendorName = dto.getVendorName();
		this.vendorEmail = dto.getVendorEmail();
		this.supplierPriceCase = dto.getSupplierPriceCase();
		this.supplierPriceUnit = dto.getSupplierPriceUnit();
		this.vendorMoq = dto.getVendorMoq();
		this.unitsPerCase = dto.getUnitsPerCase();
		this.caseUpc = dto.getCaseUpc();
		this.caseWeight = dto.getCaseWeight();
		this.caseLength = dto.getCaseLength();
		this.caseWidth = dto.getCaseWidth();
		this.caseHeight = dto.getCaseHeight();
		this.casesPerPallet = dto.getCasesPerPallet();
		this.palletWeight = dto.getPalletWeight();
		this.palletLength = dto.getPalletLength();
		this.palletHeight = dto.getPalletHeight();
		this.palletWidth = dto.getPalletWidth();
		this.inventoryOnHand = dto.getInventoryOnHand();

		Long storeNumId = dto.getStoreNumId();
		this.additionalFees = dto.getUpcMaster().getUpcAdditionalFees().stream()
				.filter(af -> Objects.equals(af.getStoreNumber().getStoreNumId(), storeNumId))
				.sorted(Comparator.comparing(af -> af.getAdditionalFee().getFeeName()))
				.map(af -> new DumpAdditionalFee(af.getAdditionalFee().getFeeName(), af.getPrice()))
				.collect(Collectors.toList());
	}

	@Getter
	@AllArgsConstructor
	public static class DumpAdditionalFee {
		private String feeName;
		private double price;
	}
}
