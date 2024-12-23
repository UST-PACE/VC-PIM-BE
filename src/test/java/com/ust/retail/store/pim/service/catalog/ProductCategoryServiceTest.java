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
import com.ust.retail.store.pim.dto.catalog.ProductCategoryDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcPictureException;
import com.ust.retail.store.pim.model.catalog.ProductCategoryModel;
import com.ust.retail.store.pim.repository.catalog.ProductCategoryRepository;
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
class ProductCategoryServiceTest {
	@Mock
	private ProductCategoryRepository mockProductCategoryRepository;
	@Mock
	private UpcMasterService mockUpcMasterService;
	@Mock
	private PictureHelper mockPictureHelper;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private ProductCategoryService service;

	@BeforeEach
	void setUp() {
		lenient().when(mockAuthenticationFacade.getCurrentUserId()).thenReturn(1L);
	}

	@Test
	void findByIdThrowsExceptionWhenNoRecordFound() {
		ResourceNotFoundException result = assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));

		assertThat(result, allOf(
				hasProperty("resourceName", equalTo("Product category")),
				hasProperty("fieldName", equalTo("id")),
				hasProperty("fieldValue", equalTo(1L))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenRecordFound() {
		ProductCategoryModel model = new ProductCategoryModel(1L, 1L, "CATEGORY 1", 1L);

		when(mockProductCategoryRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductCategoryDTO result = service.findById(1L);

		assertThat(result, allOf(
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productCategoryName", equalTo("CATEGORY 1"))
		));
	}

	@Test
	void saveOrUpdateReturnsExpectedWhenDtoIsValid() {
		ProductCategoryDTO dto = new ProductCategoryDTO(null, 1L, null, "CATEGORY NAME");
		when(mockProductCategoryRepository.save(any()))
				.thenAnswer(invocation -> {
					ProductCategoryModel model = invocation.getArgument(0);
					return new ProductCategoryModel(1L, 1L, model.getProductCategoryName(), 1L);
				});

		ProductCategoryDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productCategoryName", equalTo("CATEGORY NAME"))
		));
	}

	@Test
	void saveOrUpdateKeepsExistingPicture() {
		String expectedUrl = "http://example.com/picture.png";
		ProductCategoryModel model = new ProductCategoryModel(1L, 1L, "CATEGORY 1", 1L);
		model.updatePictureUrl(expectedUrl);

		when(mockProductCategoryRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductCategoryDTO dto = new ProductCategoryDTO(1L, 1L, null, "CATEGORY NAME");
		when(mockProductCategoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		ProductCategoryDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productCategoryName", equalTo("CATEGORY NAME")),
				hasProperty("picture", equalTo(expectedUrl))
		));
	}

	@Test
	void saveOrUpdateModifiesProductHierarchy() {
		String expectedUrl = "http://example.com/picture.png";
		ProductCategoryModel model = new ProductCategoryModel(1L, 1L, "CATEGORY 1", 1L);
		model.updatePictureUrl(expectedUrl);

		when(mockProductCategoryRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductCategoryDTO dto = new ProductCategoryDTO(1L, 2L, null, "CATEGORY NAME");
		when(mockProductCategoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		ProductCategoryDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productCategoryId", equalTo(1L)),
				hasProperty("productCategoryName", equalTo("CATEGORY NAME")),
				hasProperty("picture", equalTo(expectedUrl))
		));

		verify(mockUpcMasterService).updateProductHierarchyByCategory(1L, 2L);
	}

	@Test
	void getProductCategoriesByFiltersReturnsExpectedWhenValueFound() {
		ProductCategoryDTO dto = new ProductCategoryDTO(null, 1L, null, "category");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("productCategoryName");
		dto.setOrderDir("desc");
		ProductCategoryDTO resultDto = new ProductCategoryDTO(1L, 1L, null, "My category");
		when(mockProductCategoryRepository.findByFilters(any(), any(), any()))
				.thenReturn(new PageImpl<>(List.of(resultDto.createModel(1L))));

		Page<ProductCategoryDTO> result = service.getProductCategoriesByFilters(dto);

		assertThat(result.getTotalElements(), is(1L));
		assertThat(result.getTotalPages(), is(1));
		assertThat(result.getContent(), contains(samePropertyValuesAs(resultDto)));
	}

	@Test
	void getAutocompleteReturnsExpectedWhenValueFound() {
		ProductCategoryDTO resultDto = new ProductCategoryDTO(1L, 1L, null, "My category");
		when(mockProductCategoryRepository.getAutocompleteList(eq(1L), any())).thenReturn(List.of(resultDto));

		List<ProductCategoryDTO> result = service.getAutocomplete(1L, "category");

		assertThat(result, hasSize(1));
		assertThat(result.get(0), is(resultDto));
	}

	@Test
	void loadReturnsExpected() {
		when(mockProductCategoryRepository.findByProductGroupProductGroupId(1L))
				.thenReturn(List.of(new ProductCategoryModel(1L, 1L, "My category", null)));

		List<ProductCategoryDTO> result = service.load(1L);

		assertThat(result, is(notNullValue()));
	}

/*	@Test
	void updateProductCategoryPictureReturnsExpected() throws Exception {
		String expectedUrl = "http://example.com/picture.png";

		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockProductCategoryRepository.findById(1L)).thenReturn(Optional.of(new ProductCategoryModel()));
		when(mockPictureHelper.uploadProductCategoryPicture(1L, mockFile)).thenReturn(expectedUrl);

		String result = service.updateProductCategoryPicture(1L, mockFile);

		assertThat(result, is(expectedUrl));
	}
*/
	@Test
	void updateProductCategoryPictureThrowsExceptionWhenProductGroupNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.updateProductCategoryPicture(1L, null));
	}

/*	@Test
	void updateProductCategoryPictureThrowsExceptionWhenErrorWhileReadingFile() throws Exception {
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockProductCategoryRepository.findById(1L)).thenReturn(Optional.of(new ProductCategoryModel()));
		when(mockPictureHelper.uploadProductCategoryPicture(1L, mockFile)).thenThrow(new IOException("EXPECTED TEST EXCEPTION"));

		assertThrows(UpcPictureException.class, () -> service.updateProductCategoryPicture(1L, mockFile));
	}
*/
	@Test
	void removeProductCategoryPictureReturnsExpected() {
		when(mockProductCategoryRepository.findById(1L)).thenReturn(Optional.of(new ProductCategoryModel()));

		GenericResponse result = service.removeProductCategoryPicture(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeProductCategoryPictureThrowsExceptionWhenProductGroupNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.removeProductCategoryPicture(1L));
	}
}
