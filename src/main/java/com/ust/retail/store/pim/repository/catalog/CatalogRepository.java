package com.ust.retail.store.pim.repository.catalog;

import com.ust.retail.store.pim.model.catalog.CatalogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatalogRepository extends JpaRepository<CatalogModel, Long> {

	List<CatalogModel> findByCatalogName(String catalogName);
}
