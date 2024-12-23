package com.ust.retail.store.pim.service.report;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ust.retail.store.bistro.dto.recipes.LoadUpcAddOnDTO;
import com.ust.retail.store.bistro.repository.recipes.RecipeRepository;
import com.ust.retail.store.bistro.repository.wastage.WastageRepository;
import com.ust.retail.store.bistro.service.recipes.RecipeAddonService;
import com.ust.retail.store.bistro.service.recipes.RecipeService;
import com.ust.retail.store.pim.dto.report.RecipeAddOnCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.RecipeCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.RecipeIngredientCsvDumpDTO;
import com.ust.retail.store.pim.dto.report.UpcCsvDumpDTO;
import com.ust.retail.store.pim.model.inventory.InventoryHistoryModel;
import com.ust.retail.store.pim.model.inventory.PoReceiveDetailModel;
import com.ust.retail.store.pim.model.purchaseorder.PurchaseOrderModel;
import com.ust.retail.store.pim.repository.inventory.InventoryHistoryRepository;
import com.ust.retail.store.pim.repository.inventory.InventoryProductReturnRepository;
import com.ust.retail.store.pim.repository.inventory.PoReceiveDetailRepository;
import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;
import com.ust.retail.store.pim.util.FixtureLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;

import javax.persistence.Tuple;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
	private static FixtureLoader fixtureLoader;
	@Mock
	private PurchaseOrderRepository mockPurchaseOrderRepository;
	@Mock
	private PoReceiveDetailRepository mockPoReceiveDetailRepository;
	@Mock
	private InventoryProductReturnRepository mockInventoryProductReturnRepository;
	@Mock
	private InventoryHistoryRepository mockInventoryHistoryRepository;
	@Mock
	private WastageRepository mockWastageRepository;
	@Mock
	private UpcMasterRepository mockUpcMasterRepository;
	@Mock
	private RecipeRepository mockRecipeRepository;
	@Mock
	private RecipeService mockRecipeService;
	@Mock
	private RecipeAddonService mockRecipeAddonService;

	@InjectMocks
	private ReportService service;

	@BeforeAll
	static void beforeAll() throws Exception {
		fixtureLoader = new FixtureLoader(ReportServiceTest.class);
	}

	@Test
	void getGeneratedPurchaseOrdersReportReturnsExpected() {
		List<PurchaseOrderModel> poList = fixtureLoader.getObject("poList", new PurchaseOrderListReference()).orElse(List.of());

		when(mockPurchaseOrderRepository.getGeneratedPurchaseOrdersReport(any(), any())).thenReturn(poList);

		ByteArrayResource result = service.getGeneratedPurchaseOrdersReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getReceivingReportReturnsExpected() {
		List<PoReceiveDetailModel> receivedList = fixtureLoader.getObject("receivedList", new PoReceivedDetailListReference()).orElse(List.of());

		when(mockPoReceiveDetailRepository.getReceivedReport(any(), any())).thenReturn(receivedList);

		ByteArrayResource result = service.getReceivingReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getReturnsReportReturnsExpected() {
		Object[] objectArray = fixtureLoader.getObject("returnsList", new TupleReference()).orElse(new Object[9]);

		Tuple tuple = mock(Tuple.class);
		when(tuple.get(anyInt(), any())).then(invocation -> getRowValue(objectArray, invocation, 9));

		when(mockInventoryProductReturnRepository.getReturnsReport(any(), any(), any())).thenReturn(List.of(tuple));

		ByteArrayResource result = service.getReturnsReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getInventoryMovementReportReturnsExpected() {
		List<InventoryHistoryModel> inventoryList = fixtureLoader.getObject("inventoryMovementList", new InventoryHistoryListReference()).orElse(List.of());
		when(mockInventoryHistoryRepository.getInventoryMovementReport(any(), any())).thenReturn(inventoryList);

		ByteArrayResource result = service.getInventoryMovementReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getUnsoldProductReportReturnsExpected() {
		List<InventoryHistoryModel> inventoryList = fixtureLoader.getObject("unsoldProductList", new InventoryHistoryListReference()).orElse(List.of());
		when(mockInventoryHistoryRepository.getUnsoldProductReport(any(), any(), any(), any())).thenReturn(inventoryList);

		ByteArrayResource result = service.getUnsoldProductReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getProductionWasteReportReturnsExpected() {
		Object[] row1 = fixtureLoader.getObject("wastageRow1", new TupleReference()).orElse(new Object[9]);
		Object[] row2 = fixtureLoader.getObject("wastageRow2", new TupleReference()).orElse(new Object[9]);

		Tuple tuple1 = mock(Tuple.class);
		Tuple tuple2 = mock(Tuple.class);
		when(tuple1.get(anyInt(), any())).then(invocation -> getRowValue(row1, invocation, 0));
		when(tuple2.get(anyInt(), any())).then(invocation -> getRowValue(row2, invocation, 0));

		when(mockWastageRepository.getProductionWasteReport(any(), any())).thenReturn(List.of(tuple1, tuple2));
		ByteArrayResource result = service.getProductionWasteReport(new Date(), new Date());

		assertThat(result, is(notNullValue()));
	}

	private Object getRowValue(Object[] row, InvocationOnMock invocation, int dateColumnNumber) throws ParseException {
		Integer column = invocation.<Integer>getArgument(0);
		if (column == dateColumnNumber) {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(String.valueOf(row[column]));
		}
		return row[column];
	}

/*
	@Test
	void getUpcCsvDumpForExportReturnsExpected() {
		List<UpcCsvDumpDTO> csvList = fixtureLoader.getObject("upcCsvList", new UpcCsvListReference()).orElse(List.of());
		when(mockUpcMasterRepository.getCsvDumpData(any(), eq(1L), any(), any())).thenReturn(csvList);

		FileSystemResource result = service.getUpcCsvDumpForExport(1L);

		assertThat(result, is(notNullValue()));
	}
*/

	@Test
	void getRecipeCsvDumpForExportReturnsExpected() {
		List<RecipeCsvDumpDTO> csvList = fixtureLoader.getObject("recipeCsvList", new RecipeCsvListReference()).orElse(List.of());
		when(mockRecipeRepository.getRecipeCsvDump()).thenReturn(csvList);

		FileSystemResource result = service.getRecipeCsvDumpForExport();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeIngredientCsvDumpForExportReturnsExpected() {
		List<RecipeIngredientCsvDumpDTO> csvList = fixtureLoader.getObject("recipeIngredientCsvList", new RecipeIngredientCsvListReference()).orElse(List.of());
		when(mockRecipeRepository.getRecipeIngredientCsvDump()).thenReturn(csvList);

		FileSystemResource result = service.getRecipeIngredientCsvDumpForExport();

		assertThat(result, is(notNullValue()));
	}

	@Test
	void getRecipeAddOnCsvDumpForExportReturnsExpected() {
		List<RecipeAddOnCsvDumpDTO> csvList = fixtureLoader.getObject("recipeAddOnCsvList", new RecipeAddOnCsvListReference()).orElse(List.of());
		when(mockRecipeRepository.getRecipeAddOnCsvDump()).thenReturn(csvList);
		when(mockRecipeAddonService.loadUpcAddonInformation(any())).thenReturn(new LoadUpcAddOnDTO());

		FileSystemResource result = service.getRecipeAddOnCsvDumpForExport();

		assertThat(result, is(notNullValue()));
	}

	private static class RecipeAddOnCsvListReference extends TypeReference<List<RecipeAddOnCsvDumpDTO>> {
	}

	private static class RecipeIngredientCsvListReference extends TypeReference<List<RecipeIngredientCsvDumpDTO>> {
	}

	private static class RecipeCsvListReference extends TypeReference<List<RecipeCsvDumpDTO>> {
	}

	private static class UpcCsvListReference extends TypeReference<List<UpcCsvDumpDTO>> {
	}

	private static class PurchaseOrderListReference extends TypeReference<List<PurchaseOrderModel>> {
	}

	private static class PoReceivedDetailListReference extends TypeReference<List<PoReceiveDetailModel>> {
	}

	private static class TupleReference extends TypeReference<Object[]> {
	}

	private static class InventoryHistoryListReference extends TypeReference<List<InventoryHistoryModel>> {
	}
}
