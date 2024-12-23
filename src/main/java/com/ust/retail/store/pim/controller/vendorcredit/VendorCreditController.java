package com.ust.retail.store.pim.controller.vendorcredit;

import com.ust.retail.store.pim.common.annotations.OnAuthorize;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnRecover;
import com.ust.retail.store.pim.common.annotations.OnReject;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.catalogs.InventoryProductReturnStatusCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.productreturn.CalculateCreditsDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnFilterDTO;
import com.ust.retail.store.pim.dto.productreturn.ProductReturnFilterResultDTO;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationDTO;
import com.ust.retail.store.pim.dto.productreturn.authorization.ProductReturnAuthorizationResultDTO;
import com.ust.retail.store.pim.service.vendorcredit.VendorCreditService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/vendorcredit/p")
@Validated
public class VendorCreditController extends BaseController {

	private final VendorCreditService vendorCreditService;
	private final InventoryProductReturnStatusCatalog inventoryProductReturnStatusCatalog;

	public VendorCreditController(
			VendorCreditService vendorCreditService,
			InventoryProductReturnStatusCatalog inventoryProductReturnStatusCatalog) {
		this.vendorCreditService = vendorCreditService;
		this.inventoryProductReturnStatusCatalog = inventoryProductReturnStatusCatalog;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/catalog/status")
	public List<CatalogDTO> loadVendorCreditStatusCatalog() {
		return inventoryProductReturnStatusCatalog.getCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<ProductReturnFilterResultDTO> findByFilters(@Valid @RequestBody ProductReturnFilterDTO dto) {
		return vendorCreditService.getVendorCreditsByFilters(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/find/id/{id}")
	public ProductReturnDTO findById(@PathVariable(value = "id") Long returnId) {
		return vendorCreditService.findById(returnId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/authorize")
	@Validated(OnAuthorize.class)
	public ProductReturnAuthorizationResultDTO authorizeCredits(@Valid @RequestBody ProductReturnAuthorizationDTO dto) {
		return vendorCreditService.authorize(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/reject")
	@Validated(OnReject.class)
	public ProductReturnAuthorizationResultDTO rejectCredits(@Valid @RequestBody ProductReturnAuthorizationDTO dto) {
		return vendorCreditService.decline(dto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/calculate/credits")
	@Validated(OnRecover.class)
	public CalculateCreditsDTO calculateCredits(@Valid @RequestBody CalculateCreditsDTO dto) {
		return vendorCreditService.calculateCredits(dto);
	}

}
