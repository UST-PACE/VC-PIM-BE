package com.ust.retail.store.pim.dto.catalog;

import com.ust.retail.store.pim.model.catalog.CatalogModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CatalogDTO {
	private Long catalogId;
	private String catalogName;
	private String catalogOptions;

	public CatalogDTO parseToDTO(CatalogModel model) {
		this.catalogId = model.getCatalogId();
		this.catalogName = model.getCatalogName();
		this.catalogOptions = model.getCatalogOptions();
		return this;
	}
}
