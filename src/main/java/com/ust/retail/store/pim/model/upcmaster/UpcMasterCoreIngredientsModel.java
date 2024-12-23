package com.ust.retail.store.pim.model.upcmaster;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "upc_master_core_ingredients")
@Getter
@NoArgsConstructor
public class UpcMasterCoreIngredientsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="upc_master_core_ingredients_id")
    private Long upcMasterCoreIngredientsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id")
    private UpcMasterModel upcMasterModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "core_ingredients_id", referencedColumnName = "core_ingredients_id")
    private CoreIngredients coreIngredientsId;

}
