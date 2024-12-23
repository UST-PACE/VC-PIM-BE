package com.ust.retail.store.bistro.model.menu;

import com.ust.retail.store.pim.model.general.Audits;
import com.ust.retail.store.pim.model.security.UserModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bistro_menu_entry_days")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class MenuEntryDayModel extends Audits implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_entry_day_id")
	private Long menuEntryDayId;

	private Integer day;

	private Integer start;

	private Integer end;

	@ManyToOne
	@JoinColumn(name = "menu_entry_id")
	@Setter(AccessLevel.PACKAGE)
	private MenuEntryModel menuEntry;

	public MenuEntryDayModel(Long userId) {
		super.userCreate = new UserModel(userId);
	}

	public MenuEntryDayModel(MenuEntryModel menuEntry, Integer day, Integer start, Integer end, Long userCreated) {
		this.menuEntry = menuEntry;
		this.day = day;
		this.start = start;
		this.end = end;
		super.userCreate = new UserModel(userCreated);
	}

	public MenuEntryDayModel copy(Long userId) {
		return new MenuEntryDayModel(menuEntry, day, start, end, userId);
	}

/*
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		MenuEntryDayModel that = (MenuEntryDayModel) o;
		return menuEntryDayId != null && Objects.equals(menuEntryDayId, that.menuEntryDayId);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
*/
}
