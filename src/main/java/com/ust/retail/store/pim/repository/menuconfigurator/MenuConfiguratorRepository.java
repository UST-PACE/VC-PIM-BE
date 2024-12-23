package com.ust.retail.store.pim.repository.menuconfigurator;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorModel;

public interface MenuConfiguratorRepository extends JpaRepository<MenuConfiguratorModel, Long>{

	@Query("SELECT DISTINCT m FROM MenuConfiguratorModel m "
			+ "WHERE "
			+ "	(m.deleted = false) AND "
			+ " (?1 IS NULL OR m.year = ?1) AND "
			+ " (?2 IS NULL OR m.quarter.catalogId = ?2) AND "
			+ " (?3 IS NULL OR m.month.catalogId = ?3)")
	Page<MenuConfiguratorModel> findByFilters(Long year, Long quarter, Long month, Pageable pageable);

	@Query(value = "SELECT DISTINCT p.menuConfiguratorProduct.upcMasterId FROM MenuConfiguratorModel m "
			+ "LEFT JOIN m.menuConfiguratorWeeks w ON m = w.menuConfigurator "
			+ "LEFT JOIN w.menuConfiguratorDays d ON d.menuConfiguratorWeek = w "
			+ "LEFT JOIN d.menuConfiguratorProducts p ON p.menuConfiguratorDay = d "
			+ "WHERE (m.deleted = false) AND"
			+ "(m.year = ?1) AND "
			+ "(m.quarter.catalogId = ?2) AND "
			+ "(m.month.catalogId = ?3) AND "
			+ "(w.week.catalogId = ?4) AND "
			+ "(d.day.catalogId = ?5) AND "
			+ "(p.menuConfiguratorProduct.productCategory.productCategoryId = ?6)")
	List<Long> findByFilters(Long year, Long quarter, Long month, Long week, Long day, Long categoryId);
	
	@Query(value = "SELECT DISTINCT m FROM MenuConfiguratorModel m "
			+ "LEFT JOIN m.menuConfiguratorWeeks w ON m = w.menuConfigurator "
			+ "LEFT JOIN w.menuConfiguratorDays d ON d.menuConfiguratorWeek = w "
			+ "LEFT JOIN d.menuConfiguratorProducts p ON p.menuConfiguratorDay = d "
			+ "LEFT JOIN UpcMasterModel u ON u = p.menuConfiguratorProduct "
			+ "LEFT JOIN u.upcSellingChannels usc ON usc.upcMaster = u "
			+ "WHERE (m.deleted = false) AND"
			+ "(m.year = ?1) AND "
			+ "(m.month.catalogId = ?2) AND "
			+ "(?3 IS NULL OR w.week.catalogId = ?3) AND "
			+ "(?4 IS NULL OR d.day.catalogId = ?4) AND "
			+ "(u.upcMasterStatus.catalogId != ?5) AND (usc.channel.catalogId =?6 AND usc.enabled = true)")
	List<MenuConfiguratorModel> getScheduledMenuProducts(Long year, Long month, Long week, Long day, Long discountinueStatus, Long vcUpcSellingChannel);
	
	List<MenuConfiguratorModel> findByYearAndDeletedFalse(Long year);
}
