package com.ust.retail.store.pim.service.vendormaster;

import com.ust.retail.store.pim.repository.vendormaster.VendorMasterRepository;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DefaultVendorCodeGenerator implements VendorCodeGenerator {
	private static final String PO_VENDOR_PREFIX = "PIM-V";
	private final VendorMasterRepository vendorMasterRepository;

	public DefaultVendorCodeGenerator(VendorMasterRepository vendorMasterRepository) {
		this.vendorMasterRepository = vendorMasterRepository;
	}

	@Override
	public String generateCode(String vendorName) {
		String processedName = RegExUtils.removePattern(vendorName, "[\\W]");
		return String.format("%s-%s-%s",
				PO_VENDOR_PREFIX,
				processedName.substring(0, Integer.min(3, processedName.length())).toUpperCase(),
				StringUtils.leftPad(String.valueOf(vendorMasterRepository.count() + 1), 5, "0")
		);
	}

}
