package com.ust.retail.store.pim.dto.vendormaster;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class VendorContactDTO {

	@NotNull(message = "Vendor Master contact is mandatory.", groups = { OnUpdate.class })
	private Long vendorContactId;

	@NotNull(message = "Vendor Master Conctact Name is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min=3,max = 20, message="Verdor Master Contact Name must have between 3 and 50 characters", groups = { OnCreate.class, OnUpdate.class })
	private String vendorContactName;
	
	@NotNull(message = "Vendor Master Conctact Phone is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	@Size(min=3,max = 15, message="Verdor Master Contact Name must have between 3 and 50 characters", groups = { OnCreate.class, OnUpdate.class })
	private String phone;

	private String cellPhone;
	
	@Email(message = "Vendor Mastert Contact E mail has an invalid email pattern, please check.", groups = { OnCreate.class, OnUpdate.class })
	private String email;
	
	@NotNull(message = "Vendor Master contact type is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long vendorTypeId;
}
