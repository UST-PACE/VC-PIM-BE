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
import com.ust.retail.store.pim.dto.catalog.ProductItemDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcPictureException;
import com.ust.retail.store.pim.model.catalog.ProductItemModel;
import com.ust.retail.store.pim.repository.catalog.ProductItemRepository;
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
class ProductItemServiceTest {
	private static final String ITEM_NAME = "ITEM NAME";
	@Mock
	private ProductItemRepository mockProductItemRepository;
	@Mock
	private UpcMasterService mockUpcMasterService;
	@Mock
	private PictureHelper mockPictureHelper;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private ProductItemService service;

	@BeforeEach
	void setUp() {
		lenient().when(mockAuthenticationFacade.getCurrentUserId()).thenReturn(1L);
	}

	@Test
	void findByIdThrowsExceptionWhenNoRecordFound() {
		ResourceNotFoundException result = assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));

		assertThat(result, allOf(
				hasProperty("resourceName", equalTo("Product item")),
				hasProperty("fieldName", equalTo("id")),
				hasProperty("fieldValue", equalTo(1L))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenRecordFound() {
		ProductItemModel model = new ProductItemModel(1L, 1L, "ITEM 1", 1L);

		when(mockProductItemRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductItemDTO result = service.findById(1L);

		assertThat(result, allOf(
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productItemId", equalTo(1L)),
				hasProperty("productItemName", equalTo("ITEM 1"))
		));
	}

	@Test
	void saveOrUpdateReturnsExpectedWhenDtoIsValid() {
		ProductItemDTO dto = new ProductItemDTO(null, 1L, 1L, 1L, null, ITEM_NAME);
		when(mockProductItemRepository.save(any()))
				.thenAnswer(invocation -> {
					ProductItemModel model = invocation.getArgument(0);
					return new ProductItemModel(1L, 1L, model.getProductItemName(), 1L);
				});

		ProductItemDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productSubcategoryId", equalTo(1L)),
				hasProperty("productItemId", equalTo(1L)),
				hasProperty("productItemName", equalTo(ITEM_NAME))
		));
	}

	@Test
	void saveOrUpdateKeepsExistingPicture() {
		String expectedUrl = "http://example.com/picture.png";
		ProductItemModel model = new ProductItemModel(1L, 1L, "ITEM 1", 1L);
		model.updatePictureUrl(expectedUrl);

		when(mockProductItemRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductItemDTO dto = new ProductItemDTO(1L, 1L, 1L, 1L, null, "ITEM NAME");
		when(mockProductItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		ProductItemDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productItemId", equalTo(1L)),
				hasProperty("productItemName", equalTo("ITEM NAME")),
				hasProperty("picture", equalTo(expectedUrl))
		));
	}

	@Test
	void saveOrUpdateModifiesProductHierarchy() {
		String expectedUrl = "http://example.com/picture.png";
		ProductItemModel model = new ProductItemModel(1L, 1L, "ITEM 1", 1L);
		model.updatePictureUrl(expectedUrl);

		when(mockProductItemRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductItemDTO dto = new ProductItemDTO(1L, 1L, 1L, 2L, null, "ITEM NAME");
		when(mockProductItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		ProductItemDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productItemId", equalTo(1L)),
				hasProperty("productItemName", equalTo("ITEM NAME")),
				hasProperty("picture", equalTo(expectedUrl))
		));

		verify(mockUpcMasterService).updateProductHierarchyByItem(1L, 2L, 1L, 1L);
	}

	@Test
	void getProductCategoriesByFiltersReturnsExpectedWhenValueFound() {
		ProductItemDTO dto = new ProductItemDTO(null, 1L, 1L, 1L, null, "item");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("productItemName");
		dto.setOrderDir("desc");
		ProductItemDTO resultDto = new ProductItemDTO(1L, null, null, 1L, null, "My item");
		when(mockProductItemRepository.findByFilters(any(), any(), any()))
				.thenReturn(new PageImpl<>(List.of(resultDto.createModel(1L))));

		Page<ProductItemDTO> result = service.getProductItemsByFilters(dto);

		assertThat(result.getTotalElements(), is(1L));
		assertThat(result.getTotalPages(), is(1));
		assertThat(result.getContent(), contains(samePropertyValuesAs(resultDto)));
	}

	@Test
	void getAutocompleteReturnsExpectedWhenValueFound() {
		ProductItemDTO resultDto = new ProductItemDTO(1L, 1L, 1L, 1L, null, "My item");
		when(mockProductItemRepository.getAutocompleteList(eq(1L), any())).thenReturn(List.of(resultDto));

		List<ProductItemDTO> result = service.getAutocomplete(1L, "item");

		assertThat(result, hasSize(1));
		assertThat(result.get(0), is(resultDto));
	}

	@Test
	void loadReturnsExpected() {
		when(mockProductItemRepository.findByProductSubcategoryProductSubcategoryId(1L))
				.thenReturn(List.of(new ProductItemModel(1L, 1L, "My Item", null)));

		List<ProductItemDTO> result = service.load(1L);

		assertThat(result, is(notNullValue()));
	}
/*
	@Test
	void updateProductItemPictureReturnsExpected() throws Exception {
		String expectedUrl = "http://example.com/picture.png";

		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockProductItemRepository.findById(1L)).thenReturn(Optional.of(new ProductItemModel()));
		when(mockPictureHelper.uploadProductItemPicture(1L, mockFile)).thenReturn(expectedUrl);

		String result = service.updateProductItemPicture(1L, mockFile);

		assertThat(result, is(expectedUrl));
	}
*/
	@Test
	void updateProductItemPictureThrowsExceptionWhenProductGroupNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.updateProductItemPicture(1L, null));
	}
/*
	@Test
	void updateProductItemPictureThrowsExceptionWhenErrorWhileReadingFile() throws Exception {
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockProductItemRepository.findById(1L)).thenReturn(Optional.of(new ProductItemModel()));
		when(mockPictureHelper.uploadProductItemPicture(1L, mockFile)).thenThrow(new IOException("EXPECTED TEST EXCEPTION"));

		assertThrows(UpcPictureException.class, () -> service.updateProductItemPicture(1L, mockFile));
	}
*/
	@Test
	void removeProductItemPictureReturnsExpected() {
		when(mockProductItemRepository.findById(1L)).thenReturn(Optional.of(new ProductItemModel()));

		GenericResponse result = service.removeProductItemPicture(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeProductItemPictureThrowsExceptionWhenProductGroupNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.removeProductItemPicture(1L));
	}
}
