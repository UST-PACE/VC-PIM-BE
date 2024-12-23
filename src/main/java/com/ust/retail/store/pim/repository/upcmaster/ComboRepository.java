package com.ust.retail.store.pim.repository.upcmaster;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ust.retail.store.pim.dto.upcmaster.ComboDTO;
import com.ust.retail.store.pim.model.upcmaster.ComboModel;

public interface ComboRepository extends JpaRepository<ComboModel, Long> {

	@Query(value = "SELECT c FROM ComboModel AS c "
			+ "WHERE (c.deleted = false) AND (:comboName IS NULL OR c.comboName LIKE CONCAT('%', :comboName, '%')) "
			+ "AND (:comboStatusId IS NULL OR c.comboStatus.catalogId = :comboStatusId)")
	Page<ComboModel> findByFilter(@Param("comboName") String comboName, @Param("comboStatusId") Long comboStatusId,
			Pageable pageable);
	
	List<ComboModel> findAllByDeletedFalse();
	
	@Transactional
	@Modifying
	@Query(value = "UPDATE ComboModel c SET c.deleted = true WHERE c.comboId = ?1")
	void deleteById(Long id);
	
	@Query(value = "SELECT new com.ust.retail.store.pim.dto.upcmaster.ComboDTO(i.comboId, i.comboName)"
			+ " FROM ComboModel i "
			+ " WHERE (i.deleted = false) AND (?1 = NULL OR i.comboName LIKE %?1%)")
	List<ComboDTO> getAutocompleteList(String comboName);
	
	@Query(value = "SELECT CASE " +
            "WHEN c.principalUpc = ?1 THEN 'principalUpc' " +
            "WHEN c.comboName = ?2 THEN 'comboName' " +
            "END AS result " +
            "FROM ComboModel c " +
            "WHERE (c.deleted = false) "
            +"AND (c.principalUpc = ?1 OR c.comboName = ?2) " +
            "AND (?3 IS NULL OR c.comboId != ?3) ")
	List<String> existsByUpcOrComboName(String upc, String comboName, Long id);
	
	@Query(value = "SELECT DISTINCT c FROM ComboModel AS c " 
	        + "JOIN ComboProductCategoryModel AS cpc ON c = cpc.combo "
			+ "WHERE c.deleted = false " 
			+"AND cpc.productCategory.productCategoryId IN (:categoryIds) "
			+"AND (:comboId IS NULL OR  c.comboId != :comboId)")
	List<ComboModel> findByCategoryIds(@Param("categoryIds") Set<Long> categoryIds, @Param("comboId") Long comboId);
	
}
