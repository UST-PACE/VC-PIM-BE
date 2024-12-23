package com.ust.retail.store.bistro.model.recipes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Entity
@Table(
        name = "drinks_conf_master_detail_flavours",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_drinks_conf_master_detail_flavour",
                        columnNames = {"drink_conf_master_id", "drink_flavour_id"})
        })
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DrinkConfMasterDetailFlavourModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drink_conf_master_detail_flavour_id")
    private Long drinkConfMasterDetailFlavourId;

    @OneToOne
    @JoinColumn(name = "drink_flavour_id", referencedColumnName = "drink_flavour_id")
    private DrinkFlavourModel drinkFlavour;

    @ManyToOne
    @JoinColumn(name = "drink_conf_master_id", referencedColumnName = "drink_conf_master_id")
    private DrinkConfMasterModel drinkConfMaster;

    public void setPrice(Double price) {
        this.price = price;
    }

    private Double price;


}
