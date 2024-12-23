package com.ust.retail.store.pim.dto.report;

import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

public interface IUpcCsvDumpDTO {
	String getUpc();
	String getSku();
	String getProductName();
	String getProductItem();
	String getProductGroup();
	String getProductCategory();
	String getProductSubcategory();
	String getBrand();
	String getStatus();
	String getProductDescription();
	String getPictureUrl();
	String getStoreSection();
	Double getHeight();
	Double getWidth();
	Double getDepth();
	Double getContentPerUnit();
	String getContentPerUnitUom();
	String getCountryOfOrigin();
	String getInventoryUnit();
	Double getSalePrice();
	Double getTaxPercentage();
	Integer getShelfLifeWh();
	Integer getShelfLifeShipment();
	Integer getShelfLifeCustomer();
	String getPlanogramLocation();
	Double getStockMin();
	boolean getExpirationDateRequired();
	boolean getAgeRestricted();
	boolean getImageTrained();
	String getVendorName();
	String getVendorEmail();
	Double getSupplierPriceCase();
	Double getSupplierPriceUnit();
	Double getVendorMoq();
	Integer getUnitsPerCase();
	String getCaseUpc();
	Double getCaseWeight();
	Double getCaseLength();
	Double getCaseHeight();
	Double getCaseWidth();
	Integer getCasesPerPallet();
	Double getPalletWeight();
	Double getPalletLength();
	Double getPalletHeight();
	Double getPalletWidth();
	Double getInventoryOnHand();
	UpcMasterModel getUpcMaster();
	Long getStoreNumId();

}
