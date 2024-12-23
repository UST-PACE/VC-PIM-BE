package com.ust.retail.store.pim.model.vendorcredits;

import com.ust.retail.store.pim.model.vendormaster.VendorMasterModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Entity
@Table(name = "vendor_credits")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class VendorCreditModel implements Serializable {
	
	private static final long serialVersionUID = 1614810648773737396L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vendor_credit_id")
	private Long vendorCreditId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_master_id", referencedColumnName = "vendor_master_id", unique = true)
	private VendorMasterModel vendorMaster;

	@Column(name = "available_credit")
	private Double availableCredit;

	public VendorCreditModel(Long vendorCreditId) {
		this.vendorCreditId = vendorCreditId;
	}

	public VendorCreditModel(Long vendorMasterId, Double availableCredit) {
		this.vendorMaster = new VendorMasterModel(vendorMasterId);
		this.availableCredit = availableCredit;
	}

	public VendorCreditModel(Long vendorCreditId, Long vendorMasterId, Double availableCredit) {
		this.vendorCreditId = vendorCreditId;
		this.vendorMaster = new VendorMasterModel(vendorMasterId);
		this.availableCredit = availableCredit;
	}

	public void updateAvailableCredit(Double credit) {
		this.availableCredit = Optional.ofNullable(this.availableCredit).orElse(0d) + credit;
	}
}
