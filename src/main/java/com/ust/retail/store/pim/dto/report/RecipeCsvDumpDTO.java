package com.ust.retail.store.pim.dto.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class RecipeCsvDumpDTO {
	@JsonIgnore
	private UpcMasterModel product;
	private String upc;
	private String sku;
	private String productName;
	private String productGroup;
	private String productCategory;
	private String productSubcategory;
	private String brand;
	private String status;
	private String productDescription;
	private Double contentPerUnit;
	private String contentPerUnitUom;
	private Double salePrice;
	private Double taxPercentage;
	private boolean ageRestricted;
	private boolean imageTrained;
	private String temperature;
	private List<DumpAdditionalFee> additionalFees = new ArrayList<>();

	public RecipeCsvDumpDTO(UpcMasterModel product,
							String upc,
							String sku,
							String productName,
							String productGroup,
							String productCategory,
							String productSubcategory,
							String brand,
							String status,
							String productDescription,
							Double contentPerUnit,
							String contentPerUnitUom,
							Double salePrice,
							Double taxPercentage,
							boolean ageRestricted,
							String temperature) {
		this.product = product;
		this.upc = upc;
		this.sku = sku;
		this.productName = productName;
		this.productGroup = productGroup;
		this.productCategory = productCategory;
		this.productSubcategory = productSubcategory;
		this.brand = brand;
		this.status = status;
		this.productDescription = productDescription;
		this.contentPerUnit = contentPerUnit;
		this.contentPerUnitUom = contentPerUnitUom;
		this.salePrice = salePrice;
		this.taxPercentage = taxPercentage;
		this.ageRestricted = ageRestricted;
		this.imageTrained = product.isImageTrained();
		this.temperature = temperature;

		this.additionalFees = product.getUpcAdditionalFees().stream()
				.sorted(Comparator.comparing(af -> af.getAdditionalFee().getFeeName()))
				.map(af -> new DumpAdditionalFee(
						af.getStoreNumber().getStoreName(),
						af.getAdditionalFee().getFeeName(),
						af.getPrice()))
				.collect(Collectors.toList());
	}

	@Getter
	@AllArgsConstructor
	public static class DumpAdditionalFee {
		private String storeName;
		private String feeName;
		private double price;
	}
}
