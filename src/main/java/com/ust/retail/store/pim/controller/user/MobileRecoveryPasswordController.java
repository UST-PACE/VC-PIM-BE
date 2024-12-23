package com.ust.retail.store.pim.controller.user;

import com.ust.retail.store.pim.common.annotations.OnRecover;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.security.RecoverPasswordDTO;
import com.ust.retail.store.pim.service.recoverpassword.MobileRecoverPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/mobile/p/password/")
@Validated
public class MobileRecoveryPasswordController extends BaseController {
	private final MobileRecoverPasswordService mobileRecoverPasswordService;
	
	public MobileRecoveryPasswordController(MobileRecoverPasswordService mobileRecoverPasswordService) {
		super();
		this.mobileRecoverPasswordService = mobileRecoverPasswordService;
	}

	@PostMapping("/recover")
	@Validated(OnRecover.class)
	public ResponseEntity<Void> recoverPassword(@Valid @RequestBody RecoverPasswordDTO recoverPasswordDTO) {
		
		this.mobileRecoverPasswordService.sendPasswordRecoveryEmail(recoverPasswordDTO.getUsername());
		
		return ResponseEntity.ok().build();
	}

	@PutMapping("/recover/change")
	@Validated(OnRecover.class)
	public ResponseEntity<Void> changePassword(@Valid @RequestBody RecoverPasswordDTO recoverPasswordDTO) {

		this.mobileRecoverPasswordService.changeUserPassword(recoverPasswordDTO.getUsername(), recoverPasswordDTO.getPassword(), recoverPasswordDTO.getToken());
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasRole('ROLE_STORE_ASSOCIATE')")
	@PutMapping("/change")
	@Validated(OnRecover.class)
	public ResponseEntity<Void> updatePassword(@Valid @RequestBody RecoverPasswordDTO recoverPasswordDTO) {

		this.mobileRecoverPasswordService.changeUserPassword(recoverPasswordDTO.getPassword());
		return ResponseEntity.ok().build();
	}

}
