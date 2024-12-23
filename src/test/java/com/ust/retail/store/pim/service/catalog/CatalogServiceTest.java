package com.ust.retail.store.pim.service.catalog;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.repository.catalog.CatalogRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {
	@Mock
	private CatalogRepository mockCatalogRepository;

	@InjectMocks
	private CatalogService service;

	@Test
	void findByCatalogNameReturnsExpected() {
		when(mockCatalogRepository.findByCatalogName("CATALOG")).thenReturn(List.of(new CatalogModel(1L)));
		List<CatalogDTO> result = service.findByCatalogName("CATALOG");

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findCatalogByIdReturnsExpected() {
		when(mockCatalogRepository.findById(1L)).thenReturn(Optional.of(new CatalogModel(1L)));

		CatalogDTO result = service.findCatalogById(1L);

		assertThat(result, is(notNullValue()));
	}

	@Test
	void findCatalogByIdThrowsExceptionWhenCatalogIdNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> service.findCatalogById(1L));
	}
}
