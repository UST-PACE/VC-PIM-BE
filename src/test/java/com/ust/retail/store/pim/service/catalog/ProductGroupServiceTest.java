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
import com.ust.retail.store.pim.dto.catalog.ProductGroupDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcPictureException;
import com.ust.retail.store.pim.model.catalog.ProductGroupModel;
import com.ust.retail.store.pim.repository.catalog.ProductGroupRepository;

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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductGroupServiceTest {
	@Mock
	private ProductGroupRepository mockProductGroupRepository;
	@Mock
	private PictureHelper mockPictureHelper;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private ProductGroupService service;

	@BeforeEach
	void setUp() {
		lenient().when(mockAuthenticationFacade.getCurrentUserId()).thenReturn(1L);
	}

	@Test
	void findByIdThrowsExceptionWhenNoRecordFound() {
		ResourceNotFoundException result = assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));

		assertThat(result, allOf(
				hasProperty("resourceName", equalTo("Product group")),
				hasProperty("fieldName", equalTo("id")),
				hasProperty("fieldValue", equalTo(1L))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenRecordFound() {
		ProductGroupModel model = new ProductGroupModel(1L, "GROUP 1", true, 1L);
		when(mockProductGroupRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductGroupDTO result = service.findById(1L);

		assertThat(result, allOf(
				hasProperty("productGroupId", equalTo(1L)),
				hasProperty("productGroupName", equalTo("GROUP 1"))
		));
	}

	@Test
	void saveOrUpdateReturnsExpectedWhenDtoIsValid() {
		ProductGroupDTO dto = new ProductGroupDTO(null, "GROUP NAME");
		when(mockProductGroupRepository.save(any()))
				.thenAnswer(invocation -> {
					ProductGroupModel model = invocation.getArgument(0);
					return new ProductGroupModel(1L, model.getProductGroupName(), true, 1L);
				});

		ProductGroupDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productGroupId", equalTo(1L)),
				hasProperty("productGroupName", equalTo("GROUP NAME"))
		));
	}

	@Test
	void saveOrUpdateKeepsExistingPicture() {
		String expectedUrl = "http://example.com/picture.png";
		ProductGroupModel model = new ProductGroupModel(1L, "GROUP 1", true, 1L);
		model.updatePictureUrl(expectedUrl);

		when(mockProductGroupRepository.findById(1L)).thenReturn(Optional.of(model));

		ProductGroupDTO dto = new ProductGroupDTO(1L, "GROUP NAME");
		when(mockProductGroupRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		ProductGroupDTO result = service.saveOrUpdate(dto);

		assertThat(result, allOf(
				hasProperty("productGroupId", equalTo(1L)),
				hasProperty("productGroupName", equalTo("GROUP NAME")),
				hasProperty("picture", equalTo(expectedUrl))
		));
	}

	@Test
	void getProductGroupsByFiltersReturnsExpectedWhenValueFound() {
		ProductGroupDTO dto = new ProductGroupDTO(null, "group");
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("productGroupName");
		dto.setOrderDir("desc");
		ProductGroupDTO resultDto = new ProductGroupDTO(1L, "My group");
		when(mockProductGroupRepository.findByFilters(any(), any()))
				.thenReturn(new PageImpl<>(List.of(resultDto.createModel(1L))));

		Page<ProductGroupDTO> result = service.getProductGroupsByFilters(dto);

		assertThat(result.getTotalElements(), is(1L));
		assertThat(result.getTotalPages(), is(1));
		assertThat(result.getContent(), contains(samePropertyValuesAs(resultDto)));
	}

	@Test
	void getAutocompleteReturnsExpectedWhenValueFound() {
		ProductGroupDTO resultDto = new ProductGroupDTO(1L, "My group");
		when(mockProductGroupRepository.getAutocompleteList(any())).thenReturn(List.of(resultDto));

		List<ProductGroupDTO> result = service.getAutocomplete("group");

		assertThat(result, hasSize(1));
		assertThat(result.get(0), is(resultDto));
	}

	@Test
	void loadReturnsExpected() {
		when(mockProductGroupRepository.findAll())
				.thenReturn(List.of(new ProductGroupModel(1L, "My Group", true, null)));

		List<ProductGroupDTO> result = service.load();

		assertThat(result, is(notNullValue()));
	}
/*
	@Test
	void updateProductGroupPictureReturnsExpected() throws Exception {
		String expectedUrl = "http://example.com/picture.png";

		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockProductGroupRepository.findById(1L)).thenReturn(Optional.of(new ProductGroupModel()));
		when(mockPictureHelper.uploadProductGroupPicture(1L, mockFile)).thenReturn(expectedUrl);

		String result = service.updateProductGroupPicture(1L, mockFile);

		assertThat(result, is(expectedUrl));
	}
*/
	@Test
	void updateProductGroupPictureThrowsExceptionWhenProductGroupNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.updateProductGroupPicture(1L, null));
	}
/*
	@Test
	void updateProductGroupPictureThrowsExceptionWhenErrorWhileReadingFile() throws Exception {
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockProductGroupRepository.findById(1L)).thenReturn(Optional.of(new ProductGroupModel()));
		when(mockPictureHelper.uploadProductGroupPicture(1L, mockFile)).thenThrow(new IOException("EXPECTED TEST EXCEPTION"));

		assertThrows(UpcPictureException.class, () -> service.updateProductGroupPicture(1L, mockFile));
	}
*/
	@Test
	void removeProductGroupPictureReturnsExpected() {
		when(mockProductGroupRepository.findById(1L)).thenReturn(Optional.of(new ProductGroupModel()));

		GenericResponse result = service.removeProductGroupPicture(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeProductGroupPictureThrowsExceptionWhenProductGroupNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.removeProductGroupPicture(1L));
	}
}
