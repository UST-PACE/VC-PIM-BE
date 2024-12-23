package com.ust.retail.store.pim.dto.connector.esl;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EslItemDto {

	private String sku; // csv name : id // ESL Sample Value : 1 //PIM Name
	private String ean;
	private String itemName; // csv name : name // ESL Sample Value : Apple //PIM Name
	private String unit; // csv name : unit
	private String qrCode; // csv name : QrCode // ESL Sample Value : www.hanshows.com/id= //PIM Name
	private String brand; // csv name : brand // ESL Sample Value : head-shoulders //PIM Name
	private Integer promoFlag; // csv name : promotrigger //PIM Name
	private BigDecimal price1; // csv name : price // esl description : retail price //PIM Name
	private String price2; // csv name : salePrice // esl description : Promotion price //PIM Name
	private Float price4; // csv name : unitprice // esl description : other price //PIM Name
	private Float price5; // csv name : saleunitprice // esl description : market retail price //PIM Name
	private Float price6; // csv name : discount // esl description : Discount price //PIM Name

}
