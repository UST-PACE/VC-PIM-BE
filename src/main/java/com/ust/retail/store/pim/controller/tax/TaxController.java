package com.ust.retail.store.pim.controller.tax;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.common.catalogs.TaxTypeCatalog;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.tax.TaxDTO;
import com.ust.retail.store.pim.dto.tax.TaxFilterDTO;
import com.ust.retail.store.pim.dto.tax.TaxFilterResultDTO;
import com.ust.retail.store.pim.service.tax.TaxService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/tax/p")
@Validated
public class TaxController extends BaseController {
	private final TaxService taxService;
	private final TaxTypeCatalog taxTypeCatalog;

	public TaxController(TaxService taxService, TaxTypeCatalog taxTypeCatalog) {
		this.taxService = taxService;
		this.taxTypeCatalog = taxTypeCatalog;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public TaxDTO create(@Valid @RequestBody TaxDTO taxDTO) {
		return taxService.saveOrUpdate(taxDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public TaxDTO update(@Valid @RequestBody TaxDTO taxDTO) {
		return taxService.saveOrUpdate(taxDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@DeleteMapping("/delete/id/{id}")
	public GenericResponse deleteById(@PathVariable(value = "id") Long taxId) {
		taxService.deleteById(taxId);
		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "true");
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/find/id/{id}")
	public TaxDTO findById(@PathVariable(value = "id") Long taxId) {
		return taxService.findById(taxId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<TaxFilterResultDTO> findByFilters(@Valid @RequestBody TaxFilterDTO filterDTO) {
		return taxService.getTaxesByFilters(filterDTO);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STORE_MANAGER', 'ROLE_BISTRO_MANAGER')")
	@GetMapping("/types/load")
	public List<CatalogDTO> getTaxTypes() {
		return taxTypeCatalog.getCatalogOptions();
	}
}
