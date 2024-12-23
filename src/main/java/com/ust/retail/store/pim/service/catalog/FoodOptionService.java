package com.ust.retail.store.pim.service.catalog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.amazonaws.services.kafkaconnect.model.ScaleOutPolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.common.AuthenticationFacade;
import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.dto.catalog.FoodOptionDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcMasterFilterDTO;
import com.ust.retail.store.pim.exceptions.FoodOptionException;
import com.ust.retail.store.pim.exceptions.ResourceNotFoundException;
import com.ust.retail.store.pim.model.catalog.FoodOptionModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import com.ust.retail.store.pim.repository.catalog.FoodOptionRepository;
import com.ust.retail.store.pim.repository.upcmaster.UpcMasterRepository;

@Service
@Slf4j
public class FoodOptionService {

	private final AuthenticationFacade authenticationFacade;

	private final FoodOptionRepository foodOptionRepository;

	private final UpcMasterRepository upcMasterRepository;

	@Autowired
	public FoodOptionService(
			AuthenticationFacade authenticationFacade, 
			FoodOptionRepository foodOptionRepository,
			UpcMasterRepository upcMasterRepository) {
		this.authenticationFacade = authenticationFacade;
		this.foodOptionRepository = foodOptionRepository;
		this.upcMasterRepository = upcMasterRepository;
	}

	public FoodOptionDTO saveOrUpdate(FoodOptionDTO foodOptionDto) {

		FoodOptionModel foodOptionModel;
		List<Long> catalogueIds = foodOptionDto.getFoodOptionCatalogueUpcs().stream().map(UpcMasterDTO::getUpcMasterId).collect(Collectors.toList());
		List<Long> extractedUpcIds = new ArrayList<>(catalogueIds);

		if (foodOptionRepository.existsByCatalogueName(foodOptionDto.getFoodOptionCatalogueName(),foodOptionDto.getFoodOptionId()) != null) {
			throw new DataIntegrityViolationException("This catalouge name is already on the system");
		}
		
		if (foodOptionDto.getFoodOptionId() != null) {
			foodOptionModel = foodOptionRepository.findById(foodOptionDto.getFoodOptionId())
					.orElseThrow(() -> new ResourceNotFoundException("FoodOption", "id", foodOptionDto.getFoodOptionId()));

			extractedUpcIds.removeAll(foodOptionModel.getFoodOptionCatalogueUpcs().stream().map(UpcMasterModel::getUpcMasterId).collect(Collectors.toList()));
			foodOptionModel.getFoodOptionCatalogueUpcs().forEach(c -> c.setFoodOption(null));
		}
		foodOptionModel = foodOptionDto.createModel(this.authenticationFacade.getCurrentUserId());
		
		if (!foodOptionRepository.findMatchingFoodOptions(foodOptionDto.getFoodOptionId(), catalogueIds,
				Long.valueOf(catalogueIds.size()), extractedUpcIds).isEmpty()) {
			throw new FoodOptionException("This option is already on this catalog");
		}
		
		foodOptionModel.setCatalogueUpcs(upcMasterRepository.findAllById(catalogueIds));
		return new FoodOptionDTO(foodOptionRepository.saveAndFlush(foodOptionModel));
	}

	public FoodOptionDTO findById(long foodOptionId) {
		return foodOptionRepository.findById(foodOptionId).map(m -> new FoodOptionDTO(m))
				.orElseThrow(() -> new ResourceNotFoundException("Food Option", "id", foodOptionId));
	}

	public Page<FoodOptionDTO> getFoodOptionByFilters(FoodOptionDTO foodOptionDto) {
		return foodOptionRepository.findByFilters(foodOptionDto.getFoodOptionCatalogueName(),
				foodOptionDto.getFoodOptionStatusId(), foodOptionDto.createPageable()).map(m -> new FoodOptionDTO(m));
	}

