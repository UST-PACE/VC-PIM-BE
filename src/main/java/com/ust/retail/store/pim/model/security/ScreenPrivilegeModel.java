package com.ust.retail.store.pim.model.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "screen_privileges", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "role_id" }) })
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class ScreenPrivilegeModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "privilege_id")
	private Long privilegeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private UserModel user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", referencedColumnName = "role_id", nullable = false)
	private RoleModel role;
	
	

	public ScreenPrivilegeModel(UserModel user, RoleModel role) {
		super();
		this.user = user;
		this.role = role;
	}

	public ScreenPrivilegeModel(Long privilegeId, UserModel user, RoleModel role) {
		super();
		this.privilegeId = privilegeId;
		this.role = role;
	}

}
