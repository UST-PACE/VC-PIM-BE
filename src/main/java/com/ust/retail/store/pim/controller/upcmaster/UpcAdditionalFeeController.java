package com.ust.retail.store.pim.controller.upcmaster;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ust.retail.store.pim.common.GenericResponse;
import com.ust.retail.store.pim.common.annotations.OnCreate;
import com.ust.retail.store.pim.common.annotations.OnFilter;
import com.ust.retail.store.pim.common.annotations.OnUpdate;
import com.ust.retail.store.pim.dto.upcmaster.UpcAdditionalFeeDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcAdditionalFeeFilterDTO;
import com.ust.retail.store.pim.dto.upcmaster.UpcAdditionalFeeFilterResultDTO;
import com.ust.retail.store.pim.service.upcmaster.UpcAdditionalFeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/upcmaster/p/additional-fee")
@RequiredArgsConstructor
@Validated
public class UpcAdditionalFeeController {
    private final UpcAdditionalFeeService upcAdditionalFeeService;

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @PostMapping("/create")
    @Validated(OnCreate.class)
    public UpcAdditionalFeeDTO create(@Valid @RequestBody UpcAdditionalFeeDTO dto) {
        return upcAdditionalFeeService.saveOrUpdate(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @PutMapping("/update")
    @Validated(OnUpdate.class)
    public UpcAdditionalFeeDTO update(@Valid @RequestBody UpcAdditionalFeeDTO dto) {
        return upcAdditionalFeeService.saveOrUpdate(dto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @DeleteMapping("/delete/id/{id}")
    public GenericResponse deleteById(@PathVariable(value = "id") Long upcAdditionalFeeId) {
        upcAdditionalFeeService.deleteById(upcAdditionalFeeId);
        return new GenericResponse(GenericResponse.OP_TYPE_DELETE, GenericResponse.SUCCESS_CODE, "true");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @GetMapping("/find/id/{id}")
    public UpcAdditionalFeeDTO findById(@PathVariable(value = "id") Long upcAdditionalFeeId) {
        return upcAdditionalFeeService.findById(upcAdditionalFeeId);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_STORE_MANAGER') OR hasRole('ROLE_STORE_MANAGER_VC')")
    @PostMapping("/filter")
    @Validated(OnFilter.class)
    public Page<UpcAdditionalFeeFilterResultDTO> findByFilters(@Valid @RequestBody UpcAdditionalFeeFilterDTO dto) {
        return upcAdditionalFeeService.findByFilters(dto);
    }

}
