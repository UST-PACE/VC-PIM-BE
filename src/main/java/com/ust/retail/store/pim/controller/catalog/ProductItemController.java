package com.ust.retail.store.pim.controller.catalog;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.ProductItemDTO;
import com.ust.retail.store.pim.service.catalog.ProductItemService;
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
@RequestMapping(path = "/api/catalogs/p/productitem")
@Validated
public class ProductItemController extends BaseController {

	private final ProductItemService productItemService;

	@Autowired
	public ProductItemController(ProductItemService productItemService) {
		super();
		this.productItemService = productItemService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public ProductItemDTO create(@Valid @RequestBody ProductItemDTO productItemDTO) {
		return productItemService.saveOrUpdate(productItemDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public ProductItemDTO update(@Valid @RequestBody ProductItemDTO productItemDTO) {
		return productItemService.saveOrUpdate(productItemDTO);
	}
	

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public ProductItemDTO findById(@PathVariable(value = "id") Long productItemId) {
		return productItemService.findById(productItemId);
	}	 

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<ProductItemDTO> findByFilters(@Valid @RequestBody ProductItemDTO productItemDTO) {
		return productItemService.getProductItemsByFilters(productItemDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/productsubcategory/{productSubcategoryId}/autocomplete/{itemName}")
	public List<ProductItemDTO> findAutoCompleteOptions(@PathVariable(value = "productSubcategoryId") Long productSubcategoryId, @PathVariable(value = "itemName") String itemName) {
		return productItemService.getAutocomplete(productSubcategoryId, itemName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load/ps/{productSubcategoryId}")
	public List<ProductItemDTO> loadCatalog(@PathVariable(value = "productSubcategoryId") Long productSubcategoryId) {
		return productItemService.load(productSubcategoryId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/uploadpicture")
	public String uploadPicture(@RequestParam("productItemId") Long productItemId, @RequestParam("file") MultipartFile file) {
		log.info("Received file: {} for productItemId: {}", file.getOriginalFilename(), productItemId);

		return productItemService.updateProductItemPicture(productItemId, file);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@DeleteMapping("/removepicture/{productItemId}")
	public GenericResponse removePicture(@Valid @PathVariable(value = "productItemId") Long productItemId) {
		return productItemService.removeProductItemPicture(productItemId);
	}
}
