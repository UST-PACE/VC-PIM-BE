package com.ust.retail.store.pim.dto.upcmaster;

import com.ust.retail.store.pim.common.bases.BaseDTO;

import lombok.Getter;

@Getter
public class PictureDTO extends BaseDTO {

	public PictureDTO(byte[] content) {
		convertToBase64(content);
	}
}
