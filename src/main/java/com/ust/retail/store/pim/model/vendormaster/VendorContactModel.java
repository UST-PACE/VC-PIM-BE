package com.ust.retail.store.pim.model.vendormaster;

import com.ust.retail.store.pim.model.catalog.CatalogModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "vendor_contact")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class VendorContactModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vendor_contact_id")
	private Long vendorContactId;

	@NotBlank
	@Column(name = "vendor_contact_name", length = 50)
	private String vendorContactName;
	
	@NotBlank
	@Column(name = "phone", length = 15)
	private String phone;

	@Column(name = "cell_phone", length = 15)
	private String cellPhone;
	
	@NotBlank
	@Column(name = "email", length = 75)
	private String email;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_contact_type_id", referencedColumnName = "catalog_id")
	private CatalogModel vendorType;
	
	@ManyToOne
	@JoinColumn(name = "vendor_master_id")
	private VendorMasterModel vendorMaster;

	public VendorContactModel(Long vendorContactId, @NotBlank String vendorContactName, @NotBlank String phone,
			@NotBlank String cellPhone, @NotBlank String email, Long vendorTypeId,VendorMasterModel vendorMaster) {
		super();
		this.vendorContactId = vendorContactId;
		this.vendorContactName = vendorContactName;
		this.phone = phone;
		this.cellPhone = cellPhone;
		this.email = email;
		this.vendorType = new CatalogModel(vendorTypeId);
		this.vendorMaster = vendorMaster;
	}
	
}
