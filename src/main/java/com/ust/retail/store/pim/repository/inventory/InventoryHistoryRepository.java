package com.ust.retail.store.pim.repository.inventory;

import com.ust.retail.store.pim.model.inventory.InventoryHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistoryModel, Long> {

	Optional<List<InventoryHistoryModel>> findByTxnNumAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(Long txtNum, Long authorizationStatusId);

	Optional<List<InventoryHistoryModel>> findByReferenceIdAndAuthorizationStatusCatalogIdAndOperationModuleCatalogIdOrderByInventoryHistoryIdDesc(Long referenceId, Long authorizationStatusId, Long operationModuleId);

	Optional<List<InventoryHistoryModel>> findByInventoryHistoryIdAndAuthorizationStatusCatalogIdOrderByInventoryHistoryIdDesc(Long inventoryHistoryId, Long authorizationStatusId);

	List<InventoryHistoryModel> findByReferenceIdAndOperationModuleCatalogId(Long referenceId, Long operationModuleId);

	List<InventoryHistoryModel> findByReferenceIdAndOperationModuleCatalogIdAndInventoryUpcMasterUpcMasterId(Long referenceId, Long operationModuleId, Long upcMasterId);

	@Modifying
	@Query(value = "DELETE FROM InventoryHistoryModel a WHERE a.txnNum = ?1")
	void deleteByTxnNum(Long txnNum);

	@Query("FROM InventoryHistoryModel ih"
			+ "   JOIN FETCH ih.inventory i"
			+ "   JOIN FETCH i.upcMaster p"
			+ "   JOIN FETCH i.storeLocation sl"
			+ "   JOIN FETCH ih.operationModule m"
			+ "   JOIN FETCH ih.operationType ot"
			+ "   JOIN FETCH ih.authorizationStatus st"
			+ " WHERE ih.createdAt BETWEEN ?1 AND ?2"
	)
	List<InventoryHistoryModel> getInventoryMovementReport(Date startDate, Date endDate);

	@Query("FROM InventoryHistoryModel ih"
			+ " WHERE ih.createdAt BETWEEN ?1 AND ?2"
			+ "   AND ih.operationModule.catalogId = ?3"
			+ "   AND ih.operationType.catalogId = ?4"
	)
	List<InventoryHistoryModel> getUnsoldProductReport(Date startDate, Date endDate, Long bistroOperationModuleId, Long bistroTossingOperationTypeId);

	@Query("SELECT ih.inventory.upcMaster.upcMasterId, SUM(ih.operationQty)"
			+ " FROM InventoryHistoryModel ih"
			+ " WHERE ih.createdAt BETWEEN :startDate AND :endDate"
			+ "   AND ih.inventory.upcMaster.upcMasterId IN :upcList"
			+ "   AND ih.operationModule.catalogId = :bistroOperationModuleId"
			+ "   AND ih.operationType.catalogId = :bistroTossingOperationTypeId"
			+ " GROUP BY ih.inventory.upcMaster.upcMasterId"
	)
	List<Tuple> getTossingForPeriodAndUpcList(Date startDate,
											  Date endDate,
											  List<Long> upcList,
											  Long bistroOperationModuleId,
											  Long bistroTossingOperationTypeId);

}
