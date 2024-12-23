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
import com.ust.retail.store.pim.dto.catalog.ProductSubcategoryDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcPictureException;
import com.ust.retail.store.pim.model.catalog.ProductSubcategoryModel;
import com.ust.retail.store.pim.repository.catalog.ProductSubcategoryRepository;
import com.ust.retail.store.pim.service.upcmaster.UpcMasterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductSubcategoryService extends BaseService {
	private final ProductSubcategoryRepository productSubcategoryRepository;
	private final UpcMasterService upcMasterService;
	private final PictureHelper pictureHelper;
	private final AuthenticationFacade authenticationFacade;

	public ProductSubcategoryService(ProductSubcategoryRepository productSubcategoryRepository,
									 UpcMasterService upcMasterService,
									 PictureHelper pictureHelper,
									 AuthenticationFacade authenticationFacade) {
		this.productSubcategoryRepository = productSubcategoryRepository;
		this.upcMasterService = upcMasterService;
		this.pictureHelper = pictureHelper;
		this.authenticationFacade = authenticationFacade;
	}

	public ProductSubcategoryDTO findById(long productSubcategoryId) {
		return productSubcategoryRepository.findById(productSubcategoryId)
				.map(m -> new ProductSubcategoryDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("Product subcategory", "id", productSubcategoryId));
	}

	public ProductSubcategoryDTO saveOrUpdate(ProductSubcategoryDTO productSubcategoryDTO) {
		ProductSubcategoryModel model = productSubcategoryDTO.createModel(this.authenticationFacade.getCurrentUserId());

		if (productSubcategoryDTO.getProductSubcategoryId() != null) {
			Optional<ProductSubcategoryModel> optionalModel = productSubcategoryRepository.findById(productSubcategoryDTO.getProductSubcategoryId());
			optionalModel.ifPresent(currentModel -> {
				model.updatePictureUrl(currentModel.getPictureUrl());
				if (!Objects.equals(currentModel.getProductCategory().getProductCategoryId(), model.getProductCategory().getProductCategoryId())) {
					upcMasterService.updateProductHierarchyBySubcategory(
							productSubcategoryDTO.getProductSubcategoryId(),
							productSubcategoryDTO.getProductCategoryId(),
							productSubcategoryDTO.getProductGroupId());
				}
			});
		}

		ProductSubcategoryModel productSubcategoryModel = productSubcategoryRepository.save(model);
		return productSubcategoryDTO.parseToDTO(productSubcategoryModel);
	}

	public Page<ProductSubcategoryDTO> getProductSubcategoriesByFilters(ProductSubcategoryDTO productSubcategoryDTO) {
		return productSubcategoryRepository.findByFilters(
						productSubcategoryDTO.getProductCategoryId(),
						productSubcategoryDTO.getProductSubcategoryName(),
						productSubcategoryDTO.createPageable())
				.map(m -> new ProductSubcategoryDTO().parseToDTO(m));
	}

	public List<ProductSubcategoryDTO> getAutocomplete(Long productCategoryId, @Valid String subcategoryName) {
		return productSubcategoryRepository.getAutocompleteList(productCategoryId, subcategoryName);
	}

	public List<ProductSubcategoryDTO> load(Long productCategoryId) {
		return productSubcategoryRepository.findByProductCategoryProductCategoryId(productCategoryId).stream()
				.map(m -> new ProductSubcategoryDTO().parseToDTO(m))
				.collect(Collectors.toUnmodifiableList());
	}

	public String updateProductSubcategoryPicture(Long productSubcategoryId, MultipartFile file) {
		ProductSubcategoryModel modelToUpdate = productSubcategoryRepository.findById(productSubcategoryId)
				.orElseThrow(() -> getResourceNotFoundException(productSubcategoryId));

		try {

			modelToUpdate.updatePictureUrl(pictureHelper.getStorageHandler().uploadProductSubcategoryPicture(productSubcategoryId, file));

			productSubcategoryRepository.save(modelToUpdate);

			return modelToUpdate.getPictureUrl();
		} catch (IOException e) {
			log.error("Error processing UPC picture", e);
			throw new UpcPictureException(productSubcategoryId, e);
		}
	}

	public GenericResponse removeProductSubcategoryPicture(Long productSubcategoryId) {
		ProductSubcategoryModel modelToUpdate = productSubcategoryRepository.findById(productSubcategoryId)
				.orElseThrow(() -> getResourceNotFoundException(productSubcategoryId));

		pictureHelper.getStorageHandler().deletePicture(modelToUpdate.getPictureUrl());

		modelToUpdate.updatePictureUrl(null);

		productSubcategoryRepository.save(modelToUpdate);

		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE,
				"Picture was removed successfully from Product Subcategory");
	}

	private ResourceNotFoundException getResourceNotFoundException(Long productSubcategoryId) {
		return new ResourceNotFoundException("Product subcategory", "id", productSubcategoryId);
	}
}
