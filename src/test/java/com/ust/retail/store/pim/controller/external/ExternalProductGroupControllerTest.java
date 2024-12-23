package com.ust.retail.store.pim.controller.external;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.pim.common.external.ExternalApiResponse;
import com.ust.retail.store.pim.dto.external.ExternalProductGroupDTO;
import com.ust.retail.store.pim.service.external.ExternalProductGroupService;
import com.ust.retail.store.pim.util.FixtureLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalProductGroupControllerTest {

	private static FixtureLoader fixtureLoader;

	@Mock
	private ExternalProductGroupService mockExternalProductGroupService;

	@InjectMocks
	private ExternalProductGroupController controller;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ExternalProductGroupControllerTest.class);
	}

	@Test
	void loadCatalogReturnsExpected() {
		List<ExternalProductGroupDTO> externalGroupList = fixtureLoader.getObject("externalGroupList", new ExternalProductGroupListReference()).orElse(List.of());
		when(mockExternalProductGroupService.loadCatalog(any())).thenReturn(externalGroupList);

		ExternalApiResponse<List<ExternalProductGroupDTO>> result = controller.loadCatalog(1L);

		assertThat(result.getResponse(), hasSize(2));
		assertThat(result.getResponse(), contains(List.of(
				allOf(hasProperty("groupId", is(1L)), hasProperty("groupName", is("Group 1"))),
				allOf(hasProperty("groupId", is(2L)), hasProperty("groupName", is("Group 2")))
		)));
	}

	private static class ExternalProductGroupListReference extends TypeReference<List<ExternalProductGroupDTO>> {
	}
}
