package com.ust.retail.store.pim.model.upcmaster;

import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "upc_store_prices",
		uniqueConstraints = {
		@UniqueConstraint(name = "uq_store_cost", columnNames = {"upc_master_id", "store_num_id"})
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class UpcStorePriceModel extends Audits {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "upc_store_price_id")
	private Long upcStorePriceId;

	@Column(name = "sale_price")
	private Double salePrice;

	@ManyToOne
	@JoinColumn(name = "upc_master_id")
	private UpcMasterModel upcMaster;

	@ManyToOne
	@JoinColumn(name = "store_num_id")
	private StoreNumberModel storeNumber;

	public UpcStorePriceModel(Long upcStorePriceId, Long storeNumId, Double salePrice, Long userId) {
		this.upcStorePriceId = upcStorePriceId;
		this.salePrice = salePrice;

		this.storeNumber = new StoreNumberModel(storeNumId);
		this.userCreate = new UserModel(userId);
	}

	public void setUpcMaster(UpcMasterModel upcMaster) {
		this.upcMaster = upcMaster;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UpcStorePriceModel)) return false;
		UpcStorePriceModel that = (UpcStorePriceModel) o;
		Long myUpcMasterId = Optional.ofNullable(upcMaster).map(UpcMasterModel::getUpcMasterId).orElse(null);
		Long myStoreNumId = Optional.ofNullable(storeNumber).map(StoreNumberModel::getStoreNumId).orElse(null);
		Long otherUpcMasterId = Optional.ofNullable(that.upcMaster).map(UpcMasterModel::getUpcMasterId).orElse(null);
		Long otherStoreNumId = Optional.ofNullable(that.storeNumber).map(StoreNumberModel::getStoreNumId).orElse(null);

		return Objects.equals(upcStorePriceId, that.upcStorePriceId) || (Objects.equals(myUpcMasterId, otherUpcMasterId) && Objects.equals(myStoreNumId, otherStoreNumId));
	}

	@Override
	public int hashCode() {
		Long myUpcMasterId = Optional.ofNullable(upcMaster).map(UpcMasterModel::getUpcMasterId).orElse(null);
		Long myStoreNumId = Optional.ofNullable(storeNumber).map(StoreNumberModel::getStoreNumId).orElse(null);
		return Objects.hash(upcStorePriceId, myUpcMasterId, myStoreNumId);
	}

	public void updateSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
}
