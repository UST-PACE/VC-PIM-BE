package com.ust.retail.store.bistro.model.recipes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(
        name = "drinks_conf_master_detail_sizes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_drink_conf_master_detail_size",
                        columnNames = {"drink_conf_master_id", "drink_size_id"})
        })
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DrinkConfMasterDetailSizeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drink_conf_master_detail_size_id")
    private Long drinkConfMasterDetailSizeId;

    @OneToOne
    @JoinColumn(name = "drink_size_id", referencedColumnName = "drink_size_id")
    private DrinkSizeModel drinkSize;

    @ManyToOne
    @JoinColumn(name = "drink_conf_master_id", referencedColumnName = "drink_conf_master_id")
    private DrinkConfMasterModel drinkConfMaster;

    private Double price;

    @Column(name = "is_default")
    private Boolean isDefault;

    public void update(Double price,  Boolean isDefault ) {
        this.price = price;
        this.isDefault = isDefault;
    }

}
