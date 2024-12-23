package com.ust.retail.store.pim.repository.security;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ust.retail.store.pim.model.security.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {

	List<RoleModel> findByRoleNameIn(List<String> roles);

	List<RoleModel> findByRoleModule(String roleModule);

}
