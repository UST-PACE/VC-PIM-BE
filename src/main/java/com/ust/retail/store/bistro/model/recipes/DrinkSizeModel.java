package com.ust.retail.store.bistro.model.recipes;

import com.ust.retail.store.bistro.dto.recipes.DrinkSizeRequestDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "drink_sizes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DrinkSizeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drink_size_id")
    private Long drinkSizeId;

    @Column(name = "size_name", length = 35, unique = true)
    private String sizeName;

    @Column(name = "ounces")
    private Double ounces;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "catalog_id")
    private CatalogModel status; // ENABLED DISABLED


    public DrinkSizeModel update(DrinkSizeRequestDTO drinkSizeRequest) {
        sizeName = drinkSizeRequest.getSizeName();
        ounces = drinkSizeRequest.getOunces();
        status = new CatalogModel(drinkSizeRequest.getStatusId());
        return this;
    }

}
