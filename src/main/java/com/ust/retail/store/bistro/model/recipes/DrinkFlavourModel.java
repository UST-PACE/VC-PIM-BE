package com.ust.retail.store.bistro.model.recipes;

import com.ust.retail.store.bistro.dto.recipes.DrinkFlavourRequestDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "drink_flavours")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DrinkFlavourModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drink_flavour_id")
    private Long drinkFlavourId;

    @Column(name = "flavour_name", length = 35, unique = true)
    private String flavourName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "catalog_id")
    private CatalogModel status; // ENABLED DISABLED

    public DrinkFlavourModel update(DrinkFlavourRequestDTO drinkFlavourRequestDTO) {

        flavourName = drinkFlavourRequestDTO.getFlavourName();
        status = new CatalogModel(drinkFlavourRequestDTO.getStatusId());

        return this;
    }

}
