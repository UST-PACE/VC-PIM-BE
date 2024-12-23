package com.ust.retail.store.pim.service.connector.esl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ust.retail.store.pim.dto.connector.esl.EslDto;
import com.ust.retail.store.pim.dto.connector.esl.EslItemDto;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EslService {

	private final RestTemplate restTemplate;

	private final String eslEndpointUrl;

	private final String qrCode;

	private final boolean eslEnable;

	private final String clientId;

	private final String clientSecret;

	public EslService(RestTemplate restTemplate,
					  @Value("${esl.endpoint.url}") String eslEndpointUrl,
					  @Value("${qrcode.pim.url}") String qrCode,
					  @Value("${esl.url.enabled}") boolean eslEnable,
					  @Value("${esl.client.id}") String clientId,
					  @Value("${esl.client.secret}") String clientSecret) {
		this.restTemplate = restTemplate;
		this.eslEndpointUrl = eslEndpointUrl;
		this.qrCode = qrCode;
		this.eslEnable = eslEnable;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	public void sendUpc(UpcMasterModel upcMasterModel) {
		if (eslEnable) {
			try {
				EslItemDto eslItemDto = new EslItemDto();
				eslItemDto.setSku(upcMasterModel.getPrincipalUpc());
				eslItemDto.setEan(upcMasterModel.getSku());
				eslItemDto.setBrand(upcMasterModel.getBrandOwner().getBrandOwnerName());
				eslItemDto.setPromoFlag(0);
				eslItemDto.setQrCode(qrCode + upcMasterModel.getUpcMasterId());
				eslItemDto.setUnit("1");
				eslItemDto.setItemName(upcMasterModel.getProductName());
				eslItemDto.setPrice1(BigDecimal.valueOf(upcMasterModel.getSalePrice()));

				EslDto eslDto = new EslDto();
				eslDto.setBatchNo("202004091103");
				eslDto.setCustomerStoreCode("ust");
				eslDto.setStoreCode("test");
				eslDto.setItems(List.of(eslItemDto));

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.add("client-id", clientId);
				headers.add("client-secret", clientSecret);
				HttpEntity<EslDto> elsEntity = new HttpEntity<>(eslDto, headers);

				log.info(String.format("Sending UPC to ESL. Product UPC: %s; Product name: %s",
						upcMasterModel.getPrincipalUpc(), upcMasterModel.getProductName()));

				ResponseEntity<Object> eslResponse = restTemplate.exchange(eslEndpointUrl, HttpMethod.POST, elsEntity,
						Object.class);
				if (eslResponse.getStatusCode() == HttpStatus.OK) {
					log.info("Product sent to ESL successfully");
				}

			} catch (Exception e) {
				log.error(String.format("An exception occurred while sending UPC [%s] to ESL",
						upcMasterModel.getPrincipalUpc()), e);
			}
		}

	}
}
