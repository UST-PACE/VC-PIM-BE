package com.ust.retail.store.pim.controller.vendormaster;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDropdownDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterFilterDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterStoreDTO;
import com.ust.retail.store.pim.service.vendormaster.VendorMasterService;

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
class VendorMasterControllerTest {
	private static final String VENDOR_MASTER_NAME = "My Vendor";

	@Mock
	private VendorMasterService mockVendorMasterService;

	@InjectMocks
	private VendorMasterController controller;

	@Test
	void createReturnsExpectedWheInputIsValid() {
		VendorMasterDTO dto = new VendorMasterDTO(null, VENDOR_MASTER_NAME, null);
		when(mockVendorMasterService.saveOrUpdate(any()))
				.then(invocation -> new VendorMasterDTO(1L, invocation.<VendorMasterDTO>getArgument(0).getVendorName(), null));

		VendorMasterDTO result = controller.create(dto);

		assertThat(result, allOf(
				hasProperty("vendorMasterId", equalTo(1L)),
				hasProperty("vendorName", equalTo(VENDOR_MASTER_NAME))
		));
	}

	@Test
	void updateReturnsExpectedWhenInputIsValid() {
		VendorMasterDTO dto = new VendorMasterDTO(1L, VENDOR_MASTER_NAME, null);
		when(mockVendorMasterService.saveOrUpdate(any()))
				.then(invocation -> invocation.<VendorMasterDTO>getArgument(0));

		VendorMasterDTO result = controller.update(dto);

		assertThat(result, allOf(
				hasProperty("vendorMasterId", equalTo(1L)),
				hasProperty("vendorName", equalTo(VENDOR_MASTER_NAME))
		));
	}

	@Test
	void findByIdReturnsExpectedWhenObjectFound() {
		VendorMasterDTO dto = new VendorMasterDTO(1L, VENDOR_MASTER_NAME, null);
		when(mockVendorMasterService.findById(1L)).thenReturn(dto);

		VendorMasterDTO result = controller.findById(1L);

		assertThat(result, allOf(
				hasProperty("vendorMasterId", equalTo(1L)),
				hasProperty("vendorName", equalTo(VENDOR_MASTER_NAME))
		));
	}

	@Test
	void findByFiltersReturnsExpectedWhenResultsFound() {
		VendorMasterFilterDTO dto1 = new VendorMasterFilterDTO(1L, VENDOR_MASTER_NAME, null, null);
		VendorMasterFilterDTO dto6 = new VendorMasterFilterDTO(6L, "Vendor 6", null, null);
		List<VendorMasterFilterDTO> searchResults = List.of(dto1, dto6);
		VendorMasterFilterDTO dto = new VendorMasterFilterDTO(null, "Vendor", null, null);
		dto.setPage(0);
		dto.setSize(10);
		dto.setOrderColumn("vendorName");
		dto.setOrderDir("desc");
		when(mockVendorMasterService.getVendorMasterByFilters(dto))
				.thenReturn(new PageImpl<>(searchResults));

		Page<VendorMasterFilterDTO> result = controller.findByFilters(dto);

		assertThat(result.getTotalElements(), is(2L));
		assertThat(result.getContent(), contains(dto1, dto6));
	}

	@Test
	void findVendorCodeAutoCompleteOptionsReturnExpectedWhenResultsFound() {
		VendorMasterDTO dto1 = new VendorMasterDTO(1L, "My Vendor 1", "Code 1");
		VendorMasterDTO dto6 = new VendorMasterDTO(6L, "Vendor 6", "Code 6");
		List<VendorMasterDTO> searchResults = List.of(dto1, dto6);
		when(mockVendorMasterService.getAutocompleteVendorCode("Code")).thenReturn(searchResults);

		List<VendorMasterDTO> result = controller.findVendorCodeAutoCompleteOptions("Code");

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void findVendorNameAutoCompleteOptionsReturnExpectedWhenResultsFound() {
		VendorMasterDTO dto1 = new VendorMasterDTO(1L, "My Vendor 1", null);
		VendorMasterDTO dto6 = new VendorMasterDTO(6L, "Vendor 6", null);
		List<VendorMasterDTO> searchResults = List.of(dto1, dto6);
		when(mockVendorMasterService.getAutocompleteVendorName("Vendor")).thenReturn(searchResults);

		List<VendorMasterDTO> result = controller.findVendorNameAutoCompleteOptions("Vendor");

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void loadCatalogReturnExpectedWhenResultsFound() {
		VendorMasterDropdownDTO dto1 = new VendorMasterDropdownDTO(1L, VENDOR_MASTER_NAME, "Code 1");
		VendorMasterDropdownDTO dto6 = new VendorMasterDropdownDTO(6L, "Vendor 6", "Code 6");
		List<VendorMasterDropdownDTO> results = List.of(dto1, dto6);
		when(mockVendorMasterService.load()).thenReturn(results);

		List<VendorMasterDropdownDTO> result = controller.loadCatalog();

		assertThat(result, hasSize(2));
		assertThat(result, contains(dto1, dto6));
	}

	@Test
	void addStoreNumberReturnsExpected() {
		VendorMasterStoreDTO request = new VendorMasterStoreDTO(1L, 1L);
		VendorMasterStoreDTO response = new VendorMasterStoreDTO(1L, 1L);

		when(mockVendorMasterService.addStoreNumber(request)).thenReturn(response);

		VendorMasterStoreDTO result = controller.addStoreNumber(request);

		assertThat(result, is(response));
	}

	@Test
	void removeStoreNumberReturnsExpected() {

		GenericResponse result = controller.removeStoreNumber(1L, 1L);

		assertThat(result, hasProperty("operationType", is(GenericResponse.OP_TYPE_DELETE)));
		assertThat(result, hasProperty("code", is(GenericResponse.SUCCESS_CODE)));
		assertThat(result, hasProperty("msg", is("true")));
	}
}
