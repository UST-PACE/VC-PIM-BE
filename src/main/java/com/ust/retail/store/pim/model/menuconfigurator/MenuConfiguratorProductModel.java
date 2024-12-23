package com.ust.retail.store.pim.model.menuconfigurator;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ust.retail.store.pim.dto.menuconfigurator.MenuConfiguratorProductDTO;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_configurator_products")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class MenuConfiguratorProductModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_category_product_Id")
	private Long menuCategoryProductId;
	
	@ManyToOne
	@JoinColumn(name = "upc_master_id")
	private UpcMasterModel menuConfiguratorProduct;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_configurator_day_id")
	private MenuConfiguratorDayModel menuConfiguratorDay;
	
	private boolean vcEnabled;

	public MenuConfiguratorProductModel(MenuConfiguratorProductDTO dto, MenuConfiguratorDayModel menuConfiguratorDay) {
		this.menuConfiguratorDay = menuConfiguratorDay;
		this.menuConfiguratorProduct = new UpcMasterModel(dto.getUpcMasterId());
		this.vcEnabled = true;
	}

}
