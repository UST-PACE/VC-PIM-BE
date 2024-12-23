package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterSellingChannelModel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpcSellingChannelDTO {
	private Long upcMasterSellingChannelId;
	private Long upcMasterId;
	private Long channelId;
	private String channelName;
	private boolean enabled;

	public UpcSellingChannelDTO modelToDTO(UpcMasterSellingChannelModel model) {
		this.upcMasterSellingChannelId = model.getUpcMasterSellingChannelId();
		this.upcMasterId = model.getUpcMaster().getUpcMasterId();
		this.channelId = model.getChannel().getCatalogId();
		this.channelName = model.getChannel().getCatalogOptions();
		this.enabled = model.isEnabled();

		return this;
	}

	public UpcMasterSellingChannelModel parseToModel(UpcSellingChannelDTO dto) {
		return new UpcMasterSellingChannelModel(
				dto.getUpcMasterSellingChannelId(),
				new CatalogModel(dto.getChannelId()),
				dto.isEnabled());
	}
}
