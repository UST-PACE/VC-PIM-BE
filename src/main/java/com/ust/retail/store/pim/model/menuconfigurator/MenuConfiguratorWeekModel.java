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
import com.ust.retail.store.pim.dto.menuconfigurator.MenuConfiguratorWeekDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "menu_configurator_weeks")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class MenuConfiguratorWeekModel implements Serializable {
	 
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_configurator_week_id")
	private Long menuConfiguratorWeekId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "week", referencedColumnName = "catalog_id")
	private CatalogModel week;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_configurator_id")
	private MenuConfiguratorModel menuConfigurator;
	
	@OneToMany(mappedBy = "menuConfiguratorWeek", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuConfiguratorDayModel> menuConfiguratorDays = new ArrayList<>();
	
	public MenuConfiguratorWeekModel(MenuConfiguratorWeekDTO dto, MenuConfiguratorModel menuConfigurator) {
		this.menuConfigurator = menuConfigurator;
		this.week = new CatalogModel(dto.getWeekId());
		this.menuConfiguratorDays.addAll(dto.getMenuDays().stream().map(d -> new MenuConfiguratorDayModel(d, this)).collect(Collectors.toList()));
	}
	

}
