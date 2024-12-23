package com.ust.retail.store.bistro.model.recipes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(
        name = "drinks_conf_master_detail_diaries",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_drinks_conf_master_detail_diary",
                        columnNames = {"drink_conf_master_id", "drink_diary_id"})
        })
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DrinkConfMasterDetailDiaryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drink_conf_master_detail_diary_id")
    private Long drinkConfMasterDetailDiaryId;

    @OneToOne
    @JoinColumn(name = "drink_diary_id", referencedColumnName = "drink_diary_id")
    private DrinkDiaryModel drinkDiary;

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
