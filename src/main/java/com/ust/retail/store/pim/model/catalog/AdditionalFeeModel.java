package com.ust.retail.store.pim.model.catalog;

import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "additional_fee", uniqueConstraints = @UniqueConstraint(name = "uq_fee_name", columnNames = {"fee_name"}))
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class AdditionalFeeModel extends Audits {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "additional_fee_id")
	private Long additionalFeeId;

	@Column(name = "fee_name", nullable = false, length = 50)
	private String feeName;

	public AdditionalFeeModel(Long additionalFeeId) {
		this.additionalFeeId = additionalFeeId;
	}

	public AdditionalFeeModel(Long additionalFeeId, String feeName, Long userId) {
		this.additionalFeeId = additionalFeeId;
		this.feeName = feeName;
		this.userCreate = new UserModel(userId);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof AdditionalFeeModel)) return false;

		AdditionalFeeModel that = (AdditionalFeeModel) o;

		return new EqualsBuilder()
				.append(additionalFeeId, that.additionalFeeId)
				.append(feeName, that.feeName)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(additionalFeeId)
				.append(feeName)
				.toHashCode();
	}
}
