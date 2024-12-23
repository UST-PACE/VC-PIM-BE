package com.ust.retail.store.pim.model.upcmaster;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "core_ingredients")
@Getter
@NoArgsConstructor
public class CoreIngredients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="core_ingredients_id")
    private Long coreIngredientsId;

    @Column(name="description")
    private String Description;

}
