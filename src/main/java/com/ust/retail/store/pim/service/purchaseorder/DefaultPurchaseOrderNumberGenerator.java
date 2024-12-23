package com.ust.retail.store.pim.service.purchaseorder;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.ust.retail.store.pim.repository.puchaseorder.PurchaseOrderRepository;

@Service
public class DefaultPurchaseOrderNumberGenerator implements PurchaseOrderNumberGenerator {
	private static final String PO_NUMBER_PREFIX = "PIM";
	private final PurchaseOrderRepository purchaseOrderRepository;

	public DefaultPurchaseOrderNumberGenerator(PurchaseOrderRepository purchaseOrderRepository) {
		this.purchaseOrderRepository = purchaseOrderRepository;
	}

	@Override
	public String generateRevision(String currentRevision) {
		Optional<String> revisionOpt = Optional.ofNullable(currentRevision);
		if (revisionOpt.isEmpty()) {
			return this.generateNumber();
		}
		String rev = revisionOpt.get();
		String[] parts = rev.split("-");
		long orderNumber = Long.parseLong(parts[1], 10);
		int revision = Integer.parseInt(parts[2], 10);
		return getFormattedRevision(orderNumber, ++revision);
	}

	@Override
	public String generateNumber() {
		String lastPurchaseOrderNum = purchaseOrderRepository.findLastPurchaseOrderNum().orElse(getFormattedRevision(0,1));
		return getFormattedRevision(Long.parseLong(lastPurchaseOrderNum.split("-")[1], 10) + 1, 1);
	}

	@Override
	public boolean isFirstRevision(String revisionString) {
		String revision = Optional.ofNullable(revisionString)
				.orElse(getFormattedRevision(0, 0));
		return Integer.parseInt(revision.split("-")[2], 10) == 1;
	}

	private String getFormattedRevision(long orderNumber, int revision) {
		return String.format("%s-%s-%s",
				PO_NUMBER_PREFIX,
				StringUtils.leftPad(String.valueOf(orderNumber), 10, "0"),
				StringUtils.leftPad(String.valueOf(revision), 3, "0")
		);
	}
}
