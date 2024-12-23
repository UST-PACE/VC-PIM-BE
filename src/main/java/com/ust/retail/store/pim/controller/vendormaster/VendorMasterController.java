package com.ust.retail.store.pim.controller.vendormaster;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.catalogs.VendorPaymentTermCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDropdownDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterFilterDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterStoreDTO;
import com.ust.retail.store.pim.service.vendormaster.VendorMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/vendormaster/p/")
@Validated
public class VendorMasterController extends BaseController {

	private final VendorMasterService vendorMasterService;
	private final VendorPaymentTermCatalog vendorPaymentTermCatalog;

	@Autowired
	public VendorMasterController(VendorMasterService vendorMasterService, VendorPaymentTermCatalog vendorPaymentTermCatalog) {

		super();
		this.vendorMasterService = vendorMasterService;
		this.vendorPaymentTermCatalog = vendorPaymentTermCatalog;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public VendorMasterDTO create(@Valid @RequestBody VendorMasterDTO vendorMasterDTO) {
		return vendorMasterService.saveOrUpdate(vendorMasterDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public VendorMasterDTO update(@Valid @RequestBody VendorMasterDTO vendorMasterDTO) {
		return vendorMasterService.saveOrUpdate(vendorMasterDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/add")
	@Validated(OnCreate.class)
	public VendorMasterStoreDTO addStoreNumber(@Valid @RequestBody VendorMasterStoreDTO vendorMasterStoreDTO) {
		return vendorMasterService.addStoreNumber(vendorMasterStoreDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@DeleteMapping("/remove/sn/{storeNumId}/vm/{vendorMasterId}")
	public GenericResponse removeStoreNumber(@Valid @PathVariable(value = "storeNumId") Long storeNumId,
											 @Valid @PathVariable(value = "vendorMasterId") Long vendorMasterId) {

		vendorMasterService.removeStoreNumber(storeNumId, vendorMasterId);

		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "true");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public VendorMasterDTO findById(@PathVariable(value = "id") Long vendorMasterId) {
		return vendorMasterService.findById(vendorMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/autocomplete/vendor/name/{vendorName}")
	public List<VendorMasterDTO> findVendorNameAutoCompleteOptions(@PathVariable(value = "vendorName") String vendorName) {
		return vendorMasterService.getAutocompleteVendorName(vendorName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/autocomplete/vendor/code/{vendorCode}")
	public List<VendorMasterDTO> findVendorCodeAutoCompleteOptions(@PathVariable(value = "vendorCode") String vendorCode) {
		return vendorMasterService.getAutocompleteVendorCode(vendorCode);
	}


	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<VendorMasterFilterDTO> findByFilters(@Valid @RequestBody VendorMasterFilterDTO vendorMasterFilterDTO) {
		return vendorMasterService.getVendorMasterByFilters(vendorMasterFilterDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load")
	public List<VendorMasterDropdownDTO> loadCatalog() {
		return vendorMasterService.load();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load/catalog/vendorpaymenttermdays")
	public List<CatalogDTO> loadPayments() {
		return vendorPaymentTermCatalog.getCatalogOptions();
	}

}
