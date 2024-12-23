package com.ust.retail.store.pim.model.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @ToString
public class RoleModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long roleId;

	@Column(name = "role_name", length = 30, nullable = false)
	private String roleName;

	@Column(name = "role_module", length = 60, nullable = false)
	private String roleModule;

	public RoleModel(Long roleId) {
		super();
		this.roleId = roleId;
	}

}
