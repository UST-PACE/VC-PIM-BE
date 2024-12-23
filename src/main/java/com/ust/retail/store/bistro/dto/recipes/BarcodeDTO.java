package com.ust.retail.store.bistro.dto.recipes;

import com.ust.retail.store.pim.dto.upcmaster.PictureDTO;
import lombok.Getter;

@Getter
public class BarcodeDTO {
	private String barcodeData;

	public BarcodeDTO(byte[] barcodeData) {
		this.barcodeData = new PictureDTO(barcodeData).getPhotoBase64();
	}
}
