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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ust.retail.store.pim.dto.menuconfigurator.MenuConfiguratorDayDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_configurator_days")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class MenuConfiguratorDayModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_configurator_day_id")
	private Long menuConfiguratorDayId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "day", referencedColumnName = "catalog_id")
	private CatalogModel day;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_configurator_week_id")
	private MenuConfiguratorWeekModel menuConfiguratorWeek;

	@OneToMany(mappedBy = "menuConfiguratorDay", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuConfiguratorProductModel> menuConfiguratorProducts = new ArrayList<>();

	public MenuConfiguratorDayModel(MenuConfiguratorDayDTO dto, MenuConfiguratorWeekModel menuConfiguratorWeek) {
		this.menuConfiguratorWeek = menuConfiguratorWeek;
		this.day = new CatalogModel(dto.getDayId());
		this.menuConfiguratorProducts.addAll(dto.getMenuProducts().stream().map(p -> new MenuConfiguratorProductModel(p, this)).collect(Collectors.toList()));
	}

}
