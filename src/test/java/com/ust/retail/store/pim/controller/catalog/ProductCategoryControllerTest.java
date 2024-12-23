package com.ust.retail.store.pim.controller.catalog;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.multipart.MultipartFile;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.catalog.ProductCategoryDTO;
import com.ust.retail.store.pim.service.catalog.ProductCategoryService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCategoryControllerTest {
	private static final String CATEGORY_NAME = "My Category";
	private static final String GROUP_NAME = "My Group";
	@Mock
	private ProductCategoryService mockProductCategoryService;
	@Mock
	private MultipartFile mockMultipartFile;

	@InjectMocks
	private ProductCategoryController controller;

	@Test
	void createReturnsExpectedWheInputIsValid() {
		ProductCategoryDTO dto = new ProductCategoryDTO(null, 1L, GROUP_NAME, CATEGORY_NAME);
		when(mockProductCategoryService.saveOrUpdate(any()))
				.then(invocation -> new ProductCategoryDTO(1L, 1L, GROUP_NAME, invocation.<ProductCategoryDTO>getArgument(0).getProductCategoryName()));

		ProductCategoryDTO result = controller.create(dto);

		assertThat(result, allOf(
				hasProperty("productGroupId", equalTo(1L)),
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productGroupName", equalTo(GROUP_NAME)),
				hasProperty("productCategoryName", equalTo(CATEGORY_NAME))
		));
	}

	@Test
	void updateReturnsExpectedWhenInputIsValid() {
		ProductCategoryDTO dto = new ProductCategoryDTO(1L, 1L, GROUP_NAME, CATEGORY_NAME);
		when(mockProductCategoryService.saveOrUpdate(any()))
				.then(invocation -> invocation.<ProductCategoryDTO>getArgument(0));

		ProductCategoryDTO result = controller.update(dto);

		assertThat(result, allOf(
				hasProperty("productGroupId", equalTo(1L)),
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productGroupName", equalTo(GROUP_NAME)),
				hasProperty("productCategoryName", equalTo(CATEGORY_NAME))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenObjectFound() {
		ProductCategoryDTO dto = new ProductCategoryDTO(1L, 1L, GROUP_NAME, CATEGORY_NAME);
		when(mockProductCategoryService.findById(1L)).thenReturn(dto);

		ProductCategoryDTO result = controller.findById(1L);

		assertThat(result, allOf(
				hasProperty("productGroupId", equalTo(1L)),
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productGroupName", equalTo(GROUP_NAME)),
				hasProperty("productCategoryName", equalTo(CATEGORY_NAME))
		));
	}

	@Test
	void findByFiltersReturnsExpectedWhenResultsFound() {
		ProductCategoryDTO dto1 = new ProductCategoryDTO(1L, 1L, null, CATEGORY_NAME);
		ProductCategoryDTO dto6 = new ProductCategoryDTO(6L, 1L, null, "Category 6");
		List<ProductCategoryDTO> searchResults = List.of(dto1, dto6);
		ProductCategoryDTO dto = new ProductCategoryDTO(null, 1L, null, "Category");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("productCategoryName");
		dto.setOrderDir("desc");
		when(mockProductCategoryService.getProductCategoriesByFilters(dto))
				.thenReturn(new PageImpl<>(searchResults));

		Page<ProductCategoryDTO> result = controller.findByFilters(dto);

		assertThat(result.getTotalElements(), is(2L));
		assertThat(result.getContent(), contains(dto1, dto6));
	}

	@Test
	void findAutoCompleteOptionsReturnExpectedWhenResultsFound() {
		ProductCategoryDTO dto1 = new ProductCategoryDTO(1L, 1L, null, CATEGORY_NAME);
		ProductCategoryDTO dto6 = new ProductCategoryDTO(6L, 1L, null, "Category 6");
		List<ProductCategoryDTO> searchResults = List.of(dto1, dto6);
		when(mockProductCategoryService.getAutocomplete(1L, "Category")).thenReturn(searchResults);

		List<ProductCategoryDTO> result = controller.findAutoCompleteOptions(1L, "Category");

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void loadCatalogReturnExpectedWhenResultsFound() {
		ProductCategoryDTO dto1 = new ProductCategoryDTO(1L, 1L, null, CATEGORY_NAME);
		ProductCategoryDTO dto6 = new ProductCategoryDTO(6L, 1L, null, "Category 6");
		List<ProductCategoryDTO> results = List.of(dto1, dto6);
		when(mockProductCategoryService.load(1L)).thenReturn(results);

		List<ProductCategoryDTO> result = controller.loadCatalog(1L);

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void uploadPictureReturnExpectedWhenResultsFound() {
		String expectedUrl = "http://example.com/picture.png";
		when(mockProductCategoryService.updateProductCategoryPicture(1L, mockMultipartFile)).thenReturn(expectedUrl);

		String result = controller.uploadPicture(1L, mockMultipartFile);

		assertThat(result, is(expectedUrl));
	}

	@Test
	void removePictureReturnExpectedWhenResultsFound() {

		when(mockProductCategoryService.removeProductCategoryPicture(1L))
				.thenReturn(new GenericResponse(
						GenericResponse.OP_TYPE_DELETE,
						GenericResponse.SUCCESS_CODE,
						"Picture was removed successfully from Product Category"));

		GenericResponse result = controller.removePicture(1L);

		assertThat(result, hasProperty("operationType", is(GenericResponse.OP_TYPE_DELETE)));
		assertThat(result, hasProperty("code", is(GenericResponse.SUCCESS_CODE)));
		assertThat(result, hasProperty("msg", is("Picture was removed successfully from Product Category")));
	}
}
