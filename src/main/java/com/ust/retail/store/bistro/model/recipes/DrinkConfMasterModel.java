package com.ust.retail.store.bistro.model.recipes;

import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.model.upcmaster.UpcMasterModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "drinks_conf_master",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_drink_conf_master",
                        columnNames = {"store_num_id", "upc_master_id"})
        })
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DrinkConfMasterModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drink_conf_master_id")
    private Long drinkConfMasterId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upc_master_id", referencedColumnName = "upc_master_id")
    private UpcMasterModel relatedUpcMaster;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_num_id", referencedColumnName = "store_num_id")
    private StoreNumberModel storeNumber;

    public void setEnabledDrinkConf(Boolean enabledDrinkConf) {
        this.enabledDrinkConf = enabledDrinkConf;
    }

    private Boolean enabledDrinkConf;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "drinkConfMaster")
    private List<DrinkConfMasterDetailSizeModel> availableSizes;


    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "drinkConfMaster")
    private List<DrinkConfMasterDetailFlavourModel> availableFlavours;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.PERSIST, mappedBy = "drinkConfMaster")
    private List<DrinkConfMasterDetailDiaryModel> availableDiaries;



    public void addSize(DrinkConfMasterDetailSizeModel masterDetailSize) {
        if(availableSizes == null) {
            availableSizes = new ArrayList<>();
        }
        availableSizes.add(masterDetailSize);
    }


    public void clearSizes() {
        if(availableSizes != null) {
            availableSizes.clear();
        }
    }

    public void addFlavour(DrinkConfMasterDetailFlavourModel masterDetailFlavour) {
        if(availableFlavours == null) {
            availableFlavours = new ArrayList<>();
        }
        availableFlavours.add(masterDetailFlavour);
    }


    public void clearFlavours() {
        if(availableFlavours != null) {
            availableFlavours.clear();
        }
    }

    public void addDiary(DrinkConfMasterDetailDiaryModel drinkConfMasterDetailDiary) {
        if(availableDiaries == null) {
            availableDiaries = new ArrayList<>();
        }
        availableDiaries.add(drinkConfMasterDetailDiary);
    }


    public void clearDiaries() {
        if(availableDiaries != null) {
            availableDiaries.clear();
        }
    }
}
