package com.ust.retail.store.pim.model.catalog;

import com.ust.retail.store.pim.dto.catalog.StoreNumberDTO;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "store_num")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class StoreNumberModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_num_id")
	private Long storeNumId;

	@NotBlank
	@Column(name = "store_name", length = 75, unique = true, nullable = false)
	private String storeName;

	@Column(name = "location", length = 50)
	private String location;

	@Column(name = "address", length = 150)
	private String address;

	@Column(name = "zipcode", length = 10)
	private String zipcode;

	@Column(name = "contact_number", length = 20)
	private String contactNumber;

	@OneToMany(mappedBy = "storeNumber", fetch = FetchType.LAZY)
	private List<StoreLocationModel> storeLocations;

	@OneToMany(mappedBy = "storeNumber", fetch = FetchType.LAZY)
	private List<UserModel> storeManagers;

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	protected Date createdAt;

	@Column(nullable = false, updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	protected Date updatedAt;


	public StoreNumberModel(Long storeNumId,
							String storeName,
							String location,
							String address,
							String zipcode,
							String contactNumber) {
		this.storeNumId = storeNumId;
		this.storeName = storeName;
		this.location = location;
		this.address = address;
		this.zipcode = zipcode;
		this.contactNumber = contactNumber;
	}

	public StoreNumberModel(Long storeNumId) {
		super();
		this.storeNumId = storeNumId;
	}

	public StoreNumberModel updateValues(StoreNumberDTO dto) {
		this.storeName = dto.getStoreName();
		this.location = dto.getLocation();
		this.address = dto.getAddress();
		this.zipcode = dto.getZipcode();
		this.contactNumber = dto.getContactNumber();
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof StoreNumberModel)) return false;

		StoreNumberModel that = (StoreNumberModel) o;

		return new EqualsBuilder().append(storeNumId, that.storeNumId).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(storeNumId).toHashCode();
	}
}
