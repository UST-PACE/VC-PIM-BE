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
@Table(name = "brand_owners")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class BrandOwnerModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "brand_owner_id")
	private Long brandOwnerId;

	@NotBlank
	@Column(name = "brand_owner_name", length = 75, unique = true, nullable = false)
	private String brandOwnerName;


	public BrandOwnerModel(Long brandOwnerId, @NotBlank String brandOwnerName, Long userCreateId) {
		super();
		this.brandOwnerId = brandOwnerId;
		this.brandOwnerName = brandOwnerName;
		super.userCreate = new UserModel(userCreateId);
	}


	public BrandOwnerModel(Long brandOwnerId) {
		this.brandOwnerId = brandOwnerId;
	}
}
