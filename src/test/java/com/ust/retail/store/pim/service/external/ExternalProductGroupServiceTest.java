package com.ust.retail.store.pim.service.external;

import java.util.List;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.dto.security.StoreNumberInfoDTO;
import com.ust.retail.store.pim.repository.catalog.ProductCategoryRepository;
import com.ust.retail.store.pim.repository.catalog.ProductSubcategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.dto.external.ExternalProductGroupDTO;
import com.ust.retail.store.pim.model.catalog.ProductGroupModel;
import com.ust.retail.store.pim.repository.catalog.ProductGroupRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalProductGroupServiceTest {
	@Mock
	private ProductGroupRepository mockProductGroupRepository;
	@Mock
	private AuthenticationFacade mockAuthenticationFacade;
	@Mock
	private ProductCategoryRepository mockProductCategoryRepository;
	@Mock
	private ProductSubcategoryRepository mockProductSubcategoryRepository;

	@InjectMocks
	private ExternalProductGroupService service;

	@Test
	void loadCatalogReturnsExpected() {
		when(mockAuthenticationFacade.getUserStoreNumber()).thenReturn(new StoreNumberInfoDTO(1L, "STORE"));
		when(mockProductGroupRepository.findByProductTypeWithStock(any(), any())).thenReturn(List.of(new ProductGroupModel(1L, "GROUP", true, 1L)));
		List<ExternalProductGroupDTO> result = service.loadCatalog(1L);

		assertThat(result, is(notNullValue()));
	}
}
