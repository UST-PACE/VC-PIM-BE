package com.ust.retail.store.pim.service.upcmaster;

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

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.upcmaster.PrincipalUpcDTO;
import com.ust.retail.store.pim.dto.upcmaster.SimpleUpcDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcVendorDetailDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcVendorDetailFiltersDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.exceptions.InvalidUPCException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.model.upcmaster.UpcVendorDetailsModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcVendorDetailsRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpcVendorDetailsServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private UpcVendorDetailsRepository mockUpcVendorDetailsRepository;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private UpcVendorDetailsService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(UpcVendorDetailsServiceTest.class);
	}

	@Test
	void saveOrUpdate() {
		UpcVendorDetailDTO request = fixtureLoader.getObject("saveRequest", UpcVendorDetailDTO.class).orElse(new UpcVendorDetailDTO());
		when(mockUpcVendorDetailsRepository.saveAndFlush(any())).then(invocation -> invocation.getArgument(0));

		UpcVendorDetailDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<UpcVendorDetailsModel> vendorDetailModel = fixtureLoader.getObject("vendorDetailModel", UpcVendorDetailsModel.class);

		when(mockUpcVendorDetailsRepository.findById(1L)).thenReturn(vendorDetailModel);

		UpcVendorDetailDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenRecordNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void getUpcVendorDetailsByFiltersReturnsExpected() {
		UpcVendorDetailFiltersDTO request = new UpcVendorDetailFiltersDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");
		UpcVendorDetailsModel vendorDetailModel = fixtureLoader.getObject("vendorDetailModel", UpcVendorDetailsModel.class).orElse(new UpcVendorDetailsModel());
		when(mockUpcVendorDetailsRepository.findByFilters(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(vendorDetailModel)));

		Page<UpcVendorDetailFiltersDTO> result = service.getUpcVendorDetailsByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void principalUpcLookupReturnsExpected() {
		when(mockUpcMasterRepository.findByPrincipalUpc("CODE")).thenReturn(Optional.of(new UpcMasterModel()));

		PrincipalUpcDTO result = service.principalUpcLookup("CODE");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void principalUpcLookupThrowsExceptionWhenRecordNotFound() {
		assertThrows(InvalidUPCException.class, () -> service.principalUpcLookup("CODE"));
	}

	@Test
	void findDefaultVendorForReturnsExpected() {
		Optional<UpcVendorDetailsModel> vendorDetailModel = fixtureLoader.getObject("vendorDetailModel", UpcVendorDetailsModel.class);

		when(mockUpcVendorDetailsRepository.findByDefaultVendorTrueAndUpcMasterUpcMasterId(1L)).thenReturn(vendorDetailModel);

		VendorMasterDTO result = service.findDefaultVendorFor(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findDefaultVendorForThrowsExceptionWhenRecordNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findDefaultVendorFor(1L));
	}

	@Test
	void findVendorDetailsForReturnsExpected() {
		Optional<UpcVendorDetailsModel> result = service.findDefaultVendorDetailsFor(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void productHasDefaultVendorReturnsExpected() {
		boolean result = service.productHasDefaultVendor(1L);

		assertThat(result, is(false));
	}

	@Test
	void findProductsForVendorReturnsExpected() {
		UpcVendorDetailsModel model = new UpcVendorDetailsModel(new UpcMasterModel());

		when(mockUpcVendorDetailsRepository.findByVendorMasterVendorMasterId(1L)).thenReturn(List.of(model));

		List<SimpleUpcDTO> result = service.findProductsForVendor(1L);

		assertThat(result, is(notNullValue()));
	}
}
