package com.ust.retail.store.pim.service.vendormaster;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.repository.vendormaster.VendorMasterRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultVendorCodeGeneratorTest {
	@Mock
	private VendorMasterRepository mockVendorMasterRepository;

	@InjectMocks
	private DefaultVendorCodeGenerator generator;

	@Test
	void generateCodeReturnsExpectedWhenNameHasSpaces() {
		when(mockVendorMasterRepository.count()).thenReturn(0L);

		assertThat(generator.generateCode("GRUPO PEPSI SA A DE CV"), is("PIM-V-GRU-00001"));
	}

	@Test
	void generateCodeReturnsExpectedWhenNameHasNoSpaces() {
		when(mockVendorMasterRepository.count()).thenReturn(0L);

		assertThat(generator.generateCode("FEMSA"), is("PIM-V-FEM-00001"));
	}

	@Test
	void generateCodeReturnsExpectedWhenNameHasMixedCase() {
		when(mockVendorMasterRepository.count()).thenReturn(0L);

		assertThat(generator.generateCode("Long & Spaces"), is("PIM-V-LON-00001"));
	}

	@Test
	void generateCodeReturnsExpectedWhenNameHasMixedCaseAndSpaces() {
		when(mockVendorMasterRepository.count()).thenReturn(0L);

		assertThat(generator.generateCode("Long & Spaces"), is("PIM-V-LON-00001"));
	}

	@Test
	void generateCodeReturnsExpectedWhenNameHasMixedCaseAndSpacesBeforeThirdCharacter() {
		when(mockVendorMasterRepository.count()).thenReturn(0L);

		assertThat(generator.generateCode("Si Fan Chu"), is("PIM-V-SIF-00001"));
	}

	@Test
	void generateCodeReturnsExpectedWhenNameIsTooShort() {
		when(mockVendorMasterRepository.count()).thenReturn(0L);

		assertThat(generator.generateCode("C & A"), is("PIM-V-CA-00001"));
	}

	@Test
	void generateCodeReturnsExpectedWhenNameHasNumbers() {
		when(mockVendorMasterRepository.count()).thenReturn(0L);

		assertThat(generator.generateCode("7-Eleven"), is("PIM-V-7EL-00001"));
	}
}
