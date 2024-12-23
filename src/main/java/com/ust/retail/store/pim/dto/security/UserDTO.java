package com.ust.retail.store.pim.dto.security;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ust.retail.store.common.util.validation.constraints.StrongPassword;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnRecover;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.common.bases.BaseFilterDTO;
import com.ust.retail.store.pim.model.security.UserModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
public class UserDTO extends BaseFilterDTO {

	@NotNull(message = "The User Id parameter is mandatory.", groups = {OnUpdate.class, OnRecover.class})
	private Long userId;

	@NotNull(message = "The External User Id parameter is mandatory.", groups = {OnCreate.class})
	private Long externalUserId;

	@Size(max = 30, message = "Not valid user name. Must have minimum 5 chars or maximum 30 chars.", groups = {
			OnCreate.class, OnUpdate.class})
	@NotNull(message = "The user name parameter is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private String userName;

	@NotNull(message = "The Password parameter is mandatory.", groups = {OnCreate.class})
	@Size(min = 8, max = 30, message = "Invalid password. Must have min 8 chars and maximum 30 chars.", groups = {
			OnCreate.class, OnUpdate.class, OnRecover.class})
	@StrongPassword(message = "Password must contain lower and upper-case characters, digits, special symbols.", groups = {
			OnCreate.class, OnRecover.class})
	private String password;

	@Size(max = 80, message = "Not valid name. Must have minimum 5 chars or maximum 80 chars.", groups = {
			OnCreate.class, OnUpdate.class})
	@NotNull(message = "The Name parameter is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private String nameDesc;

	@Email(message = "Invalid email pattern, please check.", groups = {OnCreate.class, OnUpdate.class})
	@Size(max = 150, message = "Not valid email. Must have minimum 5 chars or maximum 150 chars.", groups = {
			OnCreate.class, OnUpdate.class})
	private String email;

	@Size(max = 15, message = "Not valid cellphone. Must have maximum 15 chars.", groups = {OnCreate.class,
			OnUpdate.class})
	private String cellphone;

	@Size(max = 15, message = "Not valid phone. Must have maximum 15 chars.", groups = {OnCreate.class,
			OnUpdate.class})
	private String phone;

	@NotNull(message = "The status id parameter is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long statusId;

	private Date updatedAt;

	@NotNull(message = "The roleId parameter is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long roleId;

	@NotNull(message = "The store number id parameter is mandatory.", groups = {OnCreate.class, OnUpdate.class})
	private Long storeNumberId;

	private boolean approver;

	private String storeNumberDesc;

	private boolean deleted;

	private List<ScreenPrivilegeDTO> screenPrivilegesDTO;

	private String statusDesc;

	public UserDTO(Long userId,
				   Long externalUserId,
				   String userName,
				   String nameDesc,
				   String email,
				   String cellphone,
				   String phone,
				   Long statusId,
				   String statusDesc,
				   Long storeLocationId,
				   String storeNumberDesc,
				   Date updatedAt,
				   boolean deleted) {

		this.userId = userId;
		this.externalUserId = externalUserId;
		this.userName = userName;
		this.nameDesc = nameDesc;
		this.email = email;
		this.cellphone = cellphone;
		this.phone = phone;
		this.updatedAt = updatedAt;
		this.statusId = statusId;
		this.statusDesc = statusDesc;
		this.storeNumberId = storeLocationId;
		this.storeNumberDesc = storeNumberDesc;
		this.deleted = deleted;
	}

	public UserDTO(Long userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public UserModel parseToModel(Long userStatusId) {
		return new UserModel(
				userId,
				userName,
				new BCryptPasswordEncoder().encode(password),
				nameDesc,
				email,
				phone,
				cellphone,
				userStatusId,
				storeNumberId,
				externalUserId);
	}

	public UserDTO parseToDTO(UserModel user) {
		this.userId = user.getUserId();
		this.externalUserId = user.getExternalUserId();
		this.userName = user.getUserName();
		this.nameDesc = user.getNameDesc();
		this.email = user.getEmail();
		this.cellphone = user.getCellphone();
		this.phone = user.getPhone();
		this.approver = user.isApprover();
		this.updatedAt = user.getUpdatedAt();
		this.storeNumberId = user.getStoreNumber().getStoreNumId();
		this.storeNumberDesc = user.getStoreNumber().getStoreName();
		this.deleted = user.isDeleted();

		Optional.ofNullable(user.getStatus()).ifPresent(status -> {
			this.statusId = status.getCatalogId();
			this.statusDesc = status.getCatalogOptions();
		});

		Optional.ofNullable(user.getPrivileges()).ifPresent(privileges ->
			this.screenPrivilegesDTO = user.getPrivileges().stream()
					.map(privilege -> new ScreenPrivilegeDTO().parseToDTO(privilege))
					.collect(Collectors.toList())
		);

		return this;
	}


}
