package com.ust.retail.store.pim.repository.inventory;

import com.ust.retail.store.pim.dto.inventory.reception.operation.ReceptionWarningDTO;
import com.ust.retail.store.pim.model.inventory.PoReceiveWarningModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

@Repository
public interface PoReceiveWarningRepository extends JpaRepository<PoReceiveWarningModel, Long> {

	@Query("SELECT new com.ust.retail.store.pim.dto.inventory.reception.operation.ReceptionWarningDTO("
			+ "a.pk.warningReason.catalogId,"
			+ "a.pk.warningReason.catalogOptions,"
			+ "a.qty)"
			+ " FROM PoReceiveWarningModel a"
			+ " WHERE a.pk.poReceiveDetail.poReceiveDetailId=?1")
	List<ReceptionWarningDTO> getWarningByPoReceiveDetailId(Long poReceiveDetailId);

	@Query("SELECT a FROM PoReceiveWarningModel a"
			+ " WHERE a.pk.poReceiveDetail.purchaseOrderDetail.purchaseOrder.purchaseOrderId=?1")
	List<PoReceiveWarningModel> checkIncompleteReception(@Valid Long purchaseOrderId);
}
