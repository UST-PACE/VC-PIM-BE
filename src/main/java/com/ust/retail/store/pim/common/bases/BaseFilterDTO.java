package com.ust.retail.store.pim.common.bases;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnSimpleFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class BaseFilterDTO {

	@NotNull(message = "The page number parameter is mandatory.", groups = { OnFilter.class, OnSimpleFilter.class })
	private Integer page;

	@NotNull(message = "The elements number parameter is mandatory.", groups = { OnFilter.class, OnSimpleFilter.class })
	private Integer size;

	@NotNull(message = "The column name to sort parameter is mandatory.", groups = { OnFilter.class })
	private String orderColumn;

	@NotNull(message = "The order dir parameter is mandatory.", groups = { OnFilter.class })
	private String orderDir;

	public BaseFilterDTO(Integer page, Integer size) {
		this.page = page;
		this.size = size;
	}

	public Pageable createPageable() {
		return PageRequest.of(getPage(), getSize(), JpaSort.unsafe(Sort.Direction.fromString(getOrderDir()), getOrderColumn()));
	}

	public Pageable createSimplePageable() {
		return PageRequest.of(getPage(), getSize());
	}

}
