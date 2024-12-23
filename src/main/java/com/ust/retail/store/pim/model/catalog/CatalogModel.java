package com.ust.retail.store.pim.model.catalog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "catalogs",
		uniqueConstraints = @UniqueConstraint(
				name = "uq_catalog_name_option",
				columnNames = {"catalog_name", "catalog_options"}))
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CatalogModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "catalog_id")
	private Long catalogId;

	@NotBlank
	@Column(name = "catalog_name", length = 60)
	private String catalogName;

	@NotBlank
	@Column(name = "catalog_options", length = 60)
	private String catalogOptions;

	public CatalogModel(Long catalogId, String catalogName, String catalogOptions) {
		this.catalogId = catalogId;
		this.catalogName = catalogName;
		this.catalogOptions = catalogOptions;
	}

	public CatalogModel(Long catalogId) {
		this.catalogId = catalogId;
	}

	public void setValues(String catalogName, String catalogOptions) {
		this.catalogName = catalogName;
		this.catalogOptions = catalogOptions;
	}
}
