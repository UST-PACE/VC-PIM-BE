package com.ust.retail.store.pim.service.promotion;

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
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.promotion.PromotionDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionFilterDTO;
import com.ust.retail.store.pim.dto.promotion.PromotionFilterResultDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.promotion.PromotionModel;
import com.ust.retail.store.pim.repository.promotion.PromotionRepository;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromotionServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private PromotionRepository mockPromotionRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;
	@InjectMocks
	private PromotionService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(PromotionServiceTest.class);
	}

	@Test
	void saveOrUpdateReturnsExpected() {
		PromotionDTO request = new PromotionDTO();
		when(mockPromotionRepository.save(any())).then(invocation -> invocation.getArgument(0));

		PromotionDTO result = service.saveOrUpdate(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdReturnsExpected() {
		Optional<PromotionModel> promotionModel = fixtureLoader.getObject("promotionModel", PromotionModel.class);
		when(mockPromotionRepository.findById(1L)).thenReturn(promotionModel);

		PromotionDTO result = service.findById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findByIdThrowsExceptionWhenResourceNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
	}

	@Test
	void deleteByIdCompletesSuccessfully() {
		assertDoesNotThrow(() -> service.deleteById(1L));
	}

	@Test
	void getPromotionsByFiltersReturnsExpected() {
		PromotionFilterDTO request = new PromotionFilterDTO();
		request.setPage(1);
		request.setSize(10);
		request.setOrderColumn("id");
		request.setOrderDir("asc");
		List<PromotionFilterResultDTO> results = fixtureLoader.getObject("filterResults", new PromotionFilterResultList()).orElse(List.of());
		when(mockPromotionRepository.findByFilters(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()))
				.thenReturn(new PageImpl<>(results));

		Page<PromotionFilterResultDTO> result = service.getPromotionsByFilters(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findApplicablePromotionForProductReturnsExpected() {
		PromotionModel promotion = fixtureLoader.getObject("promotionModel", PromotionModel.class).orElse(new PromotionModel());
		when(mockPromotionRepository.findActiveProductPromotions(any(), any(), any())).thenReturn(List.of(promotion));

		Optional<PromotionDTO> result = service.findApplicablePromotionForProduct(1L, 1L);

		assertThat(result.isPresent(), is(true));
	}

	private static class PromotionFilterResultList extends TypeReference<List<PromotionFilterResultDTO>> {
	}
}
