package com.ust.retail.store.pim.controller.user;

import com.ust.retail.store.pim.common.annotations.OnRecover;
import com.ust.retail.store.pim.common.bases.BaseController;
import com.ust.retail.store.pim.dto.security.RecoverPasswordDTO;
import com.ust.retail.store.pim.service.recoverpassword.RecoverPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/recoverpassword/p")
@Validated
public class RecoverPasswordController extends BaseController {

	private final RecoverPasswordService recoverPasswordService;

	public RecoverPasswordController(RecoverPasswordService recoverPasswordService) {
		super();
		this.recoverPasswordService = recoverPasswordService;
	}

	@PostMapping("/recoverpassword")
	@Validated(OnRecover.class)
	public ResponseEntity<Void> sendEmailRecoverPassword(@Valid @RequestBody RecoverPasswordDTO recoverPasswordDTO) {
		
		this.recoverPasswordService.sendPasswordResetEmail(recoverPasswordDTO.getUsername());
		
		return ResponseEntity.ok().build();
	}

	@PutMapping("/updatepassword")
	@Validated(OnRecover.class)
	public ResponseEntity<Void> updatePassword(@Valid @RequestBody RecoverPasswordDTO recoverPasswordDTO) {

 	this.recoverPasswordService.changeUserPassword(recoverPasswordDTO.getUsername(), recoverPasswordDTO.getPassword(), recoverPasswordDTO.getToken());
	return ResponseEntity.ok().build();
	}

}
