package com.ust.retail.store.pim.controller.upcmaster;

import com.fasterxml.jackson.databind.JsonNode;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.upcmaster.*;
import com.ust.retail.store.pim.service.productmaster.ProductScreenConfigFacade;
import com.ust.retail.store.pim.service.upcmaster.UpcAliasVerifyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLException;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/alias-verify/p")
@Validated
@Slf4j
public class UpcAliasVerifyController {
    private final ProductScreenConfigFacade productScreenConfigFacade;
    private final UpcAliasVerifyService upcAliasVerifyService;

    @Autowired
    public UpcAliasVerifyController(UpcAliasVerifyService upcAliasVerifyService,
                                    ProductScreenConfigFacade productScreenConfigFacade) {

        super();
        this.productScreenConfigFacade = productScreenConfigFacade;
        this.upcAliasVerifyService = upcAliasVerifyService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @PostMapping("/filter")
    @Validated(OnFilter.class)
    public Page<ListUpcAliasDTO> findByFilters(@Valid @RequestBody UpcMasterAliasFilterDTO dto) {
        return upcAliasVerifyService.listProductsByFilters(dto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @PostMapping("/runAliasGenerateApi")
    public Map runAliasGenerateApi(@Valid @RequestBody UpcMasterAliasDTO dto) throws SSLException {
         return upcAliasVerifyService.runGenerateAliasGeni(dto);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @PostMapping("/runIdentificationTest")
    public ResponseEntity<UpcAliasVerifyDTO> runIdentificationTest(@Valid @RequestBody UpcMasterAliasDTO dto) {
        return upcAliasVerifyService.runDishesGeniValidationTest(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @PutMapping("/update")
    @Validated(OnUpdate.class)
    public UpcMasterAliasDTO update(@Valid @RequestBody UpcMasterAliasDTO upcMasterDTO) {
        return upcAliasVerifyService.updateUpcMaster(upcMasterDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @GetMapping("/find/id/{id}")
    public IUpcMasterAliasDTO findById(@PathVariable(value = "id") Long upcMasterId) {
        return upcAliasVerifyService.findById(upcMasterId);
    }



}
