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
import com.ust.retail.store.pim.dto.catalog.ProductSubcategoryDTO;
import com.ust.retail.store.pim.service.catalog.ProductSubcategoryService;

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
class ProductSubcategoryControllerTest {
	private static final String SUBCATEGORY_NAME = "My Subcategory";
	private static final String CATEGORY_NAME = "My Category";
	@Mock
	private ProductSubcategoryService mockProductSubcategoryService;
	@Mock
	private MultipartFile mockMultipartFile;

	@InjectMocks
	private ProductSubcategoryController controller;

	@Test
	void createReturnsExpectedWheInputIsValid() {
		ProductSubcategoryDTO dto = new ProductSubcategoryDTO(null, 1L, 1L, CATEGORY_NAME, SUBCATEGORY_NAME);
		when(mockProductSubcategoryService.saveOrUpdate(any()))
				.then(invocation -> new ProductSubcategoryDTO(1L, 1L, 1L, CATEGORY_NAME, invocation.<ProductSubcategoryDTO>getArgument(0).getProductSubcategoryName()));

		ProductSubcategoryDTO result = controller.create(dto);

		assertThat(result, allOf(
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productCategoryName", equalTo(CATEGORY_NAME)),
				hasProperty("productSubcategoryName", equalTo(SUBCATEGORY_NAME))
		));
	}

	@Test
	void updateReturnsExpectedWhenInputIsValid() {
		ProductSubcategoryDTO dto = new ProductSubcategoryDTO(1L, 1L, 1L, CATEGORY_NAME, SUBCATEGORY_NAME);
		when(mockProductSubcategoryService.saveOrUpdate(any()))
				.then(invocation -> invocation.<ProductSubcategoryDTO>getArgument(0));

		ProductSubcategoryDTO result = controller.update(dto);

		assertThat(result, allOf(
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productCategoryName", equalTo(CATEGORY_NAME)),
				hasProperty("productSubcategoryName", equalTo(SUBCATEGORY_NAME))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenObjectFound() {
		ProductSubcategoryDTO dto = new ProductSubcategoryDTO(1L, 1L, 1L, CATEGORY_NAME, SUBCATEGORY_NAME);
		when(mockProductSubcategoryService.findById(1L)).thenReturn(dto);

		ProductSubcategoryDTO result = controller.findById(1L);

		assertThat(result, allOf(
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productCategoryName", equalTo(CATEGORY_NAME)),
				hasProperty("productSubcategoryName", equalTo(SUBCATEGORY_NAME))
		));
	}

	@Test
	void findByFiltersReturnsExpectedWhenResultsFound() {
		ProductSubcategoryDTO dto1 = new ProductSubcategoryDTO(1L, 1L, 1L, null, SUBCATEGORY_NAME);
		ProductSubcategoryDTO dto6 = new ProductSubcategoryDTO(6L, 1L, 1L, null, "Subcategory 6");
		List<ProductSubcategoryDTO> searchResults = List.of(dto1, dto6);
		ProductSubcategoryDTO dto = new ProductSubcategoryDTO(null, 1L, 1L, null, "Subcategory");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("productSubcategoryName");
		dto.setOrderDir("desc");
		when(mockProductSubcategoryService.getProductSubcategoriesByFilters(dto))
				.thenReturn(new PageImpl<>(searchResults));

		Page<ProductSubcategoryDTO> result = controller.findByFilters(dto);

		assertThat(result.getTotalElements(), is(2L));
		assertThat(result.getContent(), contains(dto1, dto6));
	}

	@Test
	void findAutoCompleteOptionsReturnExpectedWhenResultsFound() {
		ProductSubcategoryDTO dto1 = new ProductSubcategoryDTO(1L, 1L, 1L, null, "My Group 1");
		ProductSubcategoryDTO dto6 = new ProductSubcategoryDTO(6L, 1L, 1L, null, "Group 6");
		List<ProductSubcategoryDTO> searchResults = List.of(dto1, dto6);
		when(mockProductSubcategoryService.getAutocomplete(1L, "Group")).thenReturn(searchResults);

		List<ProductSubcategoryDTO> result = controller.findAutoCompleteOptions(1L, "Group");

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void loadCatalogReturnExpectedWhenResultsFound() {
		ProductSubcategoryDTO dto1 = new ProductSubcategoryDTO(1L, 1L, 1L, null, SUBCATEGORY_NAME);
		ProductSubcategoryDTO dto6 = new ProductSubcategoryDTO(6L, 1L, 1L, null, "Subcategory 6");
		List<ProductSubcategoryDTO> results = List.of(dto1, dto6);
		when(mockProductSubcategoryService.load(1L)).thenReturn(results);

		List<ProductSubcategoryDTO> result = controller.loadCatalog(1L);

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void uploadPictureReturnExpectedWhenResultsFound() {
		String expectedUrl = "http://example.com/picture.png";
		when(mockProductSubcategoryService.updateProductSubcategoryPicture(1L, mockMultipartFile)).thenReturn(expectedUrl);

		String result = controller.uploadPicture(1L, mockMultipartFile);

		assertThat(result, is(expectedUrl));
	}

	@Test
	void removePictureReturnExpectedWhenResultsFound() {

		when(mockProductSubcategoryService.removeProductSubcategoryPicture(1L))
				.thenReturn(new GenericResponse(
						GenericResponse.OP_TYPE_DELETE,
						GenericResponse.SUCCESS_CODE,
						"Picture was removed successfully from Product Subcategory"));

		GenericResponse result = controller.removePicture(1L);

		assertThat(result, hasProperty("operationType", is(GenericResponse.OP_TYPE_DELETE)));
		assertThat(result, hasProperty("code", is(GenericResponse.SUCCESS_CODE)));
		assertThat(result, hasProperty("msg", is("Picture was removed successfully from Product Subcategory")));
	}
}
