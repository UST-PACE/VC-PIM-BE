package com.ust.retail.store.pim.repository.menuconfigurator;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ust.retail.store.pim.model.menuconfigurator.MenuConfiguratorProductModel;

public interface MenuConfiguratorProductRepository extends JpaRepository<MenuConfiguratorProductModel, Long> {

	@Transactional
	@Modifying
	@Query(value = "DELETE MenuConfiguratorProductModel mcp WHERE mcp.menuConfiguratorProduct.upcMasterId = ?1")
	void deleteByMenuConfiguratorProductId(Long upcMasterId);
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE MenuConfiguratorProductModel mcp SET mcp.vcEnabled = :status WHERE mcp.menuConfiguratorProduct.upcMasterId = :upcMasterId")
	void updateMenuConfiguratorVcStatus(@Param("status") boolean vcStatus,@Param("upcMasterId") Long upcMasterId);
	
	List<MenuConfiguratorProductModel> findByMenuConfiguratorProductUpcMasterId(Long upcMasterId);
}
