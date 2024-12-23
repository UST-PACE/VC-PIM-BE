package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.ProductSubcategoryDTO;
import com.ust.retail.store.pim.service.catalog.ProductSubcategoryService;
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
@RequestMapping(path = "/api/catalogs/p/productsubcategory")
@Validated
public class ProductSubcategoryController extends BaseController {

	private final ProductSubcategoryService productSubcategoryService;

	@Autowired
	public ProductSubcategoryController(ProductSubcategoryService productSubcategoryService) {
		super();
		this.productSubcategoryService = productSubcategoryService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public ProductSubcategoryDTO create(@Valid @RequestBody ProductSubcategoryDTO productSubcategoryDTO) {
		return productSubcategoryService.saveOrUpdate(productSubcategoryDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public ProductSubcategoryDTO update(@Valid @RequestBody ProductSubcategoryDTO productSubcategoryDTO) {
		return productSubcategoryService.saveOrUpdate(productSubcategoryDTO);
	}
	

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public ProductSubcategoryDTO findById(@PathVariable(value = "id") Long productSubcategoryId) {
		return productSubcategoryService.findById(productSubcategoryId);
	}	 

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<ProductSubcategoryDTO> findByFilters(@Valid @RequestBody ProductSubcategoryDTO productSubcategoryDTO) {
		return productSubcategoryService.getProductSubcategoriesByFilters(productSubcategoryDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/productcategory/{productCategoryId}/autocomplete/{subcategoryName}")
	public List<ProductSubcategoryDTO> findAutoCompleteOptions(@PathVariable(value = "productCategoryId") Long productCategoryId, @PathVariable(value = "subcategoryName") String subcategoryName) {
		return productSubcategoryService.getAutocomplete(productCategoryId, subcategoryName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load/pc/{productCategoryId}")
	public List<ProductSubcategoryDTO> loadCatalog(@PathVariable(value = "productCategoryId") Long productCategoryId) {
		return productSubcategoryService.load(productCategoryId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/uploadpicture")
	public String uploadPicture(@RequestParam("productSubcategoryId") Long productSubcategoryId, @RequestParam("file") MultipartFile file) {
		log.info("Received file: {} for productSubcategoryId: {}", file.getOriginalFilename(), productSubcategoryId);

		return productSubcategoryService.updateProductSubcategoryPicture(productSubcategoryId, file);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@DeleteMapping("/removepicture/{productSubcategoryId}")
	public GenericResponse removePicture(@Valid @PathVariable(value = "productSubcategoryId") Long productSubcategoryId) {
		return productSubcategoryService.removeProductSubcategoryPicture(productSubcategoryId);
	}
}
