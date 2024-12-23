package com.ust.retail.store.pim.dto.external;

import com.ust.retail.store.pim.model.catalog.ProductSubcategoryModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
public class ExternalProductSubcategoryDTO {
	private Long subcategoryId;
	private String subcategoryName;
	private String picture;
	private Long productCount;

	public ExternalProductSubcategoryDTO parseToDTO(ProductSubcategoryModel m) {
		this.subcategoryId = m.getProductSubcategoryId();
		this.subcategoryName = m.getProductSubcategoryName();
		this.picture = m.getPictureUrl();
		this.productCount = (long) Optional.ofNullable(m.getProducts()).orElse(List.of()).size();

		return this;
	}
}
