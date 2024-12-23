package com.ust.retail.store.pim.controller.upcmaster;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.ust.retail.store.bistro.dto.recipes.BarcodeDTO;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.upcmaster.ImageType;
import com.ust.retail.store.pim.dto.upcmaster.ProductFilterScreenConfigDTO;
import com.ust.retail.store.pim.dto.upcmaster.ProductScreenConfigDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterFilterDTO;
import com.ust.retail.store.pim.service.productmaster.ProductScreenConfigFacade;
import com.ust.retail.store.pim.service.upcmaster.UpcMasterService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpcMasterControllerTest {
	@Mock
	private ProductScreenConfigFacade mockProductScreenConfigFacade;
	@Mock
	private UpcMasterService mockUpcMasterService;

	@InjectMocks
	private UpcMasterController controller;

	@Test
	void createReturnsExpected() {
		UpcMasterDTO request = new UpcMasterDTO();
		when(mockUpcMasterService.saveOrUpdate(request)).thenReturn(new UpcMasterDTO());

		UpcMasterDTO result = controller.create(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		UpcMasterDTO request = new UpcMasterDTO();
		when(mockUpcMasterService.saveOrUpdate(request)).thenReturn(new UpcMasterDTO());

		UpcMasterDTO result = controller.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockUpcMasterService.findById(1L)).thenReturn(new UpcMasterDTO());

		UpcMasterDTO result = controller.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeBarcodeReturnsExpected() {
		when(mockUpcMasterService.getRecipeBarcode(1L)).thenReturn(new BarcodeDTO(new byte[0]));

		BarcodeDTO result = controller.getRecipeBarcode(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByNameReturnsExpected() {
		when(mockUpcMasterService.findByName("ANY")).thenReturn(List.of());

		List<UpcMasterDTO> result = controller.findByName("ANY");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByProductItemReturnsExpected() {
		when(mockUpcMasterService.findByProductItem(1L)).thenReturn(List.of());

		List<UpcMasterDTO> result = controller.findByProductItem(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByProductItemAndVendorReturnsExpected() {
		when(mockUpcMasterService.findByProductItemAndVendor(1L, 1L)).thenReturn(List.of());

		List<UpcMasterDTO> result = controller.findByProductItemAndVendor(1L, 1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByFiltersReturnsExpected() {
		UpcMasterFilterDTO request = new UpcMasterFilterDTO();
		when(mockUpcMasterService.getProductsByFilters(request)).thenReturn(Page.empty());

		Page<UpcMasterFilterDTO> result = controller.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadReturnsExpected() {
		when(mockUpcMasterService.load()).thenReturn(List.of());

		List<UpcMasterDTO> result = controller.load();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void screenConfigReturnsExpected() {
		when(mockProductScreenConfigFacade.getScreenConfig()).thenReturn(new ProductScreenConfigDTO());

		ProductScreenConfigDTO result = controller.screenConfig();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void filterScreenConfigReturnsExpected() {
		when(mockProductScreenConfigFacade.getFilterScreenConfig()).thenReturn(new ProductFilterScreenConfigDTO());

		ProductFilterScreenConfigDTO result = controller.filterScreenConfig();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void uploadPictureReturnsExpected() {
		String expectedUrl = "http://example.com/picture.png";
		MultipartFile mockMultipartFile = mock(MultipartFile.class);
		when(mockUpcMasterService.updateUpcPicture(1L, ImageType.WEBSITE, mockMultipartFile)).thenReturn(expectedUrl);

		String result = controller.uploadPicture(1L, ImageType.WEBSITE, mockMultipartFile);

		assertThat(result, is(expectedUrl));
	}

	@Test
	void removePictureReturnsExpected() {
		when(mockUpcMasterService.removeUpcPicture(1L, ImageType.WEBSITE)).thenReturn(new GenericResponse());

		GenericResponse result = controller.removePicture(1L, ImageType.WEBSITE);

		assertThat(result, is(notNullValue()));
	}
}
