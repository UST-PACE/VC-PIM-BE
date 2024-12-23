package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.ProductGroupDTO;
import com.ust.retail.store.pim.service.catalog.ProductGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api/catalogs/p/productgroup")
@Validated
public class ProductGroupController extends BaseController {

	private final ProductGroupService productGroupService;

	@Autowired
	public ProductGroupController(ProductGroupService productGroupService) {
		super();
		this.productGroupService = productGroupService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public ProductGroupDTO create(@Valid @RequestBody ProductGroupDTO productGroupDTO) {
		return productGroupService.saveOrUpdate(productGroupDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public ProductGroupDTO update(@Valid @RequestBody ProductGroupDTO productGroupDTO) {
		return productGroupService.saveOrUpdate(productGroupDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/find/id/{id}")
	public ProductGroupDTO findById(@PathVariable(value = "id") Long productGroupId) {
		return productGroupService.findById(productGroupId);
	}	 

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<ProductGroupDTO> findByFilters(@Valid @RequestBody ProductGroupDTO productGroupDTO) {
		return productGroupService.getProductGroupsByFilters(productGroupDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER')")
	@GetMapping("/autocomplete/{groupName}")
	public List<ProductGroupDTO> findAutoCompleteOptions(@PathVariable(value = "groupName") String storeName) {
		return productGroupService.getAutocomplete(storeName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load")
	public List<ProductGroupDTO> loadCatalog() {
		return productGroupService.load();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@PostMapping("/uploadpicture")
	public String  uploadPicture(@RequestParam("productGroupId") Long productGroupId, @RequestParam("file") MultipartFile file) {
		log.info("Received file: {} for productGroupId: {}", file.getOriginalFilename(), productGroupId);

		return productGroupService.updateProductGroupPicture(productGroupId, file);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@DeleteMapping("/removepicture/{productGroupId}")
	public GenericResponse removePicture(@Valid @PathVariable(value = "productGroupId") Long productGroupId) {
		return productGroupService.removeProductGroupPicture(productGroupId);
	}
}
