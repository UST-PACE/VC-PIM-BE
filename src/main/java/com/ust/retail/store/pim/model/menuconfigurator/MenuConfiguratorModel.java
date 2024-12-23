package com.ust.retail.store.pim.model.menuconfigurator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ust.retail.store.pim.dto.menuconfigurator.MenuConfiguratorDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_configurator")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class MenuConfiguratorModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_configurator_id")
	private Long menuConfiguratorId;
	
	private Long year;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quarter", referencedColumnName = "catalog_id")
	private CatalogModel quarter;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "month", referencedColumnName = "catalog_id")
	private CatalogModel month;
	
	private Boolean deleted;
	
	@OneToMany(mappedBy = "menuConfigurator", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuConfiguratorWeekModel> menuConfiguratorWeeks = new ArrayList<>();
	
	public MenuConfiguratorModel createModel(MenuConfiguratorDTO dto, Long userId) {
		this.year = dto.getYear();
		this.quarter = new CatalogModel(dto.getQuarterId());
		this.month = new CatalogModel(dto.getMonthId());
		this.menuConfiguratorWeeks.addAll(dto.getMenuWeeks().stream().map(w -> new MenuConfiguratorWeekModel(w, this)).collect(Collectors.toList()));
		this.userCreate = new UserModel(userId);
		this.deleted = false;
		return this;
	}	
}
