package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.dto.security.StoreNumberInfoDTO;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/catalogs/p/storenumber")
@Validated
public class StoreNumberController extends BaseController {

	private final StoreNumberService storeNumberService;

	@Autowired
	public StoreNumberController(StoreNumberService storeNumberService) {
		
		super();
		this.storeNumberService = storeNumberService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public StoreNumberDTO create(@Valid @RequestBody StoreNumberDTO storeNumberDTO) {
		return storeNumberService.create(storeNumberDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public StoreNumberDTO update(@Valid @RequestBody StoreNumberDTO storeNumberDTO) {
		return storeNumberService.update(storeNumberDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public StoreNumberDTO findById(@PathVariable(value = "id") Long storeNumberId) {
		return storeNumberService.findById(storeNumberId);
	}	 

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<StoreNumberDTO> findByFilters(@Valid @RequestBody StoreNumberDTO storeNumberDTO) {
		return storeNumberService.getStoreNumbersByFilters(storeNumberDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/autocomplete/{storeName}")
	public List<StoreNumberDTO> findAutoCompleteOptions(@PathVariable(value = "storeName") String storeName) {
		return storeNumberService.getAutocomplete(storeName);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load")
	public List<StoreNumberDTO> loadCatalog() {
		return storeNumberService.load();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load/vm/{id}")
	public List<StoreNumberDTO> loadCatalogByVendorMasterId(@PathVariable(value = "id") Long vendorMasterId) {
		return storeNumberService.loadByVendorMasterId(vendorMasterId);
	}


	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/info/sn/{storeNumId}")
	public StoreNumberInfoDTO changeStoreNumber(@PathVariable(value = "storeNumId", required = false) Long storeNumId) {
		StoreNumberDTO storeNumber = storeNumberService.findById(storeNumId);
		return new StoreNumberInfoDTO(storeNumber.getStoreNumId(), storeNumber.getStoreName());
	}

}
