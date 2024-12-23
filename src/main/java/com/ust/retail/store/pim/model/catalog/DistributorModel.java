package com.ust.retail.store.pim.model.catalog;

import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "distributors")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class DistributorModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "distributor_id")
	private Long distributorId;

	@NotBlank
	@Column(name = "distributor_name", length = 75, nullable = false, unique = true)
	private String distributorName;

	public DistributorModel(Long distributorId, @NotBlank String distributorName, Long userCreateId) {
		super();
		this.distributorId = distributorId;
		this.distributorName = distributorName;
		super.userCreate = new UserModel(userCreateId);
	}

	public DistributorModel(Long distributorId) {
		super();
		this.distributorId = distributorId;
	}
}