	public List<UpcMasterFilterDTO> load(UpcMasterFilterDTO upcMasterFilterDTO) {
		return upcMasterRepository.loadProducts(upcMasterFilterDTO.getProductName(), upcMasterFilterDTO.createSimplePageable());
	}

	public List<FoodOptionDTO> getAutocomplete(@Valid String itemName) {
		return foodOptionRepository.getAutocompleteList(itemName);
	}
	
	public GenericResponse deleteById(Long foodOptionId) {
		FoodOptionModel model = foodOptionRepository.findById(foodOptionId)
				.orElseThrow(() -> new ResourceNotFoundException("FoodOption", "id", foodOptionId));
		model.setDeleted(true);
		model.getFoodOptionCatalogueUpcs().forEach(c -> c.setFoodOption(null));
		foodOptionRepository.saveAndFlush(model);
		return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "true");
	}

	public FileSystemResource getFoodOptionExport() {
		StringBuilder contentBuilder = new StringBuilder();
		LocalDateTime now= LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String currentDate = now.format(formatter);

		contentBuilder.append(String.format("Report Generation Date: ,%s",
						StringUtils.wrap(StringUtils.prependIfMissing(currentDate, "'"), '"')))
				.append(System.lineSeparator())
				.append(System.lineSeparator());

		String[] headers = {
				"Catalog Name",
				"UPCs",
				"Product name",
				"Status"};
		String header = Arrays.stream(headers)
				.map(s -> StringUtils.wrap(s, '"'))
				.collect(Collectors.joining(","));


		contentBuilder.append(header)
				.append(System.lineSeparator());

		String results = getUpcForVcCsvData().stream()
				.map(optionRecord ->
					optionRecord.getFoodOptionCatalogueUpcs().stream().map(upcRecord->
						String.format(StringUtils.repeat("%s", ",", headers.length),
								StringUtils.wrap(optionRecord.getFoodOptionCatalogueName(), '"'),
								StringUtils.wrap(StringUtils.prependIfMissing(upcRecord.getPrincipalUPC(), "'"), '"'),
								StringUtils.wrap(upcRecord.getProductName(),""),
								StringUtils.wrap(optionRecord.getFoodOptionStatusName(), '"')
					)).collect(Collectors.joining(System.lineSeparator())))
							.collect(Collectors.joining(System.lineSeparator()));

		contentBuilder.append(results);

		String fileName = String.format("upc_master_data_%tF.csv", new Date());
		return new FileSystemResource(getWrittenFilePath(fileName, contentBuilder).toFile());
	}
	public String getUpcMasterId(List<UpcMasterDTO> list){
		String upcs = list.stream().map(
				upc-> String.format(StringUtils.repeat("%s", ",",3),
						"",
						"",
						StringUtils.wrap(numberToString(upc.getUpcMasterId()), '"')
				)).collect(Collectors.joining(System.lineSeparator()));
		return upcs;
	}
	public String getProductName(List<UpcMasterDTO> list){
		String upcs = list.stream().map(
				upc-> String.format(StringUtils.repeat("%s", ",", list.size()),
						"",
						"",
						StringUtils.wrap((upc.getProductName()), '"'),
						StringUtils.wrap(numberToString(upc.getUpcMasterId()), '"')
				)).collect(Collectors.joining(System.lineSeparator()));
		System.out.println("\r\n"+upcs);
		return upcs;
	}


	public List<FoodOptionDTO> getUpcForVcCsvData() {
		return foodOptionRepository.findAll().stream()
				.map(m -> new FoodOptionDTO().parseToAllDTO(m))
				.collect(Collectors.toList());
	}

	private Path getWrittenFilePath(String fileName, StringBuilder contentBuilder) {
		Path path = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
		try {
			Files.writeString(path, contentBuilder.toString());
		} catch (IOException e) {
			log.error("Error while writing file", e);
		}
		return path;
	}

	private String numberToString(Number number) {
		return Objects.isNull(number)? "": String.valueOf(number);
	}

}