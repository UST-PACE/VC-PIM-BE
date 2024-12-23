package com.ust.retail.store.pim.dto.external;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.model.catalog.ProductGroupModel;
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
public class ExternalProductGroupDTO {
	private Long groupId;
	private String groupName;
	private String picture;
	private Long productCount;

	@Setter
	private List<ExternalProductCategoryDTO> categoryList;

	public ExternalProductGroupDTO parseToDTO(ProductGroupModel m) {
		this.groupId = m.getProductGroupId();
		this.groupName = m.getProductGroupName();
		this.picture = m.getPictureUrl();
		this.productCount = (long) Optional.ofNullable(m.getProducts()).orElse(List.of()).size();
		this.categoryList = Optional.ofNullable(m.getProductCategories()).orElse(List.of()).stream()
				.map(pc -> new ExternalProductCategoryDTO().parseToDTO(pc))
				.sorted(Comparator.comparing(ExternalProductCategoryDTO::getCategoryId))
				.collect(Collectors.toUnmodifiableList());

		return this;
	}
}
