package com.ust.retail.store.pim.service.upcmaster;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.bistro.dto.recipes.BarcodeDTO;
import com.ust.retail.store.bistro.service.recipes.RecipeService;
import com.ust.retail.store.common.util.PictureHelper;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.upcmaster.ImageType;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterFilterDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.exceptions.UpcMasterInUseException;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.service.connector.esl.EslService;
import com.ust.retail.store.pim.service.inventory.InventoryService;
import com.ust.retail.store.pim.service.purchaseorder.PurchaseOrderFacade;
import com.ust.retail.store.pim.util.FixtureLoader;

@ExtendWith(MockitoExtension.class)
class UpcMasterServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private PictureHelper mockPictureHelper;
	@Mock
	private RecipeService mockRecipeService;
	@Mock
	private InventoryService mockInventoryService;
	@Mock
	private EslService mockEslService;
	@Mock
	private PurchaseOrderFacade mockPurchaseOrderFacade;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private UpcMasterService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(UpcMasterServiceTest.class);
	}

	@Test
	void saveOrUpdateReturnsExpectedWhenSaving() {
		UpcMasterDTO request = fixtureLoader.getObject("saveRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());

		when(mockUpcMasterRepository.save(any())).then(invocation -> invocation.getArgument(0));

		UpcMasterDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void saveOrUpdateReturnsExpectedWhenUpdating() {
		UpcMasterDTO request = fixtureLoader.getObject("updateRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockUpcMasterRepository.save(any())).then(invocation -> invocation.getArgument(0));

		UpcMasterDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void saveOrUpdateThrowsExceptionWhenUpdatingProductType() {
		UpcMasterDTO request = fixtureLoader.getObject("updateProductTypeRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockRecipeService.isUpcUsedOnRecipes(1L)).thenReturn(true);

		UpcMasterInUseException exception = assertThrows(UpcMasterInUseException.class, () -> service.saveOrUpdate(request));

		assertThat(exception.getMessage(), is("Can not change field Product type. Currently present in Recipes"));
		assertThat(exception.getUsedIn(), is("RECIPE"));
		assertThat(exception.getErrorCode(), is("PIM-IUC-001"));
	}

	@Test
	void saveOrUpdateThrowsExceptionWhenUpdatingContentPerUnitAndRecipeUsage() {
		UpcMasterDTO request = fixtureLoader.getObject("updateContentPerUnitRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockRecipeService.isUpcUsedOnRecipes(1L)).thenReturn(true);

		UpcMasterInUseException exception = assertThrows(UpcMasterInUseException.class, () -> service.saveOrUpdate(request));

		assertThat(exception.getMessage(), is("Can not change field Content per unit. Currently present in Recipes"));
		assertThat(exception.getUsedIn(), is("RECIPE"));
		assertThat(exception.getErrorCode(), is("PIM-IUC-002"));
	}

	@Test
	void saveOrUpdateThrowsExceptionWhenUpdatingContentPerUnitUomAndRecipeUsage() {
		UpcMasterDTO request = fixtureLoader.getObject("updateContentPerUnitUomRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockRecipeService.isUpcUsedOnRecipes(1L)).thenReturn(true);

		UpcMasterInUseException exception = assertThrows(UpcMasterInUseException.class, () -> service.saveOrUpdate(request));

		assertThat(exception.getMessage(), is("Can not change field Content per unit UOM. Currently present in Recipes"));
		assertThat(exception.getUsedIn(), is("RECIPE"));
		assertThat(exception.getErrorCode(), is("PIM-IUC-003"));
	}

	@Test
	void saveOrUpdateThrowsExceptionWhenUpdatingInventoryAndRecipeUsage() {
		UpcMasterDTO request = fixtureLoader.getObject("updateInventoryUnitRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockRecipeService.isUpcUsedOnRecipes(1L)).thenReturn(true);

		UpcMasterInUseException exception = assertThrows(UpcMasterInUseException.class, () -> service.saveOrUpdate(request));

		assertThat(exception.getMessage(), is("Can not change field Inventory unit. Currently present in Recipes"));
		assertThat(exception.getUsedIn(), is("RECIPE"));
		assertThat(exception.getErrorCode(), is("PIM-IUC-004"));
	}

	@Test
	void saveOrUpdateCompletesWhenRecipeUsageAndNoInvalidChanges() {
		UpcMasterDTO request = fixtureLoader.getObject("updateWithValidRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockRecipeService.isUpcUsedOnRecipes(1L)).thenReturn(true);

		when(mockUpcMasterRepository.save(any())).then(invocation -> invocation.getArgument(0));

		UpcMasterDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void saveOrUpdateThrowsExceptionWhenUpdatingProductTypeAndInventoryUsage() {
		UpcMasterDTO request = fixtureLoader.getObject("updateProductTypeRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockInventoryService.upcHasInventory(1L)).thenReturn(true);

		UpcMasterInUseException exception = assertThrows(UpcMasterInUseException.class, () -> service.saveOrUpdate(request));

		assertThat(exception.getMessage(), is("Can not change field Product type. Currently has On Hand inventory"));
		assertThat(exception.getUsedIn(), is("INVENTORY"));
		assertThat(exception.getErrorCode(), is("PIM-IUC-001"));
	}

	@Test
	void saveOrUpdateThrowsExceptionWhenUpdatingContentPerUnitAndInventoryUsage() {
		UpcMasterDTO request = fixtureLoader.getObject("updateContentPerUnitRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockInventoryService.upcHasInventory(1L)).thenReturn(true);

		UpcMasterInUseException exception = assertThrows(UpcMasterInUseException.class, () -> service.saveOrUpdate(request));

		assertThat(exception.getMessage(), is("Can not change field Content per unit. Currently has On Hand inventory"));
		assertThat(exception.getUsedIn(), is("INVENTORY"));
		assertThat(exception.getErrorCode(), is("PIM-IUC-002"));
	}

	@Test
	void saveOrUpdateThrowsExceptionWhenUpdatingContentPerUnitUomAndInventoryUsage() {
		UpcMasterDTO request = fixtureLoader.getObject("updateContentPerUnitUomRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockInventoryService.upcHasInventory(1L)).thenReturn(true);

		UpcMasterInUseException exception = assertThrows(UpcMasterInUseException.class, () -> service.saveOrUpdate(request));

		assertThat(exception.getMessage(), is("Can not change field Content per unit UOM. Currently has On Hand inventory"));
		assertThat(exception.getUsedIn(), is("INVENTORY"));
		assertThat(exception.getErrorCode(), is("PIM-IUC-003"));
	}

	@Test
	void saveOrUpdateThrowsExceptionWhenUpdatingInventoryUnitAndInventoryUsage() {
		UpcMasterDTO request = fixtureLoader.getObject("updateInventoryUnitRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockInventoryService.upcHasInventory(1L)).thenReturn(true);

		UpcMasterInUseException exception = assertThrows(UpcMasterInUseException.class, () -> service.saveOrUpdate(request));

		assertThat(exception.getMessage(), is("Can not change field Inventory unit. Currently has On Hand inventory"));
		assertThat(exception.getUsedIn(), is("INVENTORY"));
		assertThat(exception.getErrorCode(), is("PIM-IUC-004"));
	}

	@Test
	void saveOrUpdateCompletesWhenInventoryUsageAndNoInvalidChanges() {
		UpcMasterDTO request = fixtureLoader.getObject("updateWithValidRequest", UpcMasterDTO.class).orElse(new UpcMasterDTO());
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);
		when(mockInventoryService.upcHasInventory(1L)).thenReturn(true);

		when(mockUpcMasterRepository.save(any())).then(invocation -> invocation.getArgument(0));

		UpcMasterDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);

		UpcMasterDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenProductNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

//	@Test
//	void getProductsByFiltersReturnsExpected() {
//		UpcMasterFilterDTO request = new UpcMasterFilterDTO();
//		request.setPage(1);
//		request.setSize(10);
//		request.setOrderColumn("id");
//		request.setOrderDir("asc");
//		when(mockUpcMasterRepository.findByFilters(any(), any(), any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of()));
//		Page<UpcMasterFilterDTO> result = service.getProductsByFilters(request);
//
//		assertThat(result, is(notNullValue()));
//	}

	@Test
	void findByNameReturnsExpected() {
		UpcMasterModel product = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());

		when(mockUpcMasterRepository.findByProductNameContaining(any())).thenReturn(List.of(product));

		List<UpcMasterDTO> result = service.findByName("NAME");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadReturnsExpected() {
		UpcMasterModel product = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());

		when(mockUpcMasterRepository.findAll()).thenReturn(List.of(product));

		List<UpcMasterDTO> result = service.load();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByProductItemReturnsExpected() {
		UpcMasterModel product = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());

		when(mockUpcMasterRepository.findByProductItemProductItemId(1L)).thenReturn(List.of(product));

		List<UpcMasterDTO> result = service.findByProductItem(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByProductItemAndVendorReturnsExpected() {
		UpcMasterModel product = fixtureLoader.getObject("product", UpcMasterModel.class).orElse(new UpcMasterModel());

		when(mockUpcMasterRepository.findByItemAndVendor(1L, 1L)).thenReturn(List.of(product));

		List<UpcMasterDTO> result = service.findByProductItemAndVendor(1L, 1L);

		assertThat(result, is(notNullValue()));
	}
/*
	@Test
	void updateUpcPictureReturnsExpected() throws Exception {
		String expectedUrl = "http://example.com/picture.png";

		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(Optional.of(new UpcMasterModel()));
		when(mockPictureHelper.uploadProductPicture(1L, ImageType.WEBSITE, mockFile)).thenReturn(expectedUrl);

		String result = service.updateUpcPicture(1L, ImageType.WEBSITE, mockFile);

		assertThat(result, is(expectedUrl));
	}
*/
	@Test
	void updateUpcPictureThrowsExceptionWhenUpcMasterNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.updateUpcPicture(1L, ImageType.WEBSITE, null));
	}
/*
	@Test
	void updateUpcPictureThrowsExceptionWhenErrorWhileReadingFile() throws Exception {
		MultipartFile mockFile = mock(MultipartFile.class);
		when(mockUpcMasterRepository.findById(1L)).thenReturn(Optional.of(new UpcMasterModel()));
		when(mockPictureHelper.uploadProductPicture(1L, ImageType.WEBSITE, mockFile)).thenThrow(new IOException("EXPECTED TEST EXCEPTION"));

		assertThrows(UpcPictureException.class, () -> service.updateUpcPicture(1L, ImageType.WEBSITE, mockFile));
	}
*/
	@Test
	void removeUpcPictureReturnsExpected() {
		when(mockUpcMasterRepository.findById(1L)).thenReturn(Optional.of(new UpcMasterModel()));

		GenericResponse result = service.removeUpcPicture(1L, ImageType.WEBSITE);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeUpcPictureThrowsExceptionWhenUpcMasterNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.removeUpcPicture(1L, ImageType.WEBSITE));
	}

	@Test
	void getRecipeBarcodeReturnsExpected() {
		Optional<UpcMasterModel> product = fixtureLoader.getObject("product", UpcMasterModel.class);

		when(mockUpcMasterRepository.findById(1L)).thenReturn(product);

		BarcodeDTO result = service.getRecipeBarcode(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeBarcodeThrowsExceptionWhenProductNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.getRecipeBarcode(1L));
	}

	@Test
	void updateProductHierarchyByCategoryCompletesSuccessfully() {
		assertDoesNotThrow(() -> service.updateProductHierarchyByCategory(1L, 2L));
	}

	@Test
	void updateProductHierarchyBySubcategoryCompletesSuccessfully() {
		assertDoesNotThrow(() -> service.updateProductHierarchyBySubcategory(1L, 2L, 3L));
	}

	@Test
	void updateProductHierarchyByItemCompletesSuccessfully() {
		assertDoesNotThrow(() -> service.updateProductHierarchyByItem(1L, 2L, 3L, 4L));
	}
}
