package com.ust.retail.store.pim.dto.upcmaster;

import java.util.Date;

import com.ust.retail.store.pim.common.bases.BaseFilterDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpcMasterFilterDTO extends BaseFilterDTO {

	private String principalUpc;
	private Long productTypeId;
	private Long upcMasterTypeId;
	private String upcMasterTypeName;
	private String productName;
	private Long brandOwnerId;

	private Date createdAt;
	private Long upcMasterId;
	private String productTypeDesc;
	private String brandOwnerDesc;

	private Long upcMasterStatusId;

	private String upcMasterStatusDesc;
	
	private boolean vcItem;

	private String coreName;

	private Double recipeNumber;

	private Long upcProductTypeId;

	private String upcProductType;

	private Long productCategoryId;

	private String productCategoryName;


	public UpcMasterFilterDTO(Long upcMasterId,
							  String principalUpc,
							  String productName,
							  Long productTypeId,
							  String upcMasterTypeName,
							  Long brandOwnerId,
							  String productTypeDesc,
							  String brandOwnerDesc,
							  Long upcMasterStatusId,
							  String upcMasterStatusDesc,
							  Date createdAt,
							  boolean vcItem,
							  String coreName,
							  Double recipeNumber,
							  String upcProductType,
							  String productCategoryName) {

		this.upcMasterId = upcMasterId;
		this.principalUpc = principalUpc;
		this.productName = productName;
		this.productTypeId = productTypeId;
		this.upcMasterTypeName = upcMasterTypeName;
		this.brandOwnerId = brandOwnerId;
		this.productTypeDesc = productTypeDesc;
		this.brandOwnerDesc = brandOwnerDesc;
		this.upcMasterStatusId = upcMasterStatusId;
		this.upcMasterStatusDesc = upcMasterStatusDesc;

		this.createdAt = createdAt;
		this.vcItem = vcItem;
		this.coreName=coreName;
		this.recipeNumber=recipeNumber;
		this.upcProductType=upcProductType;
		this.productCategoryName=productCategoryName;

	}
	
	public UpcMasterFilterDTO(String productName, Long upcMasterId) {
		this.productName = productName;
		this.upcMasterId = upcMasterId;
	}
}
