package com.ust.retail.store.pim.repository.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ust.retail.store.pim.model.inventory.InventoryProductReturnDetailModel;

public interface InventoryProductReturnDetailRepository extends JpaRepository<InventoryProductReturnDetailModel, Long> {
}
