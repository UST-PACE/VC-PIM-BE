package com.ust.retail.store.pim.dto.catalog;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
public class StoreNumberDTO extends BaseFilterDTO {
	@Null(message = "Store Number ID should not be present when creating.", groups = {OnCreate.class})
	@NotNull(message = "Store Number ID is mandatory when updating.", groups = {OnUpdate.class})
	private Long storeNumId;

	@NotNull(message = "Store Name is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	@Size(min = 3, max = 75, message = "Store Name must have between 3 and 75 characters", groups = {OnCreate.class, OnUpdate.class})
	private String storeName;

	@NotNull(message = "Store Location is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	@Size(min = 3, max = 50, message = "Store Location must have between 3 and 50 characters", groups = {OnCreate.class, OnUpdate.class})
	private String location;

	@NotNull(message = "Store Address is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	@Size(min = 3, max = 150, message = "Store Address must have between 3 and 150 characters", groups = {OnCreate.class, OnUpdate.class})
	private String address;

	@NotNull(message = "Store Zipcode is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	@Size(min = 3, max = 10, message = "Store Zipcode must have between 3 and 10 characters", groups = {OnCreate.class, OnUpdate.class})
	private String zipcode;

	@NotNull(message = "Store Contact Number is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	@Size(min = 3, max = 20, message = "Contact Number must have between 3 and 20 characters", groups = {OnCreate.class, OnUpdate.class})
	private String contactNumber;


	public StoreNumberDTO(Long storeNumId, String storeName) {
		this.storeNumId = storeNumId;
		this.storeName = storeName;
	}

	public StoreNumberDTO(Long storeNumId, String storeName, String location, String address, String zipcode, String contactNumber) {
		this.storeNumId = storeNumId;
		this.storeName = storeName;
		this.location = location;
		this.address = address;
		this.zipcode = zipcode;
		this.contactNumber = contactNumber;
	}

	public StoreNumberModel createModel() {
		return new StoreNumberModel(this.storeNumId,
				this.storeName,
				this.location,
				this.address,
				this.zipcode,
				this.contactNumber);
	}

	public StoreNumberDTO parseToDTO(StoreNumberModel model) {

		this.storeNumId = model.getStoreNumId();
		this.storeName = model.getStoreName();
		this.location = model.getLocation();
		this.address = model.getAddress();
		this.zipcode = model.getZipcode();
		this.contactNumber = model.getContactNumber();

		return this;
	}
}
