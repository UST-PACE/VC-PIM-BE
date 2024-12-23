package com.ust.retail.store.pim.model.upcmaster;

import com.ust.retail.store.pim.model.catalog.AdditionalFeeModel;
import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "upc_additional_fee", uniqueConstraints = @UniqueConstraint(name = "uq_store_upc_fee_label", columnNames = {"store_num_id", "upc_master_id", "additional_fee_id"}))
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class UpcAdditionalFeeModel extends Audits {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "upc_additional_fee_id")
	private Long upcAdditionalFeeId;

	@Column(name = "price")
	private double price;

	@ManyToOne(optional = false)
	@JoinColumn(name = "additional_fee_id", nullable = false)
	private AdditionalFeeModel additionalFee;

	@ManyToOne(optional = false)
	@JoinColumn(name = "store_num_id", nullable = false)
	private StoreNumberModel storeNumber;

	@ManyToOne(optional = false)
	@JoinColumn(name = "upc_master_id", nullable = false)
	private UpcMasterModel upcMaster;

	public UpcAdditionalFeeModel(Long upcAdditionalFeeId,
								 Long storeNumId,
								 Long upcMasterId,
								 Long additionalFeeId,
								 double price,
								 Long userId) {
		this.upcAdditionalFeeId = upcAdditionalFeeId;
		this.price = price;
		this.additionalFee = new AdditionalFeeModel(additionalFeeId);
		this.storeNumber = new StoreNumberModel(storeNumId);
		this.upcMaster = new UpcMasterModel(upcMasterId);
		this.userCreate = new UserModel(userId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof UpcAdditionalFeeModel)) return false;

		UpcAdditionalFeeModel that = (UpcAdditionalFeeModel) o;

		return new EqualsBuilder()
				.append(upcAdditionalFeeId, that.upcAdditionalFeeId)
				.append(storeNumber, that.storeNumber)
				.append(upcMaster, that.upcMaster)
				.append(additionalFee, that.additionalFee)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(upcAdditionalFeeId)
				.append(storeNumber)
				.append(upcMaster)
				.append(additionalFee)
				.toHashCode();
	}
}
