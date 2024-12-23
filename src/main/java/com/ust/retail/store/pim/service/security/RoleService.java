package com.ust.retail.store.pim.service.security;

import com.ust.retail.store.pim.model.security.RoleModel;
import com.ust.retail.store.pim.repository.security.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RoleService {
	public static final Long ADMIN_ROLE_ID = 1L;
	public static final Long STORE_MANAGER_ROLE_ID = 2L;

	private final RoleRepository roleRepository;
	private final Map<Long, Long> externalRoleMapping;

	public RoleService(RoleRepository roleRepository,
					   @Value("#{${pim.external.role-map}}") Map<Long, Long> externalRoleMapping) {
		this.roleRepository = roleRepository;
		this.externalRoleMapping = externalRoleMapping;
	}

	public List<RoleModel> findAllRoles() {
		return this.roleRepository.findAll();
	}

	public Optional<Long> getMappedRoleId(Long externalRoleId) {
		return Optional.ofNullable(externalRoleMapping.get(externalRoleId));
	}

}
