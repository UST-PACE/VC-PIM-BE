package com.ust.retail.store.pim.model.security;

import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.model.catalog.CatalogModel;
import com.ust.retail.store.pim.model.catalog.StoreNumberModel;
import com.ust.retail.store.pim.service.security.RoleService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "external_user_id"})})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class UserModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	List<ScreenPrivilegeModel> privileges;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	@Column(name = "external_user_id", nullable = false)
	private Long externalUserId;
	@Column(name = "user_name", unique = true, length = 30, nullable = false)
	private String userName;
	@Column(name = "password", length = 250, nullable = false)
	private String password;
	@Column(name = "name_desc", length = 80, nullable = false)
	private String nameDesc;
	@Column(name = "email", length = 150, unique = true, nullable = false)
	private String email;
	@Column(name = "cellphone", length = 15)
	private String cellphone;
	@Column(name = "phone", length = 15)
	private String phone;
	@Column(name = "is_approver", nullable = false)
	private boolean approver;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", referencedColumnName = "catalog_id")
	private CatalogModel status;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_num_id", referencedColumnName = "store_num_id")
	private StoreNumberModel storeNumber;
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

	@Column(name = "recovery_code", length = 6, nullable = true)
	private String recoveryCode;

	@Column(name = "login_attempts")
	private Integer loginAttempts;

	@Column(name = "deleted", nullable = false)
	private boolean deleted;

	public UserModel(Long userId) {
		super();
		this.userId = userId;
	}

	public UserModel(Long userId,
					 String userName,
					 String password,
					 String nameDesc,
					 String email,
					 String cellphone,
					 String phone,
					 Long userStatusId,
					 Long storeNumberId,
					 Long externalUserId) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.nameDesc = nameDesc;
		this.email = email;
		this.cellphone = cellphone;
		this.phone = phone;
		this.externalUserId = externalUserId;
		this.status = new CatalogModel(userStatusId);
		this.storeNumber = new StoreNumberModel(storeNumberId);
	}

	public void changePassword(String password) {
		this.password = new BCryptPasswordEncoder().encode(password);
	}

	public void addPrivilege(Long roleId) {
		if (privileges == null) privileges = new ArrayList<>();

		privileges.add(new ScreenPrivilegeModel(this, new RoleModel(roleId)));
	}

	public void clearPrivileges() {
		if (privileges != null) {
			this.privileges.forEach(p -> p.setUser(null));
			this.privileges.clear();
		}
	}

	public void setApprover(boolean approver) {
		this.approver = approver && Optional.ofNullable(getPrivileges()).orElse(List.of()).stream()
				.map(ScreenPrivilegeModel::getRole)
				.anyMatch(role -> Objects.equals(role.getRoleId(), RoleService.STORE_MANAGER_ROLE_ID));
	}

	public void updateValues(UserDTO userDTO) {
		this.userName = userDTO.getUserName();
		this.nameDesc = userDTO.getNameDesc();
		this.email = userDTO.getEmail();
		this.cellphone = userDTO.getCellphone();
		this.phone = userDTO.getPhone();
		this.status = new CatalogModel(userDTO.getStatusId());
		this.storeNumber = new StoreNumberModel(userDTO.getStoreNumberId());
	}
}
