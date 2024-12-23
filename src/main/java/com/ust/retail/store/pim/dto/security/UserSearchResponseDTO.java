package com.ust.retail.store.pim.dto.security;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ust.retail.store.pim.util.deserializers.AmericanFormatDateDeserializer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSearchResponseDTO {

	private Long userId;
	private String userName;
	private String nameDesc;
	private Long statusId;
	private String statusDesc;
	private Long storeLocationId;
	private String storeLocationDesc;
	
	@JsonDeserialize(using = AmericanFormatDateDeserializer.class)
	private Date updatedAt;

	public UserSearchResponseDTO(Long userId, String userName, String nameDesc) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.nameDesc = nameDesc;
	}

	
}
