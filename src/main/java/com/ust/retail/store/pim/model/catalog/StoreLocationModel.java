package com.ust.retail.store.pim.model.catalog;

import com.ust.retail.store.pim.dto.catalog.StoreLocationDTO;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "store_location", uniqueConstraints = {@UniqueConstraint(columnNames = {"store_num_id", "store_location_name"})})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class StoreLocationModel extends Audits implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final long DEFAULT_STORE_LOCATION = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_location_id")
	private Long storeLocationId;

	@NotBlank
	@Column(name = "store_location_name", length = 75)
	private String storeLocationName;

	@Column(name = "front_sale")
	private boolean frontSale;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_num_id", referencedColumnName = "store_num_id")
	private StoreNumberModel storeNumber;

	public StoreLocationModel(Long storeLocationId, Long storeNumId, String storeLocationName, Long userCreateId) {
		super();
		this.storeLocationId = storeLocationId;
		this.storeNumber = new StoreNumberModel(storeNumId);
		this.storeLocationName = storeLocationName;
		super.userCreate = new UserModel(userCreateId);
		this.frontSale = false;
	}

	public StoreLocationModel(Long storeLocationId) {
		super();
		this.storeLocationId = storeLocationId;
	}

	public StoreLocationModel enableStoreLocationForSales() {
		this.frontSale = true;
		return this;
	}

	public StoreLocationModel updateValues(StoreLocationDTO dto) {
		this.storeNumber = new StoreNumberModel(dto.getStoreNumId());
		this.storeLocationName = dto.getStoreLocationName();
		return this;
	}
}
