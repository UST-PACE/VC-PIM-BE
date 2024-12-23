package com.ust.retail.store.pim.dto.security;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.model.security.ScreenPrivilegeModel;

import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class ScreenPrivilegeDTO {

	private Long privilegeId;

	@NotNull(message = "The user Id parameter is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long userId;

	@NotNull(message = "The role id parameter is mandatory.", groups = { OnCreate.class, OnUpdate.class })
	private Long roleId;

	private Integer authorizatorSequence;
	
	
	private String role;
	public ScreenPrivilegeModel parseToModel() {
		return null;//new ScreenPrivilegeModel(new UserModel(userId), new RoleModel(roleId));
	}

	public ScreenPrivilegeModel parseToModel(ScreenPrivilegeModel screenPrivilege) {
		screenPrivilege.getRole().setRoleId(roleId);
		return screenPrivilege;
	}

	public ScreenPrivilegeDTO parseToDTO(ScreenPrivilegeModel screenPrivilege) {
		this.setPrivilegeId(screenPrivilege.getPrivilegeId());
//		this.setUserId(screenPrivilege.getUser().getUserId());
		this.setRoleId(screenPrivilege.getRole().getRoleId());
		this.setRole(screenPrivilege.getRole().getRoleName());
		return this;
	}

	public ScreenPrivilegeModel parseListPrivilegeCreate(ScreenPrivilegeDTO screenPrivilege) {
		return null;
//		new ScreenPrivilegeModel(new UserModel(screenPrivilege.getUserId()),
//				new RoleModel(screenPrivilege.getRoleId()));
	}

}
