package com.ust.retail.store.pim.service.security;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ust.retail.store.pim.model.security.RoleModel;
import com.ust.retail.store.pim.repository.security.RoleRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
	@Mock
	private RoleRepository mockRoleRepository;

	@InjectMocks
	private RoleService service;

	@Test
	void findAllRolesReturnsExpected() {
		when(mockRoleRepository.findAll()).thenReturn(List.of(new RoleModel()));

		List<RoleModel> result = service.findAllRoles();

		assertThat(result, is(notNullValue()));
	}
}
