package com.ust.retail.store.pim.dto.external.store;

import java.util.List;
import java.util.stream.Collectors;

import com.ust.retail.store.pim.model.catalog.StoreNumberModel;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ExternalStoreDTO {
	private Long storeId;
	private String storeName;
	private String location;
	private String address;
	private String zipcode;
	private String contactNumber;
	private List<ExternalStoreLocationDTO> storeLocationList;

	public ExternalStoreDTO parseToDTO(StoreNumberModel model) {
		this.storeId = model.getStoreNumId();
		this.storeName = model.getStoreName();
		this.location = model.getLocation();
		this.address = model.getAddress();
		this.zipcode = model.getZipcode();
		this.contactNumber = model.getContactNumber();
		this.storeLocationList = model.getStoreLocations().stream()
				.map(sl -> new ExternalStoreLocationDTO().parseToDTO(sl))
				.collect(Collectors.toUnmodifiableList());

		return this;
	}
}
