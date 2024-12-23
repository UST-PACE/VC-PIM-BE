package com.ust.retail.store.pim.controller.upcmaster;

import com.ust.retail.store.bistro.dto.recipes.BarcodeDTO;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.dto.tax.StoreTaxesDTO;
import com.ust.retail.store.pim.dto.upcmaster.*;
import com.ust.retail.store.pim.service.productmaster.ProductScreenConfigFacade;
import com.ust.retail.store.pim.service.upcmaster.UpcMasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/upcmaster/p")
@Validated
@Slf4j
public class UpcMasterController extends BaseController {

	private final ProductScreenConfigFacade productScreenConfigFacade;
	private final UpcMasterService upcMasterService;
	@Autowired
	public UpcMasterController(UpcMasterService upcMasterService,
			ProductScreenConfigFacade productScreenConfigFacade) {
		
		super();
		this.productScreenConfigFacade = productScreenConfigFacade;
		this.upcMasterService = upcMasterService;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/create")
	@Validated(OnCreate.class)
	public UpcMasterDTO create(@Valid @RequestBody UpcMasterDTO upcMasterDTO) {
		return upcMasterService.saveOrUpdate(upcMasterDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/update")
	@Validated(OnUpdate.class)
	public UpcMasterDTO update(@Valid @RequestBody UpcMasterDTO upcMasterDTO) {
		return upcMasterService.saveOrUpdate(upcMasterDTO);
	}


	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/id/{id}")
	public UpcMasterDTO findById(@PathVariable(value = "id") Long upcMasterId) {
		return upcMasterService.findById(upcMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_Store_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/barcode/{upcMasterId}")
	public BarcodeDTO getRecipeBarcode(@PathVariable Long upcMasterId) {
		return upcMasterService.getRecipeBarcode(upcMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/name/{productName}")
	public List<UpcMasterDTO> findByName(@PathVariable(value = "productName") String productName) {
		return upcMasterService.findByName(productName);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/pi/{productItemId}")
	public List<UpcMasterDTO> findByProductItem(@PathVariable(value = "productItemId") Long productItemId) {
		return upcMasterService.findByProductItem(productItemId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/find/pi/{productItemId}/vm/{vendorMasterId}")
	public List<UpcMasterDTO> findByProductItemAndVendor(
			@PathVariable(value = "productItemId") Long productItemId,
			@PathVariable(value = "vendorMasterId") Long vendorMasterId) {
		return upcMasterService.findByProductItemAndVendor(productItemId, vendorMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PostMapping("/filter")
	@Validated(OnFilter.class)
	public Page<UpcMasterFilterDTO> findByFilters(@Valid @RequestBody UpcMasterFilterDTO dto) {
		return upcMasterService.getProductsByFilters(dto);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load")
	public List<UpcMasterDTO> load() {
		return upcMasterService.load();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/load/finished-goods")
	public List<UpcMasterDTO> loadFinishedGoods() {
		return upcMasterService.loadFinishedGoods();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/screenconfig")
	public ProductScreenConfigDTO screenConfig() {
		return productScreenConfigFacade.getScreenConfig();
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER')")
	@GetMapping("/filter/screenconfig")
	public ProductFilterScreenConfigDTO filterScreenConfig() {
		return productScreenConfigFacade.getFilterScreenConfig();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/status/load")
	public List<CatalogDTO> loadStatusCatalog() {
		return productScreenConfigFacade.getUpcMasterCatalogOptions();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC') ")
	@PostMapping("/uploadpicture")
	public String uploadPicture(@RequestParam("upcMasterId") Long upcMasterId, @RequestParam(required = false) ImageType imageType, @RequestParam("file") MultipartFile file) {
		log.info("Received file: {} for upcMasterId: {} type {}", file.getOriginalFilename(), upcMasterId, imageType);

		return upcMasterService.updateUpcPicture(upcMasterId, Optional.ofNullable(imageType).orElse(ImageType.WEBSITE), file);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@DeleteMapping({"/removepicture/{upcMasterId}", "/removepicture/{upcMasterId}/{imageType}"})
	public GenericResponse removePicture(@Valid @PathVariable Long upcMasterId, @PathVariable(required = false) ImageType imageType) {
		return upcMasterService.removeUpcPicture(upcMasterId, Optional.ofNullable(imageType).orElse(ImageType.WEBSITE));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/storeprices/{upcMasterId}")
	public List<StorePriceDTO> loadStorePrices(@PathVariable Long upcMasterId) {
		return upcMasterService.getStorePrices(upcMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_BISTRO_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@GetMapping("/storetaxes/{upcMasterId}")
	public List<StoreTaxesDTO> loadStoreTaxes(@PathVariable Long upcMasterId) {
		return upcMasterService.getStoreTaxes(upcMasterId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
	@PutMapping("/financialinformation")
	public UpcMasterDTO updateFinancialInformation(@RequestBody UpcMasterDTO upcMasterDTO) {
		return upcMasterService.updateFinancialInformation(upcMasterDTO);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/upcforvc/csv")
	public ResponseEntity<Resource> getUpcForVcCsv() throws IOException {
		return getResponse(upcMasterService.getUpcForVcCsvForExport(), Optional.of(String.format("upc_master_data_%tF", new Date())));
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
