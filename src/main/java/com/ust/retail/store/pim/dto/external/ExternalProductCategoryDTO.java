package com.ust.retail.store.pim.dto.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class ExternalProductCategoryDTO {
	private Long categoryId;
	private String categoryName;
	private String picture;
	private Long productCount;

	@Setter
	private List<ExternalProductSubcategoryDTO> subcategoryList;

	public ExternalProductCategoryDTO parseToDTO(ProductCategoryModel m) {
		this.categoryId = m.getProductCategoryId();
		this.categoryName = m.getProductCategoryName();
		this.picture = m.getPictureUrl();
		this.productCount = (long) Optional.ofNullable(m.getProducts()).orElse(List.of()).size();
		this.subcategoryList = Optional.ofNullable(m.getProductSubcategories()).orElse(List.of()).stream()
				.map(ps -> new ExternalProductSubcategoryDTO().parseToDTO(ps))
				.sorted(Comparator.comparing(ExternalProductSubcategoryDTO::getSubcategoryId))
				.collect(Collectors.toUnmodifiableList());

		return this;
	}
}
