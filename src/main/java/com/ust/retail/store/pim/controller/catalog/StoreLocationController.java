package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.StoreLocationDTO;
import com.ust.retail.store.pim.service.catalog.StoreLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/catalogs/p/storelocation")
@Validated
public class StoreLocationController extends BaseController {

	private final StoreLocationService storeLocationService;

	@Autowired
	public StoreLocationController(StoreLocationService storeLocationService) {
		
		super();
		this.storeLocationService = storeLocationService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public StoreLocationDTO create(@Valid @RequestBody StoreLocationDTO storeLocationDTO) {
		return storeLocationService.save(storeLocationDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public StoreLocationDTO update(@Valid @RequestBody StoreLocationDTO storeLocationDTO) {
		return storeLocationService.update(storeLocationDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/find/id/{id}")
	public StoreLocationDTO findById(@PathVariable(value = "id") Long storeLocationId) {
		return storeLocationService.findById(storeLocationId);
	}	 

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<StoreLocationDTO> findByFilters(@Valid @RequestBody StoreLocationDTO storeLocationDTO) {
		return storeLocationService.getStoreLocationsByFilters(storeLocationDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE')")
	@GetMapping("/load/sn/{storeNumId}")
	public List<StoreLocationDTO> loadCatalog(@PathVariable(value = "storeNumId", required = false) Long storeNumId) {
		return storeLocationService.load(storeNumId);
	}
	
	
}
