package com.ust.retail.store.bistro.repository.wastage;

import com.ust.retail.store.bistro.model.wastage.WastageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.List;

public interface WastageRepository extends JpaRepository<WastageModel, Long> {
	List<WastageModel> findByExecutionDate(Date executionDate);

	@Query("SELECT"
			+ "   w.executionDate,"
			+ "   u.nameDesc,"
			+ "   p.principalUpc,"
			+ "   p.productName,"
			+ "   w.wholeDish,"
			+ "   i.principalUpc,"
			+ "   i.productName,"
			+ "   d.wastedAmount,"
			+ "   su.catalogOptions"
			+ " FROM WastageModel w"
			+ "   JOIN w.recipe.relatedUpcMaster p"
			+ "   JOIN w.userCreate u"
			+ "   LEFT JOIN w.details d"
			+ "   LEFT JOIN d.ingredient i"
			+ "   LEFT JOIN d.wastedUnit su"
			+ " WHERE w.executionDate BETWEEN ?1 AND ?2"
	)
	List<Tuple> getProductionWasteReport(Date startDate, Date endDate);

	@Query(value = "SELECT w"
			+ " FROM MenuEntryDayModel d"
			+ "   JOIN d.menuEntry m"
			+ "   JOIN m.recipe r"
			+ "   JOIN r.relatedUpcMaster p"
			+ "   JOIN m.store sn"
			+ "   JOIN WastageModel w ON w.recipe = r"
			+ " WHERE sn.storeNumId = :storeId"
			+ "   AND (:recipeName = NULL OR p.productName LIKE %:recipeName%)"
			+ "   AND d.day = :weekDay"
			+ "   AND :time BETWEEN d.start AND d.end"
			+ "   AND w.executionDate = :executionDate"
			+ "   AND p.productGroup.displayExternally = true"
			+ " GROUP BY w"
	)
	Page<WastageModel> findCurrentByStoreAndRecipeName(Long storeId,
													   String recipeName,
													   Integer weekDay,
													   Integer time,
													   Date executionDate,
													   Pageable pageable);
}
