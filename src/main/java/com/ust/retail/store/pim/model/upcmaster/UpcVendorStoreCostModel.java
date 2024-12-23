package com.ust.retail.store.pim.model.upcmaster;

import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.model.general.Audits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "upc_vendor_costs",
		uniqueConstraints = {
				@UniqueConstraint(name = "uq_vendor_store_cost", columnNames = {"upc_vendor_detail_id", "store_num_id"})
		})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class UpcVendorStoreCostModel extends Audits {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "upc_vendor_store_cost_id")
	private Long upcVendorCostId;

	@Column(name = "cost")
	private Double cost;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "upc_vendor_detail_id")
	@Setter
	@Accessors(chain = true)
	private UpcVendorDetailsModel upcVendorDetail;

	@OneToOne
	@JoinColumn(name = "store_num_id")
	@Setter
	@Accessors(chain = true)
	private StoreNumberModel storeNumber;

	public UpcVendorStoreCostModel(Long upcVendorCostId, Long storeNumId, Double cost) {
		this.upcVendorCostId = upcVendorCostId;
		this.upcVendorDetail = new UpcVendorDetailsModel();
		this.storeNumber = new StoreNumberModel(storeNumId);
		this.cost = cost;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UpcVendorStoreCostModel)) return false;
		UpcVendorStoreCostModel that = (UpcVendorStoreCostModel) o;
		Long myUpcVendorDetailId = Optional.ofNullable(upcVendorDetail).map(UpcVendorDetailsModel::getUpcVendorDetailId).orElse(null);
		Long myStoreNumberId = Optional.ofNullable(storeNumber).map(StoreNumberModel::getStoreNumId).orElse(null);
		Long otherUpcVendorDetailId = Optional.ofNullable(that.upcVendorDetail).map(UpcVendorDetailsModel::getUpcVendorDetailId).orElse(null);
		Long otherStoreNumberId = Optional.ofNullable(that.storeNumber).map(StoreNumberModel::getStoreNumId).orElse(null);

		return Objects.equals(upcVendorCostId, that.upcVendorCostId) || (Objects.equals(myUpcVendorDetailId, otherUpcVendorDetailId) && Objects.equals(myStoreNumberId, otherStoreNumberId));
	}

	@Override
	public int hashCode() {
		Long myUpcVendorDetailId = Optional.ofNullable(upcVendorDetail).map(UpcVendorDetailsModel::getUpcVendorDetailId).orElse(null);
		Long myStoreNumberId = Optional.ofNullable(storeNumber).map(StoreNumberModel::getStoreNumId).orElse(null);
		return Objects.hash(myUpcVendorDetailId, myStoreNumberId);
	}
}
