package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.ProductCategoryDTO;
import com.ust.retail.store.pim.service.catalog.ProductCategoryService;
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
@RequestMapping(path = "/api/catalogs/p/productcategory")
@Validated
public class ProductCategoryController extends BaseController {

	private final ProductCategoryService productCategoryService;

	@Autowired
	public ProductCategoryController(ProductCategoryService productCategoryService) {
		super();
		this.productCategoryService = productCategoryService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public ProductCategoryDTO create(@Valid @RequestBody ProductCategoryDTO productCategoryDTO) {
		return productCategoryService.saveOrUpdate(productCategoryDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public ProductCategoryDTO update(@Valid @RequestBody ProductCategoryDTO productCategoryDTO) {
		return productCategoryService.saveOrUpdate(productCategoryDTO);
	}
	

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public ProductCategoryDTO findById(@PathVariable(value = "id") Long productCategoryId) {
		return productCategoryService.findById(productCategoryId);
	}	 

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<ProductCategoryDTO> findByFilters(@Valid @RequestBody ProductCategoryDTO productCategoryDTO) {
		return productCategoryService.getProductCategoriesByFilters(productCategoryDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/productgroup/{productGroupId}/autocomplete/{categoryName}")
	public List<ProductCategoryDTO> findAutoCompleteOptions(@PathVariable(value = "productGroupId") Long productGroupId, @PathVariable(value = "categoryName") String categoryName) {
		return productCategoryService.getAutocomplete(productGroupId, categoryName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_ASSOCIATE') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load/pg/{productGroupId}")
	public List<ProductCategoryDTO> loadCatalog(@PathVariable(value = "productGroupId") Long productGroupId) {
		return productCategoryService.load(productGroupId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/uploadpicture")
	public String uploadPicture(@RequestParam("productCategoryId") Long productCategoryId, @RequestParam("file") MultipartFile file) {
		log.info("Received file: {} for productCategoryId: {}", file.getOriginalFilename(), productCategoryId);

		return productCategoryService.updateProductCategoryPicture(productCategoryId, file);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@DeleteMapping("/removepicture/{productCategoryId}")
	public GenericResponse removePicture(@Valid @PathVariable(value = "productCategoryId") Long productCategoryId) {
		return productCategoryService.removeProductCategoryPicture(productCategoryId);
	}
}
