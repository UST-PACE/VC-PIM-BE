package com.ust.retail.store.bistro.model.recipes;

import com.ust.retail.store.bistro.dto.recipes.DrinkDiaryRequestDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "drink_diaries")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class DrinkDiaryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drink_diary_id")
    private Long drinkDiaryId;

    @Column(name = "diary_name", length = 35, unique = true)
    private String diaryName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", referencedColumnName = "catalog_id")
    private CatalogModel status; // ENABLED DISABLED

    public DrinkDiaryModel update(DrinkDiaryRequestDTO diaryRequestDTO) {
        diaryName = diaryRequestDTO.getDiaryName();
        status = new CatalogModel(diaryRequestDTO.getStatusId());
        return this;
    }
}
