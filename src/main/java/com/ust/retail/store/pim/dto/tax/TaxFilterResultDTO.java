package com.ust.retail.store.pim.dto.tax;

import com.ust.retail.store.pim.model.tax.TaxModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaxFilterResultDTO {
	private Long taxId;
	private String storeName;
	private String productGroupName;
	private String productCategoryName;
	private String productSubcategoryName;
	private String taxTypeName;
	private Double percentage;
	private Date createdAt;

	public TaxFilterResultDTO parseToDTO(TaxModel m) {
		this.taxId = m.getTaxId();
		this.storeName = m.getStoreNumber().getStoreName();
		this.taxTypeName = m.getTaxType().getCatalogOptions();
		this.percentage = m.getPercentage();
		this.createdAt = m.getCreatedAt();
		this.productGroupName = m.getProductGroup().getProductGroupName();
		Optional.ofNullable(m.getProductCategory()).ifPresent(ps -> this.productCategoryName = ps.getProductCategoryName());
		Optional.ofNullable(m.getProductSubcategory()).ifPresent(ps -> this.productSubcategoryName = ps.getProductSubcategoryName());

		return this;
	}
}
