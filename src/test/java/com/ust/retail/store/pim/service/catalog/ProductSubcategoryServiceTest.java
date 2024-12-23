package com.ust.retail.store.pim.service.catalog;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.multipart.MultipartFile;

import com.ust.retail.store.common.util.PictureHelper;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.catalog.ProductSubcategoryDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcPictureException;
import com.ust.retail.store.pim.model.catalog.ProductSubcategoryModel;
import com.ust.retail.store.pim.repository.catalog.ProductSubcategoryRepository;
import com.ust.retail.store.pim.service.upcmaster.UpcMasterService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductSubcategoryServiceTest {
	@Mock
	private ProductSubcategoryRepository mockProductSubcategoryRepository;
	@Mock
	private UpcMasterService mockUpcMasterService;
	@Mock
	private PictureHelper mockPictureHelper;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private ProductSubcategoryService service;

	@BeforeEach
	void setUp() {
		lenient().when(mockAuthenticationFacade.getCurrentUserId()).thenReturn(1L);
	}

	@Test
	void findByIdThrowsExceptionWhenNoRecordFound() {
		ResourceNotFoundException result = assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));

		assertThat(result, allOf(
				hasProperty("resourceName", equalTo("Product subcategory")),
				hasProperty("fieldName", equalTo("id")),
				hasProperty("fieldValue", equalTo(1L))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenRecordFound() {
		ProductSubcategoryModel model = new ProductSubcategoryModel(1L, 1L, "SUBCATEGORY 1", 1L);

		when(mockProductSubcategoryRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductSubcategoryDTO result = service.findById(1L);

		assertThat(result, allOf(
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productSubcategoryName", equalTo("SUBCATEGORY 1"))
		));
	}

	@Test
	void saveOrUpdateReturnsExpectedWhenDtoIsValid() {
		ProductSubcategoryDTO dto = new ProductSubcategoryDTO(null, 1L, 1L, null, "SUBCATEGORY NAME");
		when(mockProductSubcategoryRepository.save(any()))
				.thenAnswer(invocation -> {
					ProductSubcategoryModel model = invocation.getArgument(0);
					return new ProductSubcategoryModel(1L, 1L, model.getProductSubcategoryName(), 1L);
				});

		ProductSubcategoryDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productSubcategoryName", equalTo("SUBCATEGORY NAME"))
		));
	}

	@Test
	void saveOrUpdateKeepsExistingPicture() {
		String expectedUrl = "http://example.com/picture.png";
		ProductSubcategoryModel model = new ProductSubcategoryModel(1L, 1L, "SUBCATEGORY 1", 1L);
		model.updatePictureUrl(expectedUrl);

		when(mockProductSubcategoryRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductSubcategoryDTO dto = new ProductSubcategoryDTO(1L, 1L, 1L, null, "SUBCATEGORY NAME");
		when(mockProductSubcategoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		ProductSubcategoryDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productSubcategoryName", equalTo("SUBCATEGORY NAME")),
				hasProperty("picture", equalTo(expectedUrl))
		));
	}

	@Test
	void saveOrUpdateModifiesProductHierarchy() {
		String expectedUrl = "http://example.com/picture.png";
		ProductSubcategoryModel model = new ProductSubcategoryModel(1L, 1L, "SUBCATEGORY 1", 1L);
		model.updatePictureUrl(expectedUrl);

		when(mockProductSubcategoryRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductSubcategoryDTO dto = new ProductSubcategoryDTO(1L, 1L, 2L, null, "SUBCATEGORY NAME");
		when(mockProductSubcategoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		ProductSubcategoryDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productSubcategoryName", equalTo("SUBCATEGORY NAME")),
				hasProperty("picture", equalTo(expectedUrl))
		));

		verify(mockUpcMasterService).updateProductHierarchyBySubcategory(1L, 2L, 1L);
	}

	@Test
	void getProductCategoriesByFiltersReturnsExpectedWhenValueFound() {
		ProductSubcategoryDTO dto = new ProductSubcategoryDTO(null, 1L, 1L, null, "subcategory");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("productSubcategoryName");
		dto.setOrderDir("desc");
		ProductSubcategoryDTO resultDto = new ProductSubcategoryDTO(1L, null, 1L, null, "My subcategory");
		when(mockProductSubcategoryRepository.findByFilters(any(), any(), any()))
				.thenReturn(new PageImpl<>(List.of(resultDto.createModel(1L))));

		Page<ProductSubcategoryDTO> result = service.getProductSubcategoriesByFilters(dto);

		assertThat(result.getTotalElements(), is(1L));
		assertThat(result.getTotalPages(), is(1));
		assertThat(result.getContent(), contains(samePropertyValuesAs(resultDto)));
	}

	@Test
	void getAutocompleteReturnsExpectedWhenValueFound() {
		ProductSubcategoryDTO resultDto = new ProductSubcategoryDTO(1L, 1L, 1L, null, "My subcategory");
		when(mockProductSubcategoryRepository.getAutocompleteList(eq(1L), any())).thenReturn(List.of(resultDto));

		List<ProductSubcategoryDTO> result = service.getAutocomplete(1L, "subcategory");

		assertThat(result, hasSize(1));
		assertThat(result.get(0), is(resultDto));
	}

	@Test
	void loadReturnsExpected() {
		when(mockProductSubcategoryRepository.findByProductCategoryProductCategoryId(1L))
				.thenReturn(List.of(new ProductSubcategoryModel(1L, 1L, "My Subcategory", null)));

		List<ProductSubcategoryDTO> result = service.load(1L);

		assertThat(result, is(notNullValue()));
	}
/*
	@Test
	void updateProductSubcategoryPictureReturnsExpected() throws Exception {
		String expectedUrl = "http://example.com/picture.png";

		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockProductSubcategoryRepository.findById(1L)).thenReturn(Optional.of(new ProductSubcategoryModel()));
		when(mockPictureHelper.uploadProductSubcategoryPicture(1L, mockFile)).thenReturn(expectedUrl);

		String result = service.updateProductSubcategoryPicture(1L, mockFile);

		assertThat(result, is(expectedUrl));
	}
*/
	@Test
	void updateProductSubcategoryPictureThrowsExceptionWhenProductGroupNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.updateProductSubcategoryPicture(1L, null));
	}
/*
	@Test
	void updateProductSubcategoryPictureThrowsExceptionWhenErrorWhileReadingFile() throws Exception {
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockProductSubcategoryRepository.findById(1L)).thenReturn(Optional.of(new ProductSubcategoryModel()));
		when(mockPictureHelper.uploadProductSubcategoryPicture(1L, mockFile)).thenThrow(new IOException("EXPECTED TEST EXCEPTION"));

		assertThrows(UpcPictureException.class, () -> service.updateProductSubcategoryPicture(1L, mockFile));
	}
*/
	@Test
	void removeProductSubcategoryPictureReturnsExpected() {
		when(mockProductSubcategoryRepository.findById(1L)).thenReturn(Optional.of(new ProductSubcategoryModel()));

		GenericResponse result = service.removeProductSubcategoryPicture(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeProductSubcategoryPictureThrowsExceptionWhenProductGroupNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.removeProductSubcategoryPicture(1L));
	}
}
