package com.ust.retail.store.common.cloud.storage;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ust.retail.store.pim.dto.upcmaster.ImageType;

public abstract class CloudStorageOperations {
	
	protected static final String FILE_SUFFIX_FORMAT = "_%07d";
	protected static final String PRODUCT_GROUP_FILE_FORMAT = "pg" + FILE_SUFFIX_FORMAT;
	protected static final String PRODUCT_CATEGORY_FILE_FORMAT = "pc" + FILE_SUFFIX_FORMAT;
	protected static final String PRODUCT_SUBCATEGORY_FILE_FORMAT = "ps" + FILE_SUFFIX_FORMAT;
	protected static final String PRODUCT_ITEM_FILE_FORMAT = "ps" + FILE_SUFFIX_FORMAT;
	protected static final String PRODUCT_FILE_FORMAT = "upc_%s" + FILE_SUFFIX_FORMAT;
	
	public String uploadProductGroupPicture(Long productGroupId, MultipartFile file) throws IOException {
		return uploadPicture(String.format(PRODUCT_GROUP_FILE_FORMAT, productGroupId), file);
	}

	public String uploadProductCategoryPicture(Long productCategoryId, MultipartFile file) throws IOException {
		return uploadPicture(String.format(PRODUCT_CATEGORY_FILE_FORMAT, productCategoryId), file);
	}

	public String uploadProductSubcategoryPicture(Long productSubcategoryId, MultipartFile file) throws IOException {
		return uploadPicture(String.format(PRODUCT_SUBCATEGORY_FILE_FORMAT, productSubcategoryId), file);
	}

	public String uploadProductItemPicture(Long productItemId, MultipartFile file) throws IOException {
		return uploadPicture(String.format(PRODUCT_ITEM_FILE_FORMAT, productItemId), file);
	}

	public String uploadProductPicture(Long upcMasterId, ImageType imageType, MultipartFile file) throws IOException {
		return uploadPicture(String.format(PRODUCT_FILE_FORMAT, imageType.toString().toLowerCase(), upcMasterId), file);
	}
	
	abstract String uploadPicture(String fileName, MultipartFile file) throws IOException;


}
