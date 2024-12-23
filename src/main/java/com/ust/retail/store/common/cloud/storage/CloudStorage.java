package com.ust.retail.store.common.cloud.storage;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ust.retail.store.pim.dto.upcmaster.ImageType;

public interface CloudStorage {
	
	public String uploadProductGroupPicture(Long productGroupId, MultipartFile file) throws IOException ;

	public String uploadProductCategoryPicture(Long productCategoryId, MultipartFile file) throws IOException;

	public String uploadProductSubcategoryPicture(Long productSubcategoryId, MultipartFile file) throws IOException ;

	public String uploadProductItemPicture(Long productItemId, MultipartFile file) throws IOException ;

	public String uploadProductPicture(Long upcMasterId, ImageType imageType, MultipartFile file) throws IOException ;

	public void deletePicture(String pictureUrl) ;

	public String uploadPicture(String fileName, MultipartFile file) throws IOException ;
}
