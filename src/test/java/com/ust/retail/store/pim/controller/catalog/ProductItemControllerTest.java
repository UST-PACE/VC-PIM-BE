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
import com.ust.retail.store.pim.dto.catalog.ProductItemDTO;
import com.ust.retail.store.pim.service.catalog.ProductItemService;

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
class ProductItemControllerTest {
	private static final String ITEM_NAME = "My Item";
	private static final String SUBCATEGORY_NAME = "My Subcategory";
	@Mock
	private ProductItemService mockProductItemService;
	@Mock
	private MultipartFile mockMultipartFile;

	@InjectMocks
	private ProductItemController controller;

	@Test
	void createReturnsExpectedWheInputIsValid() {
		ProductItemDTO dto = new ProductItemDTO(null, 1L, 1L, 1L, SUBCATEGORY_NAME, ITEM_NAME);
		when(mockProductItemService.saveOrUpdate(any()))
				.then(invocation -> new ProductItemDTO(1L, 1L, 1L, 1L, SUBCATEGORY_NAME, invocation.<ProductItemDTO>getArgument(0).getProductItemName()));

		ProductItemDTO result = controller.create(dto);

		assertThat(result, allOf(
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productItemId", equalTo(1L)),
				hasProperty("productSubcategoryName", equalTo(SUBCATEGORY_NAME)),
				hasProperty("productItemName", equalTo(ITEM_NAME))
		));
	}

	@Test
	void updateReturnsExpectedWhenInputIsValid() {
		ProductItemDTO dto = new ProductItemDTO(1L, 1L, 1L, 1L, SUBCATEGORY_NAME, ITEM_NAME);
		when(mockProductItemService.saveOrUpdate(any()))
				.then(invocation -> invocation.<ProductItemDTO>getArgument(0));

		ProductItemDTO result = controller.update(dto);

		assertThat(result, allOf(
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productItemId", equalTo(1L)),
				hasProperty("productSubcategoryName", equalTo(SUBCATEGORY_NAME)),
				hasProperty("productItemName", equalTo(ITEM_NAME))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenObjectFound() {
		ProductItemDTO dto = new ProductItemDTO(1L, 1L, 1L, 1L, SUBCATEGORY_NAME, ITEM_NAME);
		when(mockProductItemService.findById(1L)).thenReturn(dto);

		ProductItemDTO result = controller.findById(1L);

		assertThat(result, allOf(
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productItemId", equalTo(1L)),
				hasProperty("productSubcategoryName", equalTo(SUBCATEGORY_NAME)),
				hasProperty("productItemName", equalTo(ITEM_NAME))
		));
	}

	@Test
	void findByFiltersReturnsExpectedWhenResultsFound() {
		ProductItemDTO dto1 = new ProductItemDTO(1L, 1L, 1L, 1L, SUBCATEGORY_NAME, ITEM_NAME);
		ProductItemDTO dto6 = new ProductItemDTO(6L, 1L, 1L, 1L, SUBCATEGORY_NAME, "Item 6");
		List<ProductItemDTO> searchResults = List.of(dto1, dto6);
		ProductItemDTO dto = new ProductItemDTO(null, 1L, 1L, 1L, SUBCATEGORY_NAME, "Item");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("productItemName");
		dto.setOrderDir("desc");
		when(mockProductItemService.getProductItemsByFilters(dto))
				.thenReturn(new PageImpl<>(searchResults));

		Page<ProductItemDTO> result = controller.findByFilters(dto);

		assertThat(result.getTotalElements(), is(2L));
		assertThat(result.getContent(), contains(dto1, dto6));
	}

	@Test
	void findAutoCompleteOptionsReturnExpectedWhenResultsFound() {
		ProductItemDTO dto1 = new ProductItemDTO(1L, 1L, 1L, 1L, SUBCATEGORY_NAME, ITEM_NAME);
		ProductItemDTO dto6 = new ProductItemDTO(6L, 1L, 1L, 1L, SUBCATEGORY_NAME, "Item 6");
		List<ProductItemDTO> searchResults = List.of(dto1, dto6);
		when(mockProductItemService.getAutocomplete(1L, "Item")).thenReturn(searchResults);

		List<ProductItemDTO> result = controller.findAutoCompleteOptions(1L, "Item");

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void loadCatalogReturnExpectedWhenResultsFound() {
		ProductItemDTO dto1 = new ProductItemDTO(1L, 1L, 1L, 1L, null, ITEM_NAME);
		ProductItemDTO dto6 = new ProductItemDTO(6L, 1L, 1L, 1L, null, "Item 6");
		List<ProductItemDTO> results = List.of(dto1, dto6);
		when(mockProductItemService.load(1L)).thenReturn(results);

		List<ProductItemDTO> result = controller.loadCatalog(1L);

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void uploadPictureReturnExpectedWhenResultsFound() {
		String expectedUrl = "http://example.com/picture.png";
		when(mockProductItemService.updateProductItemPicture(1L, mockMultipartFile)).thenReturn(expectedUrl);

		String result = controller.uploadPicture(1L, mockMultipartFile);

		assertThat(result, is(expectedUrl));
	}

	@Test
	void removePictureReturnExpectedWhenResultsFound() {

		when(mockProductItemService.removeProductItemPicture(1L))
				.thenReturn(new GenericResponse(
						GenericResponse.OP_TYPE_DELETE,
						GenericResponse.SUCCESS_CODE,
						"Picture was removed successfully from Product Item"));

		GenericResponse result = controller.removePicture(1L);

		assertThat(result, hasProperty("operationType", is(GenericResponse.OP_TYPE_DELETE)));
		assertThat(result, hasProperty("code", is(GenericResponse.SUCCESS_CODE)));
		assertThat(result, hasProperty("msg", is("Picture was removed successfully from Product Item")));
	}
}
