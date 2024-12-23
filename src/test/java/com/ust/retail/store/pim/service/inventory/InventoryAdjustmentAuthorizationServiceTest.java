package com.ust.retail.store.pim.service.inventory;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationDetailDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationFilterDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.InventoryAdjustmentAuthorizationFilterResultDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationRequestDTO;
import com.ust.retail.store.pim.dto.inventory.adjustment.authorization.operation.InventoryAdjustmentAuthorizationResultDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.inventory.InventoryAdjustmentModel;
import com.ust.retail.store.pim.repository.inventory.InventoryAdjustmentRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryAdjustmentAuthorizationServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private InventoryAdjustmentRepository mockInventoryAdjustmentRepository;
	@Mock
	private InventoryService mockInventoryService;

	@InjectMocks
	private InventoryAdjustmentAuthorizationService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(InventoryAdjustmentAuthorizationServiceTest.class);
	}

	@Test
	void getInventoryAdjustmentsByFiltersReturnsExpected() {
		InventoryAdjustmentAuthorizationFilterDTO request = new InventoryAdjustmentAuthorizationFilterDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");
		List<InventoryAdjustmentModel> filterResults = fixtureLoader.getObject("filterResults", new InventoryAdjustmentFilterListReference()).orElse(List.of());
		when(mockInventoryAdjustmentRepository.findByFilters(eq(null), eq(null), eq(null), eq(null), eq(null), eq(null), any()))
				.thenReturn(new PageImpl<>(filterResults));

		Page<InventoryAdjustmentAuthorizationFilterResultDTO> result = service.getInventoryAdjustmentsByFilters(request);

		assertThat(result.getContent(), hasSize(1));
	}

	@Test
	void findByIdReturnsExpectedWhenRecordFound() {
		Optional<InventoryAdjustmentModel> adjustment = fixtureLoader.getObject("adjustment", InventoryAdjustmentModel.class);

		when(mockInventoryAdjustmentRepository.findById(1L)).thenReturn(adjustment);
		InventoryAdjustmentAuthorizationDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenRecordNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void authorizeReturnsExpectedWhenAtLeastOneDetailIsPending() {
		InventoryAdjustmentAuthorizationRequestDTO request = fixtureLoader.getObject("request", InventoryAdjustmentAuthorizationRequestDTO.class)
				.orElse(new InventoryAdjustmentAuthorizationRequestDTO());
		List<InventoryAdjustmentAuthorizationDetailDTO> details = fixtureLoader.getObject("detailsWithPending", new InventoryAdjustmentDetailListReference())
				.orElse(List.of());
		Optional<InventoryAdjustmentModel> adjustment = fixtureLoader.getObject("adjustment", InventoryAdjustmentModel.class);
		when(mockInventoryAdjustmentRepository.findAdjustmentDetailWithStatus(eq(1L), any(), any()))
				.thenReturn(details);
		when(mockInventoryAdjustmentRepository.findById(1L)).thenReturn(adjustment);

		InventoryAdjustmentAuthorizationResultDTO result = service.authorize(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void authorizeReturnsExpectedWhenNoDetailIsPending() {
		InventoryAdjustmentAuthorizationRequestDTO request = fixtureLoader.getObject("request", InventoryAdjustmentAuthorizationRequestDTO.class)
				.orElse(new InventoryAdjustmentAuthorizationRequestDTO());
		List<InventoryAdjustmentAuthorizationDetailDTO> details = fixtureLoader.getObject("details", new InventoryAdjustmentDetailListReference())
				.orElse(List.of());
		Optional<InventoryAdjustmentModel> adjustment = fixtureLoader.getObject("adjustment", InventoryAdjustmentModel.class);
		when(mockInventoryAdjustmentRepository.findAdjustmentDetailWithStatus(eq(1L), any(), any()))
				.thenReturn(details);
		when(mockInventoryAdjustmentRepository.findById(1L)).thenReturn(adjustment);

		InventoryAdjustmentAuthorizationResultDTO result = service.authorize(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void declineReturnsExpectedWhenNoDetailIsPending() {
		InventoryAdjustmentAuthorizationRequestDTO request = fixtureLoader.getObject("request", InventoryAdjustmentAuthorizationRequestDTO.class)
				.orElse(new InventoryAdjustmentAuthorizationRequestDTO());
		List<InventoryAdjustmentAuthorizationDetailDTO> details = fixtureLoader.getObject("details", new InventoryAdjustmentDetailListReference())
				.orElse(List.of());
		Optional<InventoryAdjustmentModel> adjustment = fixtureLoader.getObject("adjustment", InventoryAdjustmentModel.class);
		when(mockInventoryAdjustmentRepository.findAdjustmentDetailWithStatus(eq(1L), any(), any()))
				.thenReturn(details);
		when(mockInventoryAdjustmentRepository.findById(1L)).thenReturn(adjustment);

		InventoryAdjustmentAuthorizationResultDTO result = service.decline(request);

		assertThat(result, is(notNullValue()));
	}

	private static class InventoryAdjustmentFilterListReference extends TypeReference<List<InventoryAdjustmentModel>> {
	}

	private static class InventoryAdjustmentDetailListReference extends TypeReference<List<InventoryAdjustmentAuthorizationDetailDTO>> {
	}
}
