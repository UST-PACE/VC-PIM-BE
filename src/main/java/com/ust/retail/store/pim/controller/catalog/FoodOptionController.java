package com.ust.retail.store.pim.controller.catalog;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.catalog.FoodOptionDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterFilterDTO;
import com.ust.retail.store.pim.service.catalog.FoodOptionService;
import com.ust.retail.store.pim.service.productmaster.ProductScreenConfigFacade;

@RestController
@RequestMapping(path = "/api/catalogs/p/foodoption")
@Validated
public class FoodOptionController {

	private final FoodOptionService foodOptionService;
	
	private final ProductScreenConfigFacade productScreenConfigFacade;

	@Autowired
	public FoodOptionController(FoodOptionService foodOptionService,
			ProductScreenConfigFacade productScreenConfigFacade) {
		super();
		this.foodOptionService = foodOptionService;
		this.productScreenConfigFacade = productScreenConfigFacade;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public FoodOptionDTO create(@Valid @RequestBody FoodOptionDTO foodOptionDto) {
		return foodOptionService.saveOrUpdate(foodOptionDto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/update")
	@Validated(OnUpdate.class)
	public FoodOptionDTO update(@Valid @RequestBody FoodOptionDTO foodOptionDto) {
		return foodOptionService.saveOrUpdate(foodOptionDto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public FoodOptionDTO findById(@PathVariable(value = "id") Long foodOptionId) {
		return foodOptionService.findById(foodOptionId);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<FoodOptionDTO> findByFilters(@Valid @RequestBody FoodOptionDTO foodOptionDto) {
		return foodOptionService.getFoodOptionByFilters(foodOptionDto);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/load")
	public List<UpcMasterFilterDTO> loadCatalog(@RequestBody UpcMasterFilterDTO upcMasterFilterDTO) {
		return foodOptionService.load(upcMasterFilterDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("cataloguename/autocomplete/{catalogueName}")
	public List<FoodOptionDTO> findAutoCompleteOptions(@PathVariable(value = "catalogueName") String catalogueName) {
		return foodOptionService.getAutocomplete(catalogueName);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("delete/{foodOptionId}")
	public GenericResponse deleteById (@PathVariable(value = "foodOptionId") Long foodOptionId) {
		return foodOptionService.deleteById(foodOptionId);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/status/load")
	public List<CatalogDTO> loadStatusCatalog() {
		return productScreenConfigFacade.getFoodOptionStatusCatelog();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/food-option-csv/csv")
	public ResponseEntity<Resource> getUpcForVcCsv() throws IOException {
		return getResponse(foodOptionService.getFoodOptionExport(), Optional.of(String.format("upc_master_data_%tF", new Date())));
	}


	private ResponseEntity<Resource> getResponse(FileSystemResource resource, Optional<String> reportFileName) throws IOException {
		String fileName = reportFileName.orElse("report");
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
		headers.add(HttpHeaders.PRAGMA, "no-cache");
		headers.add(HttpHeaders.EXPIRES, "0");
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".csv\"");

		return ResponseEntity.ok()
				.headers(headers)
				.contentLength(resource.contentLength())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(resource);
	}

}
