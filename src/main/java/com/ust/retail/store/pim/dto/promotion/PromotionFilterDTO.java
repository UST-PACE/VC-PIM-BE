package com.ust.retail.store.pim.dto.promotion;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.util.deserializers.AmericanFormatDateDeserializer;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PromotionFilterDTO extends BaseFilterDTO {
	private Long brandOwnerId;
	private Long vendorMasterId;
	private String vendorCode;
	private String vendorName;
	private Long productGroupId;
	private Long productCategoryId;
	private Long productSubcategoryId;
	private Long productItemId;
	private Long productMasterId;
	private Long promotionTypeId;
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date expirationDateStart;
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date expirationDateEnd;
}
