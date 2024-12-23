package com.ust.retail.store.pim.dto.promotion;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PromotionFilterResultDTO {
	private Long promotionId;
	private String vendorCode;
	private String vendorName;
	private String productGroupName;
	private String productCategoryName;
	private String productSubcategoryName;
	private String productItemName;
	private String productName;
	private String promotionType;
	private Date startDate;
	private Date endDate;
	private Double discount;
	private Date createdAt;
}
