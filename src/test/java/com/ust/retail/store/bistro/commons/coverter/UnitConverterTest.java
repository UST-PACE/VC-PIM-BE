package com.ust.retail.store.bistro.commons.coverter;

import java.util.List;

import com.ust.retail.store.common.dto.UnitOfMeasureDTO;
import com.ust.retail.store.common.service.UnitOfMeasureService;
import com.ust.retail.store.common.util.UnitConverter;
import com.ust.retail.store.pim.dto.catalog.CatalogDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.bistro.exception.UnitConvertException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitConverterTest {

	@Mock
	private UnitOfMeasureService mockUnitOfMeasureService;
	@InjectMocks
	private UnitConverter unitConverter;

	@Test
	void getValidUnitsForReturnsExpected() {
		when(mockUnitOfMeasureService.findConvertableCatalogUnitsFor(105L)).thenReturn(List.of(new CatalogDTO(), new CatalogDTO(), new CatalogDTO()));
		List<CatalogDTO> result = unitConverter.findConvertableUnitsFor(105L);

		assertThat(result, hasSize(3));
	}

	@Test
	void convertReturnsExpectedWhenSameUnit() {
		Double result = unitConverter.convert(1L, 1L, 1d);

		assertThat(result, is(1d));
	}

	@Test
	void convertReturnsExpectedWhenValueIsNull() {
		Double result = unitConverter.convert(1L, 1L, null);

		assertThat(result, is(0d));
	}

	@Test
	void convertThrowsExceptionWhenFromUnitNotConvertable() {
		assertThrows(UnitConvertException.class, () -> unitConverter.convert(129L, 109L, 1d));
	}

	@Test
	void convertThrowsExceptionWhenToUnitNotConvertable() {
		when(mockUnitOfMeasureService.findConvertableUnitsFor(125L))
				.thenReturn(List.of(
						new UnitOfMeasureDTO(125L, 125L, "g", 26000L, "Weight", false, 1000d),
						new UnitOfMeasureDTO(126L, 126L, "kg", 26000L, "Weight", true, 1d)));
		assertThrows(UnitConvertException.class, () -> unitConverter.convert(125L, 109L, 1d));
	}

	@Test
	void convertReturnsExpected() {
		when(mockUnitOfMeasureService.findConvertableUnitsFor(126L))
				.thenReturn(List.of(
						new UnitOfMeasureDTO(125L, 125L, "g", 26000L, "Weight", false, 1000d),
						new UnitOfMeasureDTO(126L, 126L, "kg", 26000L, "Weight", true, 1d)));
		Double result = unitConverter.convert(126L, 125L, 1d);

		assertThat(result, is(1000d));
	}
}
