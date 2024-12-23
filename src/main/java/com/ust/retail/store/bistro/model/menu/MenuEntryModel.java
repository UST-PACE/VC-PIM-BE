package com.ust.retail.store.bistro.model.menu;

import com.ust.retail.store.bistro.model.recipes.RecipeModel;
import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
		name = "bistro_menu_entries",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "uk_menu_entry_store_recipe",
						columnNames = {"store_num_id", "recipe_id"})
		})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class MenuEntryModel extends Audits implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_entry_id")
	private Long menuEntryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_num_id", referencedColumnName = "store_num_id")
	private StoreNumberModel store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id")
	private RecipeModel recipe;


	@OneToMany(mappedBy = "menuEntry", cascade = CascadeType.ALL, orphanRemoval = true)
	List<MenuEntryDayModel> weekDays;

	public MenuEntryModel(Long menuEntryId, Long storeNumId, Long recipeId, Long userId) {
		this.menuEntryId = menuEntryId;
		this.store = new StoreNumberModel(storeNumId);
		this.recipe = new RecipeModel(recipeId);
		super.userCreate = new UserModel(userId);
	}

	public MenuEntryModel(Long storeNumId, Long recipeId, Long userId) {
		this.store = new StoreNumberModel(storeNumId);
		this.recipe = new RecipeModel(recipeId);
		super.userCreate = new UserModel(userId);
		this.weekDays = new ArrayList<>();
	}

	public void addWeekDay(MenuEntryDayModel weekDay) {
		if (weekDays == null) {
			weekDays = new ArrayList<>();
		}
		weekDay.setMenuEntry(this);
		weekDays.add(weekDay);
	}


	public void clearWeekDays() {
		this.weekDays.clear();
	}

}
