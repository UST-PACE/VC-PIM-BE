package com.ust.retail.store.pim.service.catalog;

import java.io.IOException;
import java.util.List;
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
import com.ust.retail.store.pim.dto.catalog.ProductGroupDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcPictureException;
import com.ust.retail.store.pim.model.catalog.ProductGroupModel;
import com.ust.retail.store.pim.repository.catalog.ProductGroupRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductGroupService extends BaseService {
	private final ProductGroupRepository productGroupRepository;
	private final PictureHelper pictureHelper;
	private final AuthenticationFacade authenticationFacade;

	public ProductGroupService(ProductGroupRepository productGroupRepository,
							   PictureHelper pictureHelper,
							   AuthenticationFacade authenticationFacade) {
		this.productGroupRepository = productGroupRepository;
		this.pictureHelper = pictureHelper;
		this.authenticationFacade = authenticationFacade;
	}

	public ProductGroupDTO findById(long productGroupId) {
		return productGroupRepository.findById(productGroupId)
				.map(m -> new ProductGroupDTO().parseToDTO(m))
				.orElseThrow(() -> getResourceNotFoundException(productGroupId));
	}

	public ProductGroupDTO saveOrUpdate(ProductGroupDTO productGroupDTO) {
		ProductGroupModel model = productGroupDTO.createModel(this.authenticationFacade.getCurrentUserId());

		if (productGroupDTO.getProductGroupId() != null) {
			Optional<ProductGroupModel> optionalModel = productGroupRepository.findById(productGroupDTO.getProductGroupId());
			optionalModel.ifPresent(currentModel -> model.updatePictureUrl(currentModel.getPictureUrl()));
		}

		ProductGroupModel productGroupModel = productGroupRepository.save(model);
		return productGroupDTO.parseToDTO(productGroupModel);
	}

	public Page<ProductGroupDTO> getProductGroupsByFilters(ProductGroupDTO productGroupDTO) {
		return productGroupRepository.findByFilters(productGroupDTO.getProductGroupName(), productGroupDTO.createPageable())
				.map(m -> new ProductGroupDTO().parseToDTO(m));
	}

	public List<ProductGroupDTO> getAutocomplete(@Valid String groupName) {
		return productGroupRepository.getAutocompleteList(groupName);
	}

	public List<ProductGroupDTO> load() {
		return productGroupRepository.findAll().stream()
				.map(m -> new ProductGroupDTO().parseToDTO(m))
				.collect(Collectors.toUnmodifiableList());
	}

	public String updateProductGroupPicture(Long productGroupId, MultipartFile file) {
		ProductGroupModel modelToUpdate = productGroupRepository.findById(productGroupId)
				.orElseThrow(() -> getResourceNotFoundException(productGroupId));

		try {
			modelToUpdate.updatePictureUrl(pictureHelper.getStorageHandler().uploadProductGroupPicture(productGroupId, file));

			productGroupRepository.save(modelToUpdate);

			return modelToUpdate.getPictureUrl();
		} catch (IOException e) {
			log.error("Error processing Product Group picture", e);
			throw new UpcPictureException(productGroupId, e);
		}
	}

	public GenericResponse removeProductGroupPicture(Long productGroupId) {
		ProductGroupModel modelToUpdate = productGroupRepository.findById(productGroupId)
				.orElseThrow(() -> getResourceNotFoundException(productGroupId));

		pictureHelper.getStorageHandler().deletePicture(modelToUpdate.getPictureUrl());

		modelToUpdate.updatePictureUrl(null);

		productGroupRepository.save(modelToUpdate);

		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE,
				"Picture was removed successfully from Product Group");
	}

	private ResourceNotFoundException getResourceNotFoundException(Long productGroupId) {
		return new ResourceNotFoundException("Product group", "id", productGroupId);
	}
}
