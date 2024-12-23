package com.ust.retail.store.pim.service.catalog;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ust.retail.store.common.util.PictureHelper;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.bases.BaseService;
import com.ust.retail.store.pim.dto.catalog.ProductItemDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcPictureException;
import com.ust.retail.store.pim.model.catalog.ProductItemModel;
import com.ust.retail.store.pim.repository.catalog.ProductItemRepository;
import com.ust.retail.store.pim.service.upcmaster.UpcMasterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductItemService extends BaseService {
	private final ProductItemRepository productItemRepository;
	private final UpcMasterService upcMasterService;
	private final PictureHelper pictureHelper;
	private final AuthenticationFacade authenticationFacade;

	public ProductItemService(ProductItemRepository productItemRepository,
							  UpcMasterService upcMasterService,
							  PictureHelper pictureHelper,
							  AuthenticationFacade authenticationFacade) {
		this.productItemRepository = productItemRepository;
		this.upcMasterService = upcMasterService;
		this.pictureHelper = pictureHelper;
		this.authenticationFacade = authenticationFacade;
	}

	public ProductItemDTO findById(long productItemId) {
		return productItemRepository.findById(productItemId)
				.map(m -> new ProductItemDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("Product item", "id", productItemId));
	}

	public ProductItemDTO saveOrUpdate(ProductItemDTO productItemDTO) {
		ProductItemModel model = productItemDTO.createModel(this.authenticationFacade.getCurrentUserId());

		if (productItemDTO.getProductItemId() != null) {
			Optional<ProductItemModel> optionalModel = productItemRepository.findById(productItemDTO.getProductItemId());
			optionalModel.ifPresent(currentModel -> {
				model.updatePictureUrl(currentModel.getPictureUrl());
				if (!Objects.equals(currentModel.getProductSubcategory().getProductSubcategoryId(), model.getProductSubcategory().getProductSubcategoryId())) {
					upcMasterService.updateProductHierarchyByItem(
							productItemDTO.getProductItemId(),
							productItemDTO.getProductSubcategoryId(),
							productItemDTO.getProductCategoryId(),
							productItemDTO.getProductGroupId());
				}
			});
		}

		ProductItemModel productItemModel = productItemRepository.save(model);
		return productItemDTO.parseToDTO(productItemModel);
	}

	public Page<ProductItemDTO> getProductItemsByFilters(ProductItemDTO productItemDTO) {
		return productItemRepository.findByFilters(
						productItemDTO.getProductSubcategoryId(),
						productItemDTO.getProductItemName(),
						productItemDTO.createPageable())
				.map(m -> new ProductItemDTO().parseToDTO(m));
	}

	public List<ProductItemDTO> getAutocomplete(Long productCategoryId, @Valid String itemName) {
		return productItemRepository.getAutocompleteList(productCategoryId, itemName);
	}

	public List<ProductItemDTO> load(Long productSubcategoryId) {
		return productItemRepository.findByProductSubcategoryProductSubcategoryId(productSubcategoryId).stream()
				.map(m -> new ProductItemDTO().parseToDTO(m))
				.collect(Collectors.toUnmodifiableList());
	}

	public String updateProductItemPicture(Long productItemId, MultipartFile file) {
		ProductItemModel modelToUpdate = productItemRepository.findById(productItemId)
				.orElseThrow(() -> getResourceNotFoundException(productItemId));

		try {
			modelToUpdate.updatePictureUrl(pictureHelper.getStorageHandler().uploadProductItemPicture(productItemId, file));

			productItemRepository.save(modelToUpdate);

			return modelToUpdate.getPictureUrl();
		} catch (IOException e) {
			log.error("Error processing UPC picture", e);
			throw new UpcPictureException(productItemId, e);
		}
	}

	public GenericResponse removeProductItemPicture(Long productItemId) {
		ProductItemModel modelToUpdate = productItemRepository.findById(productItemId)
				.orElseThrow(() -> getResourceNotFoundException(productItemId));

		pictureHelper.getStorageHandler().deletePicture(modelToUpdate.getPictureUrl());

		modelToUpdate.updatePictureUrl(null);

		productItemRepository.save(modelToUpdate);

		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE,
				"Picture was removed successfully from Product Subcategory");
	}

	private ResourceNotFoundException getResourceNotFoundException(Long productItemId) {
		return new ResourceNotFoundException("Product subcategory", "id", productItemId);
	}
}
