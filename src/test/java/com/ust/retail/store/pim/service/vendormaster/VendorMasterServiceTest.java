package com.ust.retail.store.pim.service.vendormaster;

import java.util.List;
import java.util.Optional;

import com.ust.retail.store.pim.service.upcmaster.UpcVendorDetailsService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterDropdownDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterFilterDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorMasterStoreDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.vendormaster.VendorContactModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;
import com.ust.retail.store.pim.repository.vendorcredits.VendorCreditRepository;
import com.ust.retail.store.pim.repository.vendormaster.VendorMasterRepository;
import com.ust.retail.store.pim.repository.vendormaster.VendorMasterStoreRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class VendorMasterServiceTest {
	private static FixtureLoader fixtureLoader;

	@Mock
	private VendorMasterRepository mockVendorMasterRepository;
	@Mock
	private VendorMasterStoreRepository mockVendorMasterStoreRepository;
	@Mock
	private VendorCodeGenerator mockVendorCodeGenerator;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;
	@Mock
	private VendorCreditRepository mockVendorCreditRepository;
	@Mock
	private UpcVendorDetailsService mockUpcVendorDetailsService;

	@InjectMocks
	private VendorMasterService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(VendorMasterServiceTest.class);
	}

	@Test
	void saveOrUpdateReturnsExpected() {
		VendorMasterDTO request = fixtureLoader.getObject("saveRequest", VendorMasterDTO.class).orElse(new VendorMasterDTO());
		when(mockVendorMasterRepository.save(any())).then(invocation -> invocation.getArgument(0));

		VendorMasterDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void saveOrUpdateReturnsExpectedWhenUpdate() {
		VendorMasterDTO request = fixtureLoader.getObject("updateRequest", VendorMasterDTO.class).orElse(new VendorMasterDTO());
		Optional<VendorMasterModel> vendorMaster = fixtureLoader.getObject("vendorMasterModel", VendorMasterModel.class);
		when(mockVendorMasterRepository.findById(1L)).thenReturn(vendorMaster);
		when(mockVendorMasterRepository.save(any())).then(invocation -> invocation.getArgument(0));

		VendorMasterDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<VendorMasterModel> vendorMaster = fixtureLoader.getObject("vendorMasterModel", VendorMasterModel.class);
		when(mockVendorMasterRepository.findById(1L)).thenReturn(vendorMaster);

		VendorMasterDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenVendorNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void addStoreNumberReturnsExpected() {
		VendorMasterStoreDTO request = new VendorMasterStoreDTO(1L, 1L);
		when(mockVendorMasterStoreRepository.save(any())).then(invocation -> invocation.getArgument(0));

		VendorMasterStoreDTO result = service.addStoreNumber(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void removeStoreNumberCompletesSuccessfully() {
		assertDoesNotThrow(() -> service.removeStoreNumber(1L, 1L));
	}

	@Test
	void getAutoCompleteVendorNameReturnsExpected() {
		List<VendorMasterDTO> result = service.getAutocompleteVendorName("ANY");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getAutoCompleteVendorCodeReturnsExpected() {
		List<VendorMasterDTO> result = service.getAutocompleteVendorCode("ANY");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getVendorMasterByFiltersReturnsExpected() {
		VendorMasterFilterDTO request = new VendorMasterFilterDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");
		VendorMasterModel vendorMaster = fixtureLoader.getObject("vendorMasterModel", VendorMasterModel.class).orElse(new VendorMasterModel());
		when(mockVendorMasterRepository.findByFilters(any(), any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(vendorMaster)));

		Page<VendorMasterFilterDTO> result = service.getVendorMasterByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void loadReturnsExpected() {
		VendorMasterModel vendorMaster = fixtureLoader.getObject("vendorMasterModel", VendorMasterModel.class).orElse(new VendorMasterModel());
		when(mockVendorMasterRepository.findAll()).thenReturn(List.of(vendorMaster));

		List<VendorMasterDropdownDTO> result = service.load();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getVendorSalesRepresentativeReturnsExpected() {
		Optional<VendorMasterModel> vendorMaster = fixtureLoader.getObject("vendorMasterModel", VendorMasterModel.class);
		when(mockVendorMasterRepository.findById(1L)).thenReturn(vendorMaster);

		Optional<VendorContactModel> result = service.getVendorSalesRepresentative(1L);

		assertThat(result.isPresent(), is(true));
	}

	@Test
	void getVendorSalesRepresentativeThrowsExceptionWhenVendorNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.getVendorSalesRepresentative(1L));
	}

	@Test
	void getVendorEscalationContactReturnsExpected() {
		Optional<VendorMasterModel> vendorMaster = fixtureLoader.getObject("vendorMasterModel", VendorMasterModel.class);
		when(mockVendorMasterRepository.findById(1L)).thenReturn(vendorMaster);

		Optional<VendorContactModel> result = service.getVendorEscalationContact(1L);

		assertThat(result.isPresent(), is(true));
	}

	@Test
	void getVendorEscalationContactThrowsExceptionWhenVendorNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.getVendorEscalationContact(1L));
	}
}
