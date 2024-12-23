package com.ust.retail.store.pim.dto.upcmaster;

import lombok.Getter;

@Getter
public class DishJsonFileDTO {
    private String sku;
    private String product_name;
    private Long category_id;
    private String category_name;
    private String barcode;
    private String package_color;
    private String type;

    public DishJsonFileDTO(Long sku, String product_name, Long category_id,
                           String category_name, String alias, String servedWith,
                           String principalUpc, String packageColor, String type) {
        this.sku = Long.toString(sku);
        this.product_name = product_name;
        this.category_id = category_id;
        this.category_name = category_name;
        this.barcode = principalUpc;
        this.package_color = packageColor == null?"":packageColor;
        this.type = type;
        if (alias != null && alias != "") {
            this.product_name = alias;
        } else if (servedWith != null && servedWith != "") {
            this.product_name = this.product_name + " " + servedWith;
        }
    }

    public void changeProductName(String testAlias) {
        this.product_name = testAlias;
    }
}
