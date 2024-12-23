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
import com.ust.retail.store.pim.dto.catalog.ProductGroupDTO;
import com.ust.retail.store.pim.service.catalog.ProductGroupService;

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
class ProductGroupControllerTest {
	public static final String GROUP_NAME = "My Group";
	@Mock
	private ProductGroupService mockProductGroupService;
	@Mock
	private MultipartFile mockMultipartFile;

	@InjectMocks
	private ProductGroupController controller;

	@Test
	void createReturnsExpectedWheInputIsValid() {
		ProductGroupDTO dto = new ProductGroupDTO(null, GROUP_NAME);
		when(mockProductGroupService.saveOrUpdate(any()))
				.then(invocation -> new ProductGroupDTO(1L, invocation.<ProductGroupDTO>getArgument(0).getProductGroupName()));

		ProductGroupDTO result = controller.create(dto);

		assertThat(result, allOf(
				hasProperty("productGroupId", equalTo(1L)),
				hasProperty("productGroupName", equalTo(GROUP_NAME))
		));
	}

	@Test
	void updateReturnsExpectedWhenInputIsValid() {
		ProductGroupDTO dto = new ProductGroupDTO(1L, GROUP_NAME);
		when(mockProductGroupService.saveOrUpdate(any()))
				.then(invocation -> invocation.<ProductGroupDTO>getArgument(0));

		ProductGroupDTO result = controller.update(dto);

		assertThat(result, allOf(
				hasProperty("productGroupId", equalTo(1L)),
				hasProperty("productGroupName", equalTo(GROUP_NAME))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenObjectFound() {
		ProductGroupDTO dto = new ProductGroupDTO(1L, GROUP_NAME);
		when(mockProductGroupService.findById(1L)).thenReturn(dto);

		ProductGroupDTO result = controller.findById(1L);

		assertThat(result, allOf(
				hasProperty("productGroupId", equalTo(1L)),
				hasProperty("productGroupName", equalTo(GROUP_NAME))
		));
	}

	@Test
	void findByFiltersReturnsExpectedWhenResultsFound() {
		ProductGroupDTO dto1 = new ProductGroupDTO(1L, GROUP_NAME);
		ProductGroupDTO dto6 = new ProductGroupDTO(6L, "Group 6");
		List<ProductGroupDTO> searchResults = List.of(dto1, dto6);
		ProductGroupDTO dto = new ProductGroupDTO(null, "Group");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("productGroupName");
		dto.setOrderDir("desc");
		when(mockProductGroupService.getProductGroupsByFilters(dto))
				.thenReturn(new PageImpl<>(searchResults));

		Page<ProductGroupDTO> result = controller.findByFilters(dto);

		assertThat(result.getTotalElements(), is(2L));
		assertThat(result.getContent(), contains(dto1, dto6));
	}

	@Test
	void findAutoCompleteOptionsReturnExpectedWhenResultsFound() {
		ProductGroupDTO dto1 = new ProductGroupDTO(1L, GROUP_NAME);
		ProductGroupDTO dto6 = new ProductGroupDTO(6L, "Group 6");
		List<ProductGroupDTO> searchResults = List.of(dto1, dto6);
		when(mockProductGroupService.getAutocomplete("Group")).thenReturn(searchResults);

		List<ProductGroupDTO> result = controller.findAutoCompleteOptions("Group");

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void loadCatalogReturnExpectedWhenResultsFound() {
		ProductGroupDTO dto1 = new ProductGroupDTO(1L, GROUP_NAME);
		ProductGroupDTO dto6 = new ProductGroupDTO(6L, "Group 6");
		List<ProductGroupDTO> results = List.of(dto1, dto6);
		when(mockProductGroupService.load()).thenReturn(results);

		List<ProductGroupDTO> result = controller.loadCatalog();

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void uploadPictureReturnExpectedWhenResultsFound() {
		String expectedUrl = "http://example.com/picture.png";
		when(mockProductGroupService.updateProductGroupPicture(1L, mockMultipartFile)).thenReturn(expectedUrl);

		String result = controller.uploadPicture(1L, mockMultipartFile);

		assertThat(result, is(expectedUrl));
	}

	@Test
	void removePictureReturnExpectedWhenResultsFound() {

		when(mockProductGroupService.removeProductGroupPicture(1L))
				.thenReturn(new GenericResponse(
						GenericResponse.OP_TYPE_DELETE,
						GenericResponse.SUCCESS_CODE,
						"Picture was removed successfully from Product Group"));

		GenericResponse result = controller.removePicture(1L);

		assertThat(result, hasProperty("operationType", is(GenericResponse.OP_TYPE_DELETE)));
		assertThat(result, hasProperty("code", is(GenericResponse.SUCCESS_CODE)));
		assertThat(result, hasProperty("msg", is("Picture was removed successfully from Product Group")));
	}
}
