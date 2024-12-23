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
import com.ust.retail.store.pim.dto.catalog.ProductCategoryDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcPictureException;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import com.ust.retail.store.pim.repository.catalog.ProductCategoryRepository;
import com.ust.retail.store.pim.service.upcmaster.UpcMasterService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductCategoryService extends BaseService {
	private final ProductCategoryRepository productCategoryRepository;
	private final UpcMasterService upcMasterService;
	private final PictureHelper pictureHelper;
	private final AuthenticationFacade authenticationFacade;

	public ProductCategoryService(ProductCategoryRepository productCategoryRepository,
								  UpcMasterService upcMasterService,
								  PictureHelper pictureHelper,
								  AuthenticationFacade authenticationFacade) {
		this.productCategoryRepository = productCategoryRepository;
		this.upcMasterService = upcMasterService;
		this.pictureHelper = pictureHelper;
		this.authenticationFacade = authenticationFacade;
	}

	public ProductCategoryDTO findById(long productCategoryId) {
		return productCategoryRepository.findById(productCategoryId)
				.map(m -> new ProductCategoryDTO().parseToDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("Product category", "id", productCategoryId));
	}

	public ProductCategoryDTO saveOrUpdate(ProductCategoryDTO productCategoryDTO) {
		ProductCategoryModel model = productCategoryDTO.createModel(this.authenticationFacade.getCurrentUserId());

		if (productCategoryDTO.getProductCategoryId() != null) {
			Optional<ProductCategoryModel> optionalModel = productCategoryRepository.findById(productCategoryDTO.getProductCategoryId());
			optionalModel.ifPresent(currentModel -> {
				model.updatePictureUrl(currentModel.getPictureUrl());
				if (!Objects.equals(currentModel.getProductGroup().getProductGroupId(), model.getProductGroup().getProductGroupId())) {
					upcMasterService.updateProductHierarchyByCategory(productCategoryDTO.getProductCategoryId(), productCategoryDTO.getProductGroupId());
				}
			});
		}

		ProductCategoryModel productCategoryModel = productCategoryRepository.save(model);
		return productCategoryDTO.parseToDTO(productCategoryModel);
	}

	public Page<ProductCategoryDTO> getProductCategoriesByFilters(ProductCategoryDTO productCategoryDTO) {
		return productCategoryRepository.findByFilters(
						productCategoryDTO.getProductGroupId(),
						productCategoryDTO.getProductCategoryName(),
						productCategoryDTO.createPageable())
				.map(m -> new ProductCategoryDTO().parseToDTO(m));
	}

	public List<ProductCategoryDTO> getAutocomplete(Long productGroupId, @Valid String groupName) {
		return productCategoryRepository.getAutocompleteList(productGroupId, groupName);
	}

	public List<ProductCategoryDTO> load(Long productGroupId) {
		return productCategoryRepository.findByProductGroupProductGroupId(productGroupId).stream()
				.map(m -> new ProductCategoryDTO().parseToDTO(m))
				.collect(Collectors.toUnmodifiableList());
	}

	public String updateProductCategoryPicture(Long productCategoryId, MultipartFile file) {
		ProductCategoryModel modelToUpdate = productCategoryRepository.findById(productCategoryId)
				.orElseThrow(() -> getResourceNotFoundException(productCategoryId));

		try {
			modelToUpdate.updatePictureUrl(pictureHelper.getStorageHandler().uploadProductCategoryPicture(productCategoryId, file));

			productCategoryRepository.save(modelToUpdate);

			return modelToUpdate.getPictureUrl();
		} catch (IOException e) {
			log.error("Error processing Product Category picture", e);
			throw new UpcPictureException(productCategoryId, e);
		}
	}

	public GenericResponse removeProductCategoryPicture(Long productCategoryId) {
		ProductCategoryModel modelToUpdate = productCategoryRepository.findById(productCategoryId)
				.orElseThrow(() -> getResourceNotFoundException(productCategoryId));

		pictureHelper.getStorageHandler().deletePicture(modelToUpdate.getPictureUrl());

		modelToUpdate.updatePictureUrl(null);

		productCategoryRepository.save(modelToUpdate);

		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE,
				"Picture was removed successfully from Product Category");
	}

	private ResourceNotFoundException getResourceNotFoundException(Long productCategoryId) {
		return new ResourceNotFoundException("Product category", "id", productCategoryId);
	}
}
