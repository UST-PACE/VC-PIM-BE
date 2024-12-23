package com.ust.retail.store.pim.model.upcmaster;

import com.ust.retail.store.pim.model.catalog.CatalogModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@Table(name = "upc_master_selling_channels",
		uniqueConstraints = {@UniqueConstraint(name = "uq_upc_selling_channel", columnNames = {"upc_master_id", "channel_id"})})
@Getter
@NoArgsConstructor
public class UpcMasterSellingChannelModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "upc_master_selling_channel_id")
	private Long upcMasterSellingChannelId;

	@ManyToOne
	@JoinColumn(name = "upc_master_id")
	private UpcMasterModel upcMaster;

	@ManyToOne
	@JoinColumn(name = "channel_id")
	private CatalogModel channel;

	@Column(nullable = false)
	private boolean enabled;

	public UpcMasterSellingChannelModel(Long upcMasterSellingChannelId, CatalogModel channel, boolean enabled) {
		this.upcMasterSellingChannelId = upcMasterSellingChannelId;
		this.channel = channel;
		this.enabled = enabled;
	}

	public UpcMasterSellingChannelModel setUpcMaster(UpcMasterModel upcMaster) {
		this.upcMaster = upcMaster;

		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof UpcMasterSellingChannelModel)) return false;

		UpcMasterSellingChannelModel that = (UpcMasterSellingChannelModel) o;

		return new EqualsBuilder()
				.append(upcMaster, that.upcMaster)
				.append(channel, that.channel)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(upcMaster)
				.append(channel)
				.toHashCode();
	}
}
