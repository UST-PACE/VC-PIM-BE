package com.ust.retail.store.pim.service.upcmaster;

import static com.ust.retail.store.common.util.BarcodeGeneratorUtils.generateCode128BarcodeImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ust.retail.store.bistro.dto.recipes.BarcodeDTO;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.bistro.service.recipes.RecipeService;
import com.ust.retail.store.common.util.PictureHelper;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.catalogs.ProductTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterStatusCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcMasterTypeCatalog;
import com.ust.retail.store.pim.common.catalogs.UpcSellingChannelCatalog;
import com.ust.retail.store.pim.dto.inventory.Item;
import com.ust.retail.store.pim.dto.tax.StoreTaxesDTO;
import com.ust.retail.store.pim.dto.upcmaster.ApplicableTaxDTO;
import com.ust.retail.store.pim.dto.upcmaster.ImageType;
import com.ust.retail.store.pim.dto.upcmaster.StorePriceDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterFilterDTO;
import com.ust.retail.store.pim.engine.inventory.ReceiveInventory;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcMasterInUseException;
import com.ust.retail.store.pim.exceptions.UpcMasterInUseException.ChangedField;
import com.ust.retail.store.pim.exceptions.UpcMasterInUseException.Usage;
import com.ust.retail.store.pim.exceptions.UpcPictureException;
import com.ust.retail.store.pim.model.catalog.StoreLocationModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterSellingChannelModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorStoreCostModel;
import com.ust.retail.store.pim.repository.menuconfigurator.MenuConfiguratorProductRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcStorePriceRepository;
import com.ust.retail.store.pim.service.catalog.StoreNumberService;
import com.ust.retail.store.pim.service.connector.esl.EslService;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.service.purchaseorder.PurchaseOrderFacade;
import com.ust.retail.store.pim.service.tax.TaxService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UpcMasterService {
	private final UpcMasterRepository upcMasterRepository;
	private final UpcStorePriceRepository upcStorePriceRepository;
	private final StoreNumberService storeNumberService;
	private final PictureHelper pictureHelper;
	private final RecipeService recipeService;
	private final RecipeRepository recipeRepository;
	private final InventoryService inventoryService;
	private final UpcVendorDetailsService upcVendorDetailsService;
	private final TaxService taxService;
	private final EslService eslService;
	private final AuthenticationFacade authenticationFacade;
	private final PurchaseOrderFacade purchaseOrderFacade;
	private final ReceiveInventory receiveInventory;
	private final MenuConfiguratorProductRepository menuConfiguratorProductRepository;
	
	@Value("${pim.autoincrease.inventory}")
	private Boolean IS_AUTO_INCREASE_INVENTORY;
	
	@Value("${pim.autoincrease.inventory.qty}")
	private Double AUTO_INCREASE_QTY;
	
	public UpcMasterService(UpcMasterRepository upcMasterRepository,
							UpcStorePriceRepository upcStorePriceRepository,
							StoreNumberService storeNumberService,
							PictureHelper pictureHelper,
							RecipeService recipeService,
							RecipeRepository recipeRepository,
							InventoryService inventoryService,
							UpcVendorDetailsService upcVendorDetailsService,
							TaxService taxService,
							EslService eslService,
							AuthenticationFacade authenticationFacade,
							PurchaseOrderFacade purchaseOrderFacade,
							ReceiveInventory receiveInventory,
							MenuConfiguratorProductRepository menuConfiguratorProductRepository) {
		this.upcMasterRepository = upcMasterRepository;
		this.upcStorePriceRepository = upcStorePriceRepository;
		this.storeNumberService = storeNumberService;
		this.pictureHelper = pictureHelper;
		this.recipeService = recipeService;
		this.recipeRepository = recipeRepository;
		this.inventoryService = inventoryService;
		this.upcVendorDetailsService = upcVendorDetailsService;
		this.taxService = taxService;
		this.eslService = eslService;
		this.authenticationFacade = authenticationFacade;
		this.purchaseOrderFacade = purchaseOrderFacade;
		this.receiveInventory = receiveInventory;
		this.menuConfiguratorProductRepository = menuConfiguratorProductRepository;
	}

	public UpcMasterDTO saveOrUpdate(UpcMasterDTO upcMasterDTO) {
			UpcMasterModel model = upcMasterDTO.createModel(this.authenticationFacade.getCurrentUserId());
		Long upcMasterId = upcMasterDTO.getUpcMasterId();

		if (upcMasterId != null) {
			Optional<UpcMasterModel> optCurrentModel = upcMasterRepository.findById(upcMasterId);
			if (optCurrentModel.isPresent()) {
				UpcMasterModel currentModel = optCurrentModel.get();
				validateRecipeUsage(upcMasterDTO, currentModel);
				validateInventoryExistence(upcMasterDTO, currentModel);

				currentModel.updateInformation(upcMasterDTO);
				model = upcMasterRepository.save(currentModel);
				updateMenuConfiguratorStatusInfo(model);
			}
		} else {
			model.updateUpcMasterStatus(UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE);
			model = upcMasterRepository.save(model);
			
			if(IS_AUTO_INCREASE_INVENTORY 
					&& Objects.equals(model.getUpcMasterType().getCatalogId(),UpcMasterTypeCatalog.PIM_TYPE)
					&& Objects.equals(model.getProductType().getCatalogId(),ProductTypeCatalog.PRODUCT_TYPE_FG)) {
				
				initUPCInventoryToDefaultQty(model.getUpcMasterId());
			}
		}

		if (upcMasterId == null) {
			model.updateSku();
			model = upcMasterRepository.save(model);
		}
		eslService.sendUpc(model);
		return upcMasterDTO.parseToDTO(model);
	}
	
	private void initUPCInventoryToDefaultQty(Long upcMasterId) {
		Item currentNewUpc = new Item(AUTO_INCREASE_QTY,upcMasterId,StoreLocationModel.DEFAULT_STORE_LOCATION);
		List<Item> items = new ArrayList<>();
		items.add(currentNewUpc);
		
		receiveInventory.execute(items, ReceiveInventory.NO_SHRIKAGE_LIST, ReceiveInventory.DEFAULT_REFERENCE_ID);
	}

	private void validateRecipeUsage(UpcMasterDTO upcMasterDTO, UpcMasterModel model) {
		if (recipeService.isUpcUsedOnRecipes(upcMasterDTO.getUpcMasterId())) {
			if (!Objects.equals(model.getProductType().getCatalogId(), upcMasterDTO.getProductTypeId())) {
				throw new UpcMasterInUseException(ChangedField.PRODUCT_TYPE, Usage.RECIPE);
			}
			if (!Objects.equals(model.getContentPerUnit(), upcMasterDTO.getContentPerUnit())) {
				throw new UpcMasterInUseException(ChangedField.CONTENT_PER_UNIT, Usage.RECIPE);
			}
			if (!Objects.equals(model.getContentPerUnitUom().getCatalogId(), upcMasterDTO.getContentPerUnitUomId())) {
				throw new UpcMasterInUseException(ChangedField.CONTENT_PER_UNIT_UOM, Usage.RECIPE);
			}
			if (!Objects.equals(model.getInventoryUnit().getCatalogId(), upcMasterDTO.getInventoryUnitId())) {
				throw new UpcMasterInUseException(ChangedField.INVENTORY_UNIT, Usage.RECIPE);
			}
		}
	}

	private void validateInventoryExistence(UpcMasterDTO upcMasterDTO, UpcMasterModel model) {
		if (inventoryService.upcHasInventory(upcMasterDTO.getUpcMasterId())) {
			if (!Objects.equals(model.getProductType().getCatalogId(), upcMasterDTO.getProductTypeId())) {
				throw new UpcMasterInUseException(ChangedField.PRODUCT_TYPE, Usage.INVENTORY);
			}
			if (!Objects.equals(model.getContentPerUnit(), upcMasterDTO.getContentPerUnit())) {
				throw new UpcMasterInUseException(ChangedField.CONTENT_PER_UNIT, Usage.INVENTORY);
			}
			if (!Objects.equals(model.getContentPerUnitUom().getCatalogId(), upcMasterDTO.getContentPerUnitUomId())) {
				throw new UpcMasterInUseException(ChangedField.CONTENT_PER_UNIT_UOM, Usage.INVENTORY);
			}
			if (!Objects.equals(model.getInventoryUnit().getCatalogId(), upcMasterDTO.getInventoryUnitId())) {
				throw new UpcMasterInUseException(ChangedField.INVENTORY_UNIT, Usage.INVENTORY);
			}
		}
	}

	public UpcMasterDTO updateFinancialInformation(UpcMasterDTO upcMasterDTO) {
		Long upcMasterId = upcMasterDTO.getUpcMasterId();
		UpcMasterModel modelToUpdate = upcMasterRepository.findById(upcMasterId)
				.orElseThrow(() -> getResourceNotFoundException(upcMasterId));
		
		if (upcMasterDTO.getUpcMasterStatusId() != null && !Objects.equals(upcMasterDTO.getUpcMasterStatusId(),
				UpcMasterStatusCatalog.UPC_MASTER_STATUS_ACTIVE)) {
			purchaseOrderFacade.removeUpcFromPurchaseOrders(upcMasterId);
		}

		if (Objects.equals(upcMasterDTO.getUpcMasterStatusId(),
				UpcMasterStatusCatalog.UPC_MASTER_STATUS_DISCONTINUE_TRADING)) {
			inventoryService.depleteUpcInventory(upcMasterId);
			removeMenuConfiguredProduct(upcMasterDTO);
		}
 
		modelToUpdate.updateFinancialInformation(upcMasterDTO);
		modelToUpdate.setTaxePrecentage(upcMasterDTO.getTaxPercentage());
		modelToUpdate.setUpcDiscount(upcMasterDTO.getUpcDiscount());
		modelToUpdate.setTaxePrecentageActive(upcMasterDTO.getUpcTaxPercentageActive());		
		return new UpcMasterDTO().parseToDTO(upcMasterRepository.save(modelToUpdate));
	}

	public UpcMasterDTO findById(Long upcMasterId) {
		return upcMasterRepository.findById(upcMasterId).map(m -> new UpcMasterDTO().parseToDTO(m))
				.orElseThrow(() -> getResourceNotFoundException(upcMasterId));
	}
 
	public Page<UpcMasterFilterDTO> getProductsByFilters(UpcMasterFilterDTO filterDTO) {
		return upcMasterRepository.findByFilters(
				filterDTO.getPrincipalUpc(),
				filterDTO.getProductName(),
				filterDTO.getProductTypeId(),
				filterDTO.getBrandOwnerId(),
				UpcMasterTypeCatalog.PIM_TYPE,
				filterDTO.isVcItem(),
				filterDTO.getCoreName(),
				filterDTO.getRecipeNumber(),
				filterDTO.getUpcMasterStatusId(),
				filterDTO.getUpcProductTypeId(),
				filterDTO.getProductCategoryId(),
				filterDTO.createPageable());
	}

	public List<UpcMasterDTO> findByName(String productName) {
		return upcMasterRepository.findByProductNameContaining(productName).stream()
				.map(p -> new UpcMasterDTO().parseToDTO(p))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<UpcMasterDTO> load() {
		return upcMasterRepository.findAll().stream()
				.map(p -> new UpcMasterDTO().parseToDTO(p))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<UpcMasterDTO> findByProductItem(Long productItemId) {
		return upcMasterRepository.findByProductItemProductItemId(productItemId).stream()
				.map(p -> new UpcMasterDTO().parseToDTO(p))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<UpcMasterDTO> findByProductItemAndVendor(Long productItemId, Long vendorMasterId) {
		return upcMasterRepository.findByItemAndVendor(productItemId, vendorMasterId).stream()
				.map(p -> new UpcMasterDTO().parseToDTO(p))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<UpcMasterDTO> loadFinishedGoods() {
		return findByProductType(ProductTypeCatalog.PRODUCT_TYPE_FG);
	}

	public List<UpcMasterDTO> findByProductType(Long productTypeId) {
		return  upcMasterRepository.findByProductTypeCatalogId(productTypeId).stream()
				.map(p -> new UpcMasterDTO().parseToDTO(p))
				.collect(Collectors.toUnmodifiableList());
	}

	public String updateUpcPicture(Long upcMasterId, ImageType imageType, MultipartFile file) {
		UpcMasterModel modelToUpdate = upcMasterRepository.findById(upcMasterId)
				.orElseThrow(() -> getResourceNotFoundException(upcMasterId));

		try {
			String pictureUrl = pictureHelper.getStorageHandler().uploadProductPicture(upcMasterId, imageType, file);
			switch (imageType) {
				case MENU_BOARD:
					modelToUpdate.updateMenuBoardImageUrl(pictureUrl);
					break;
				case KIOSK:
					modelToUpdate.updateKioskImageUrl(pictureUrl);
					break;
				case APP:
					modelToUpdate.updateAppImageUrl(pictureUrl);
					break;
				case PRODUCT_IMAGE_1:
					modelToUpdate.updateProductImage1(pictureUrl);
					break;
				case PRODUCT_IMAGE_2:
					modelToUpdate.updateProductImage2(pictureUrl);
					break;
				case PRODUCT_IMAGE_3:
					modelToUpdate.updateProductImage3(pictureUrl);
					break;
				case PRODUCT_IMAGE_4:
					modelToUpdate.updateProductImage4(pictureUrl);
					break;
				case WEBSITE:
				default:
					modelToUpdate.updateWebsiteImage(pictureUrl);
			}

			upcMasterRepository.save(modelToUpdate);

			return pictureUrl;
		} catch (IOException e) {
			log.error("Error processing UPC picture", e);
			throw new UpcPictureException(upcMasterId, e);
		}
	}

	public GenericResponse removeUpcPicture(Long upcMasterId, ImageType imageType) {
		UpcMasterModel modelToUpdate = upcMasterRepository.findById(upcMasterId)
				.orElseThrow(() -> getResourceNotFoundException(upcMasterId));

		String pictureUrl;
		switch (imageType) {
			case MENU_BOARD:
				pictureUrl = modelToUpdate.getMenuBoardImageUrl();
				modelToUpdate.updateMenuBoardImageUrl(null);
				break;
			case KIOSK:
				pictureUrl = modelToUpdate.getKioskImageUrl();
				modelToUpdate.updateKioskImageUrl(null);
				break;
			case APP:
				pictureUrl = modelToUpdate.getAppImageUrl();
				modelToUpdate.updateAppImageUrl(null);
				break;
			case PRODUCT_IMAGE_1:
				pictureUrl = modelToUpdate.getProductImage1();
				modelToUpdate.updateProductImage1(null);
				break;
			case PRODUCT_IMAGE_2:
				pictureUrl = modelToUpdate.getProductImage2();
				modelToUpdate.updateProductImage2(null);
				break;
			case PRODUCT_IMAGE_3:
				pictureUrl = modelToUpdate.getProductImage3();
				modelToUpdate.updateProductImage3(null);
				break;
			case PRODUCT_IMAGE_4:
				pictureUrl = modelToUpdate.getProductImage4();
				modelToUpdate.updateProductImage4(null);
				break;
			case WEBSITE:
			default:
				pictureUrl = modelToUpdate.getWebsiteImageUrl();
				modelToUpdate.updateWebsiteImage(null);
		}

		pictureHelper.getStorageHandler().deletePicture(pictureUrl);

		upcMasterRepository.save(modelToUpdate);

		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE,
				"Picture was removed successfully from UPC Master");
	}

	public BarcodeDTO getRecipeBarcode(Long upcMasterId) {
		UpcMasterModel upcMasterModel = upcMasterRepository.findById(upcMasterId)
				.orElseThrow(() -> getResourceNotFoundException(upcMasterId));

		return new BarcodeDTO(generateCode128BarcodeImage(upcMasterModel.getPrincipalUpc()));
	}

	@Transactional
	public void updateProductHierarchyByCategory(Long productCategoryId, Long productGroupId) {
		upcMasterRepository.updateProductHierarchyByProductCategory(productCategoryId, productGroupId);
	}

	@Transactional
	public void updateProductHierarchyBySubcategory(Long productSubcategoryId, Long productCategoryId, Long productGroupId) {
		upcMasterRepository.updateProductHierarchyByProductSubcategory(productSubcategoryId, productCategoryId, productGroupId);
	}

	@Transactional
	public void updateProductHierarchyByItem(Long productItemId, Long productSubcategoryId, Long productCategoryId, Long productGroupId) {
		upcMasterRepository.updateProductHierarchyByProductItem(productItemId, productSubcategoryId, productCategoryId, productGroupId);
	}

	private ResourceNotFoundException getResourceNotFoundException(Long upcMasterId) {
		return new ResourceNotFoundException("Upc Master", "id", upcMasterId);
	}

	public Double getCostPerUnit(UpcMasterDTO upcMaster) {
		Double costPerUnit;
		if (recipeService.isTopping(upcMaster.getUpcMasterId())) {
			costPerUnit = recipeRepository.findByRelatedUpcMasterUpcMasterId(upcMaster.getUpcMasterId())
					.map(recipe -> recipe.getAverageSalePrice() / upcMaster.getContentPerUnit())
					.orElse(null);
		} else {
			costPerUnit = upcVendorDetailsService.findDefaultVendorDetailsFor(upcMaster.getUpcMasterId())
					.map(vendorDetails -> vendorDetails.getAverageProductCost() / upcMaster.getContentPerUnit())
					.orElse(null);
		}
		return costPerUnit;
	}

	public List<StorePriceDTO> getStorePrices(Long upcMasterId) {
		Map<Long, List<UpcVendorStoreCostModel>> costsByStore = upcVendorDetailsService.findVendorDetailsFor(upcMasterId).stream()
				.flatMap(vendorDetail -> vendorDetail.getStoreCosts().stream())
				.collect(Collectors.groupingBy(upcVendorStoreCostModel -> upcVendorStoreCostModel.getStoreNumber().getStoreNumId()));


		List<StorePriceDTO> currentStorePricesStream = upcStorePriceRepository.findByUpcMasterUpcMasterId(upcMasterId).stream()
				.map(sp -> new StorePriceDTO().parseToDTO(sp))
				.collect(Collectors.toUnmodifiableList());

		return storeNumberService.load().stream()
				.map(st -> {
					UpcVendorStoreCostModel highest = costsByStore.getOrDefault(st.getStoreNumId(), List.of()).stream()
							.max(Comparator.comparingDouble(UpcVendorStoreCostModel::getCost))
							.orElse(null);

					UpcVendorStoreCostModel lowest = costsByStore.getOrDefault(st.getStoreNumId(), List.of()).stream()
							.min(Comparator.comparingDouble(UpcVendorStoreCostModel::getCost))
							.orElse(null);

					StorePriceDTO storePriceDTO = new StorePriceDTO().setStoreInfo(st)
							.setLowestStoreCost(lowest)
							.setHighestStoreCost(highest);

					currentStorePricesStream.stream().filter(sp -> sp.equals(storePriceDTO))
							.findFirst()
							.ifPresent(storePriceDTO::merge);

					return storePriceDTO;
				})
				.sorted(Comparator.comparingLong(StorePriceDTO::getStoreNumId))
				.collect(Collectors.toUnmodifiableList());
	}

	public List<StoreTaxesDTO> getStoreTaxes(Long upcMasterId) {
		return storeNumberService.load().stream()
				.map(st -> new StoreTaxesDTO(
						st,
						taxService.findApplicableTaxesForProductAtStore(upcMasterId, st.getStoreNumId()).stream()
								.map(tax -> new ApplicableTaxDTO(
										tax.getTaxTypeName(),
										Stream.of(tax.getProductGroupName(), tax.getProductCategoryName(), tax.getProductSubcategoryName())
												.filter(Objects::nonNull)
												.collect(Collectors.joining("/")),
										tax.getPercentage()))
								.sorted(Comparator.comparing(ApplicableTaxDTO::getType)
										.thenComparing(ApplicableTaxDTO::getCategory))
								.collect(Collectors.toUnmodifiableList())))
				.filter(dto -> !dto.getApplicableTaxes().isEmpty())
				.sorted(Comparator.comparingLong(StoreTaxesDTO::getStoreNumId))
				.collect(Collectors.toUnmodifiableList());
	}

	public FileSystemResource getUpcForVcCsvForExport() {
		StringBuilder contentBuilder = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		LocalDateTime now= LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String currentDate = now.format(formatter);

		contentBuilder.append(String.format("Report Generation Date: ,%s",
						StringUtils.wrap(StringUtils.prependIfMissing(currentDate, "'"), '"')))
				.append(System.lineSeparator())
				.append(System.lineSeparator());


		String[] headers = {
				"UPC Master Id",
				"UPC",
				"Recipie Num",
				"Product name",
				"Served With",
				"Product Type",
				"Package Color",
				"Core Name",
				"Portion Per Serving",
				"Entry Type",
				"Calories",
				"Kcalories",
				"Colored Flag",
				"Category",
				"Sub Category",
				"Creation Date",
				"Last Update Date"};
		String header = Arrays.stream(headers)
				.map(s -> StringUtils.wrap(s, '"'))
				.collect(Collectors.joining(","));


		contentBuilder.append(header)
				.append(System.lineSeparator());

		String results = getUpcForVcCsvData().stream()
				.map(upcRecord -> String.format(StringUtils.repeat("%s", ",", headers.length),
						StringUtils.wrap(numberToString(upcRecord.getUpcMasterId()), '"'),
						StringUtils.wrap(StringUtils.prependIfMissing(upcRecord.getPrincipalUPC(), "'"), '"'),
						StringUtils.wrap(Optional.ofNullable(numberToString(upcRecord.getRecipeNumber())).orElse("N/A"), '"'),
						StringUtils.wrap(upcRecord.getProductName(), '"'),
						StringUtils.wrap(Optional.ofNullable(upcRecord.getServedWith()).orElse("N/A"), '"'),
						StringUtils.wrap(upcRecord.getUpcProductTypeName(), '"'),
						StringUtils.wrap(Optional.ofNullable(upcRecord.getPackageColor()).orElse("N/A"), '"'),
						StringUtils.wrap(Optional.ofNullable(upcRecord.getCoreName()).orElse("N/A"), '"'),
						StringUtils.wrap(numberToString(upcRecord.getPortionQty()), '"'),
						StringUtils.wrap(upcRecord.getMainEntryTypeName(), '"'),
						StringUtils.wrap(Optional.ofNullable(numberToString(upcRecord.getNutritionalInformation().getCalories())).orElse("0"), '"'),
						StringUtils.wrap(Optional.ofNullable(numberToString(upcRecord.getNutritionalInformation().getKiloCalories())).orElse("0"), '"'),
						StringUtils.wrap(Optional.ofNullable(upcRecord.getNutritionalInformation().getFlagToDisplay()).orElse("N/A"), '"'),
						StringUtils.wrap(upcRecord.getProductCategoryName(), '"'),
						StringUtils.wrap(upcRecord.getProductSubcategoryName(), '"'),
						StringUtils.wrap(StringUtils.prependIfMissing(sdf.format(upcRecord.getCreatedAt()), "'"), '"'),
						StringUtils.wrap(StringUtils.prependIfMissing(sdf.format(upcRecord.getUpdateAt()), "'"), '"')

				))
				.collect(Collectors.joining(System.lineSeparator()));

		contentBuilder.append(results);

		String fileName = String.format("upc_master_data_%tF.csv", new Date());
		return new FileSystemResource(getWrittenFilePath(fileName, contentBuilder).toFile());
	}


	public List<UpcMasterDTO> getUpcForVcCsvData() {
		return upcMasterRepository.getUpcForVcCsvData().stream()
				.map(m -> new UpcMasterDTO().parseToDTO(m))
				.collect(Collectors.toList());
	}



	private void removeMenuConfiguredProduct(UpcMasterDTO upcMasterDTO) {
		if (upcMasterDTO.getUpcMasterStatusId().equals(UpcMasterStatusCatalog.UPC_MASTER_STATUS_DISCONTINUE_TRADING)) {
			menuConfiguratorProductRepository.deleteByMenuConfiguratorProductId(upcMasterDTO.getUpcMasterId());
		}
	}
	
	private void updateMenuConfiguratorStatusInfo(UpcMasterModel currentModel) {
		UpcMasterSellingChannelModel UpcSellingChannel = currentModel.getUpcSellingChannels().stream()
				.filter(sc -> sc.getChannel().getCatalogId().equals(UpcSellingChannelCatalog.VC_UPC_SELLING_CHANNEL)).findFirst().get();
		if (UpcSellingChannel != null) {
			menuConfiguratorProductRepository.updateMenuConfiguratorVcStatus(UpcSellingChannel.isEnabled(), currentModel.getUpcMasterId());
		}
	}

	private Path getWrittenFilePath(String fileName, StringBuilder contentBuilder) {
		Path path = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
		try {
			Files.writeString(path, contentBuilder.toString());
		} catch (IOException e) {
			log.error("Error while writing file", e);
		}
		return path;
	}

	private String numberToString(Number number) {
		return Objects.isNull(number)? null: String.valueOf(number);
	}
	
}
