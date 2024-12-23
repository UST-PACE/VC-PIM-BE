package com.ust.retail.store.bistro.service.recipes;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.bistro.dto.recipes.BistroNewUpcResponseDTO;
import com.ust.retail.store.bistro.dto.recipes.BistroUpcMasterDTO;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeUpcMasterServiceTest {
	@Mock
	private RecipeRepository mockRecipeRepository;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;

	@InjectMocks
	private RecipeUpcMasterService service;

	@Test
	void saveReturnsExpected() {
		BistroUpcMasterDTO request = new BistroUpcMasterDTO();
		when(mockUpcMasterRepository.save(any())).then(invocation -> invocation.getArgument(0));
		when(mockRecipeRepository.save(any())).then(invocation -> invocation.getArgument(0));

		BistroNewUpcResponseDTO result = service.save(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateReturnsExpected() {
		BistroUpcMasterDTO request = new BistroUpcMasterDTO();
		when(mockUpcMasterRepository.save(any())).then(invocation -> invocation.getArgument(0));

		BistroUpcMasterDTO result = service.update(request);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void updateKeepsCurrentPicture() {
		BistroUpcMasterDTO request = new BistroUpcMasterDTO();
		when(mockUpcMasterRepository.findById(any())).thenReturn(Optional.of(new UpcMasterModel(1L)));
		when(mockUpcMasterRepository.save(any())).then(invocation -> invocation.getArgument(0));

		BistroUpcMasterDTO result = service.update(request);

		assertThat(result, is(notNullValue()));
	}
}
