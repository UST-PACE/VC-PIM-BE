package com.ust.retail.store.pim.dto.vendormaster;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.dto.vendorcredit.VendorCreditDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;
import com.ust.retail.store.pim.model.vendormaster.VendorMasterStoreModel;
import com.ust.retail.store.pim.service.vendormaster.VendorCodeGenerator;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VendorMasterDTO extends BaseFilterDTO {

	@Null(message = "Vendor Master ID should not be present when creating.", groups = { OnCreate.class })
	@NotNull(message = "Vendor Master ID is mandatory when updating.", groups = { OnUpdate.class })
	private Long vendorMasterId;

	@NotNull(message = "Vendor Master Name is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 3, max = 50, message = "Vendor Name must have between 3 and 50 characters", groups = { OnCreate.class,
			OnUpdate.class })
	private String vendorName;

	private String vendorCode;


	@NotNull(message = "Vendor Master Country is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long vendorCountryId;
	
	@NotNull(message = "Vendor Master State is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 2, max = 50, message = "Vendor Master State must have between 3 and 50 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String vendorState;
	
	@NotNull(message = "Vendor Master Address is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 3, max = 100, message = "Vendor Master Address must have between 3 and 100 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String vendorAddress;

	@NotNull(message = "Vendor Master city is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 3, max = 20, message = "Vendor Master city must have between 3 and 20 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String city;

	@NotNull(message = "Vendor Master zipcode is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 3, max = 10, message = "Vendor Master zipcode must have between 3 and 10 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String zipCode;
	
	@NotNull(message = "Vendor Master email is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Email(message = "Vendor Master E mail has an invalid email pattern, please check.", groups = { OnCreate.class,
			OnUpdate.class })
	@Size(min = 3, max = 75, message = "Vendor Master E mail must have between 3 and 75 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String vendorEmail;

	@NotNull(message = "Vendor Master phone is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 3, max = 15, message = "Vendor Master phones must have between 3 and 15 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String vendorPhone;

	@NotNull(message = "Vendor Master distributor is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long distributorId;

	@NotNull(message = "Vendor Master MOQ is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Double moq;
	
	@NotNull(message = "Vendor Master ETA is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Integer eta;
	
	@NotNull(message = "Vendor Master Shipment Dates is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Integer shipmentDay;
	
	@NotNull(message = "Vendor Master Cut Off Date is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Integer cutOffDay;

	@Valid
	@NotNull(message = "Vendor Master contacts cannot be null", groups = { OnCreate.class, OnUpdate.class })
	private List<VendorContactDTO> vendorContacts;
	
	
	@NotNull(message = "Vendor Master Shipping Country is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long shippingCountryId;

	private String shippingCountryName;
	
	@NotNull(message = "Vendor Master Shipping State is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 2, max = 50, message = "Vendor Master Shipping State must have between 3 and 50 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String shippingState;
	
	@NotNull(message = "Vendor Master Shipping Address is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 3, max = 100, message = "Vendor Master Shipping Address must have between 3 and 100 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String shippingAddress;
	
	@NotNull(message = "Vendor Master Shipping city is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 3, max = 20, message = "Vendor Master Shipping city must have between 3 and 20 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String shippingCity;

	@NotNull(message = "Vendor Master Shipping zipcode is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min = 3, max = 10, message = "Vendor Master Shipping zipcode must have between 3 and 10 characters", groups = {
			OnCreate.class, OnUpdate.class })
	private String shippingZipCode;
	
	@NotNull(message = "Vendor Master Payment Terms Days Id is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long paymentTermsDaysId ;
	
	private Integer customPaymentTermsDays;
	
	private VendorCreditDTO vendorCredit;

	private List<StoreNumberDTO> storeNumbers;
	

	public VendorMasterDTO(Long vendorMasterId, String vendorName, String vendorCode) {
		super();
		this.vendorMasterId = vendorMasterId;
		this.vendorName = vendorName;
		this.vendorCode = vendorCode;
	}

	public VendorMasterModel createModel(Long userId, List<VendorMasterStoreModel> stores, VendorCreditDTO vendorCredit, VendorCodeGenerator vendorCodeGenerator) {

		VendorMasterModel vendorMasterModel = new VendorMasterModel(
				vendorMasterId, 
				distributorId, 
				vendorName,
				Optional.ofNullable(vendorCode).orElse(vendorCodeGenerator.generateCode(vendorName)),
				vendorEmail, 
				vendorPhone,
				vendorCountryId,
				vendorState,
				city, 
				zipCode, 
				vendorAddress, 
				shippingCountryId,
				shippingState,
				shippingCity, 
				shippingZipCode, 
				shippingAddress, 
				moq,
				eta,
				shipmentDay,
				cutOffDay,
				stores,
				paymentTermsDaysId,
				customPaymentTermsDays,
				userId);

		for (int i = 0; i < vendorContacts.size(); i++) {
			vendorMasterModel.addContact(vendorContacts.get(i));
		}
		vendorMasterModel.setVendorCredit(Optional.ofNullable(this.vendorCredit).orElse(vendorCredit));
		return vendorMasterModel;
	}

	public VendorMasterDTO parseToDTO(VendorMasterModel model) {

		this.vendorMasterId = model.getVendorMasterId();
		this.distributorId = model.getDistributor().getDistributorId();
		this.vendorName = model.getVendorName();
		this.vendorCode = model.getVendorCode();
		this.vendorEmail = model.getVendorEmail();
		this.vendorPhone = model.getVendorPhone();
		
		this.vendorCountryId = model.getVendorCountry().getCatalogId();
		this.vendorState = model.getVendorState();
		this.city = model.getCity();
		this.zipCode = model.getZipCode();
		this.vendorAddress = model.getVendorAddress();

		this.shippingCountryId = model.getShippingCountry().getCatalogId();
		this.shippingCountryName = model.getShippingCountry().getCatalogOptions();
		this.shippingState = model.getShippingState();
		this.shippingCity = model.getShippingCity();
		this.shippingZipCode = model.getShippingZipCode();
		this.shippingAddress = model.getShippingAddress();
		
		this.paymentTermsDaysId = Optional.ofNullable(model.getPaymentTermsDays()).orElse(new CatalogModel()).getCatalogId();
		this.customPaymentTermsDays = model.getCustomPaymentTermsDays();

		
		this.moq = model.getMoq();
		this.eta = model.getEta();
		this.shipmentDay = model.getShipmentDay();
		this.cutOffDay = model.getCutOffDay();
		this.vendorCredit = new VendorCreditDTO().parseToDTO(model.getVendorCredit());
		
		this.vendorContacts = model.getVendorContacts().stream()
				.map(dto -> new VendorContactDTO(dto.getVendorContactId(), dto.getVendorContactName(), dto.getPhone(),
						dto.getCellPhone(), dto.getEmail(), dto.getVendorType().getCatalogId()))
				.collect(Collectors.toList());

		if (model.getVendorStoreNumbers() != null) {
			this.storeNumbers = model.getVendorStoreNumbers().stream()
					.map(dto -> new StoreNumberDTO().parseToDTO(dto.getPk().getStoreNum()))
					.collect(Collectors.toList());
		}
		
				

		return this;
	}



	
	

}
