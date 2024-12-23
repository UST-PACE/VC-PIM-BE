package com.ust.retail.store.pim.model.vendormaster;

import com.ust.retail.store.pim.dto.vendorcredit.VendorCreditDTO;
import com.ust.retail.store.pim.dto.vendormaster.VendorContactDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.catalog.DistributorModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import com.ust.retail.store.pim.model.vendorcredits.VendorCreditModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "vendor_master")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class VendorMasterModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final long VENDOR_PAYMENT_TERM_DAYS_CUSTOM = 25003L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vendor_master_id")
	private Long vendorMasterId;

	@NotBlank
	@Column(name = "vendor_name", length = 50)
	private String vendorName;

	@NotBlank
	@Column(name = "vendor_code", length = 20)
	private String vendorCode;

	@NotBlank
	@Column(name = "vendor_email", length = 75)
	private String vendorEmail;

	@NotBlank
	@Column(name = "vendor_phone", length = 15)
	private String vendorPhone;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_country_id", referencedColumnName = "catalog_id")
	private CatalogModel vendorCountry;

	@Column(name = "vendor_state", length = 50)
	private String vendorState;

	@NotBlank
	@Column(name = "city", length = 20)
	private String city;

	@NotBlank
	@Column(name = "zip_code", length = 10)
	private String zipCode;

	@NotBlank
	@Column(name = "vendor_address", length = 100)
	private String vendorAddress;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "shipping_country_id", referencedColumnName = "catalog_id")
	private CatalogModel shippingCountry;

	@Column(name = "shipping_state", length = 50)
	private String shippingState;

	@NotBlank
	@Column(name = "shipping_city", length = 20)
	private String shippingCity;

	@NotBlank
	@Column(name = "shipping_zip_code", length = 10)
	private String shippingZipCode;

	@NotBlank
	@Column(name = "shipping_address", length = 100)
	private String shippingAddress;

	@Column(name = "moq")
	private Double moq;

	@Column(name = "eta")
	private Integer eta;

	@Column(name = "shipment_day")
	private Integer shipmentDay;

	@Column(name = "cut_off_day")
	private Integer cutOffDay;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "distributor_id")
	private DistributorModel distributor;

	@OneToMany(mappedBy = "vendorMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VendorContactModel> vendorContacts;

	@OneToMany(mappedBy = "pk.vendorMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VendorMasterStoreModel> vendorStoreNumbers;

	@OneToOne(mappedBy = "vendorMaster", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private VendorCreditModel vendorCredit;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_terms_days_id", referencedColumnName = "catalog_id")
	private CatalogModel paymentTermsDays;
	
	@Column(name = "custom_payment_terms_days")
	private Integer customPaymentTermsDays;
	

	public VendorMasterModel(Long vendorMasterId, Long distributorId, String vendorName, String vendorCode,
			String vendorEmail, String vendorPhone, Long vendorCountryId, String vendorState, String city,
			String zipCode, String vendorAddress, Long shippingCountryId, String shippingState, String shippingCity,
			String shippingZipCode, String shippingAddress, Long userCreateId) {
		super();
		this.vendorMasterId = vendorMasterId;
		this.distributor = new DistributorModel(distributorId);
		this.vendorName = vendorName;
		this.vendorCode = vendorCode;
		this.vendorEmail = vendorEmail;
		this.vendorPhone = vendorPhone;

		this.vendorCountry = new CatalogModel(vendorCountryId);
		this.vendorState = vendorState;
		this.city = city;
		this.zipCode = zipCode;
		this.vendorAddress = vendorAddress;

		this.shippingCountry = new CatalogModel(shippingCountryId);
		this.shippingState = shippingState;
		this.shippingCity = shippingCity;
		this.shippingZipCode = shippingZipCode;
		this.shippingAddress = shippingAddress;

		super.userCreate = new UserModel(userCreateId);

	}
	
	public VendorMasterModel(Long vendorMasterId, Long distributorId, String vendorName, String vendorCode,
			String vendorEmail, String vendorPhone, Long vendorCountryId, String vendorState, String city,
			String zipCode, String vendorAddress, Long shippingCountryId, String shippingState, String shippingCity,
			String shippingZipCode, String shippingAddress, Double moq, Integer eta, Integer shipmentDay,
			Integer cutOffDay, List<VendorMasterStoreModel> stores, Long paymentTermsDaysId, Integer customPaymentTermsDays, Long userCreateId) {
		super();
		
		this.vendorMasterId = vendorMasterId;
		this.distributor = new DistributorModel(distributorId);
		this.vendorName = vendorName;
		this.vendorCode = vendorCode;
		this.vendorEmail = vendorEmail;
		this.vendorPhone = vendorPhone;

		this.vendorCountry = new CatalogModel(vendorCountryId);
		this.vendorState = vendorState;
		this.city = city;
		this.zipCode = zipCode;
		this.vendorAddress = vendorAddress;

		this.shippingCountry = new CatalogModel(shippingCountryId);
		this.shippingState = shippingState;
		this.shippingCity = shippingCity;
		this.shippingZipCode = shippingZipCode;
		this.shippingAddress = shippingAddress;
		
		
		this.moq = moq;
		this.eta = eta;
		this.shipmentDay = shipmentDay;
		this.cutOffDay = cutOffDay;
		this.distributor = new DistributorModel(distributorId);
		this.vendorStoreNumbers = stores;
		
		this.paymentTermsDays = new CatalogModel(paymentTermsDaysId);
		
		this.customPaymentTermsDays = Objects.equals(paymentTermsDaysId, VENDOR_PAYMENT_TERM_DAYS_CUSTOM) ? customPaymentTermsDays: null;
		
		super.userCreate = new UserModel(userCreateId);

	}

	public VendorMasterModel(Long vendorMasterId) {
		super();
		this.vendorMasterId = vendorMasterId;
	}

	public void setVendorCredit(VendorCreditDTO vendorCredit) {
		if (Objects.nonNull(vendorCredit)) {
			this.vendorCredit = new VendorCreditModel(vendorCredit.getVendorCreditId(), this.vendorMasterId, vendorCredit.getAvailableCredit());
		}
	}

	public void addContact(VendorContactDTO contact) {

		if (vendorContacts == null)
			vendorContacts = new ArrayList<>();

		vendorContacts.add(new VendorContactModel(contact.getVendorContactId(), contact.getVendorContactName(),
				contact.getPhone(), contact.getCellPhone(), contact.getEmail(), contact.getVendorTypeId(), this));
	}
}
