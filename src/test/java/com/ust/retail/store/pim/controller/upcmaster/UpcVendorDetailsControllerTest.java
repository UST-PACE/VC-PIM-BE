package com.ust.retail.store.pim.controller.upcmaster;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.ust.retail.store.pim.dto.upcmaster.PrincipalUpcDTO;
import com.ust.retail.store.pim.dto.upcmaster.SimpleUpcDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcVendorDetailDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcVendorDetailFiltersDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.service.upcmaster.UpcVendorDetailsService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpcVendorDetailsControllerTest {
	@Mock
	private UpcVendorDetailsService mockUpcVendorDetailsService;

	@InjectMocks
	private UpcVendorDetailsController controller;

	@Test
	void createReturnsExpected() {
		UpcVendorDetailDTO request = new UpcVendorDetailDTO();
		when(mockUpcVendorDetailsService.saveOrUpdate(request)).thenReturn(new UpcVendorDetailDTO());

		UpcVendorDetailDTO result = controller.create(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		UpcVendorDetailDTO request = new UpcVendorDetailDTO();
		when(mockUpcVendorDetailsService.saveOrUpdate(request)).thenReturn(new UpcVendorDetailDTO());

		UpcVendorDetailDTO result = controller.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByFiltersReturnsExpected() {
		UpcVendorDetailFiltersDTO request = new UpcVendorDetailFiltersDTO();
		when(mockUpcVendorDetailsService.getUpcVendorDetailsByFilters(request)).thenReturn(Page.empty());

		Page<UpcVendorDetailFiltersDTO> result = controller.findByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		when(mockUpcVendorDetailsService.findById(1L)).thenReturn(new UpcVendorDetailDTO());

		UpcVendorDetailDTO result = controller.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void principalUPCLookUpReturnsExpected() {
		when(mockUpcVendorDetailsService.principalUpcLookup("ANY")).thenReturn(new PrincipalUpcDTO(1L, "ANY"));

		PrincipalUpcDTO result = controller.principalUPCLookup("ANY");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findDefaultVendorForProductReturnsExpected() {
		when(mockUpcVendorDetailsService.findDefaultVendorFor(1L)).thenReturn(new VendorMasterDTO());

		VendorMasterDTO result = controller.findDefaultVendorForProduct(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findProductsForVendorReturnsExpected() {
		when(mockUpcVendorDetailsService.findProductsForVendor(1L)).thenReturn(List.of(new SimpleUpcDTO()));

		List<SimpleUpcDTO> result = controller.findProductsForVendor(1L);

		assertThat(result, is(notNullValue()));
	}
}
