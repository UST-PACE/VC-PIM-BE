package com.ust.retail.store.pim.controller.upcmaster;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.upcmaster.PrincipalUpcDTO;
import com.ust.retail.store.pim.dto.upcmaster.SimpleUpcDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcVendorDetailDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcVendorDetailFiltersDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.service.upcmaster.UpcVendorDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/vendormaster/p/upc")
@Validated
public class UpcVendorDetailsController extends BaseController {

	private final UpcVendorDetailsService upcVendorDetailsService;

	@Autowired
	public UpcVendorDetailsController(UpcVendorDetailsService upcVendorDetailsService) {
		
		super();
		this.upcVendorDetailsService = upcVendorDetailsService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public UpcVendorDetailDTO create(@Valid @RequestBody UpcVendorDetailDTO upcVendorDetailDTO) {
		return upcVendorDetailsService.saveOrUpdate(upcVendorDetailDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public UpcVendorDetailDTO update(@Valid @RequestBody UpcVendorDetailDTO upcVendorDetailDTO) {
		return upcVendorDetailsService.saveOrUpdate(upcVendorDetailDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<UpcVendorDetailFiltersDTO> findByFilters(@Valid @RequestBody UpcVendorDetailFiltersDTO upcMasterFiltersDTO ) {
		return upcVendorDetailsService.getUpcVendorDetailsByFilters(upcMasterFiltersDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public UpcVendorDetailDTO findById(@PathVariable(value = "id") Long upcVendorDetailId) {
		return upcVendorDetailsService.findById(upcVendorDetailId);
	}	
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/lookup/code/{code}")
	public PrincipalUpcDTO principalUPCLookup(@PathVariable(value = "code") String code) {
		return upcVendorDetailsService.principalUpcLookup(code);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/defaultvendor/{upcMasterId}")
	public VendorMasterDTO findDefaultVendorForProduct(@PathVariable("upcMasterId") Long upcMasterId) {
		return upcVendorDetailsService.findDefaultVendorFor(upcMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/vm/{vendorMasterId}/simple")
	public List<SimpleUpcDTO> findProductsForVendor(@PathVariable("vendorMasterId") Long vendorMasterId) {
		return upcVendorDetailsService.findProductsForVendor(vendorMasterId);
	}
}
