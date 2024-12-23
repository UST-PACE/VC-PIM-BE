package com.ust.retail.store.pim.repository.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ust.retail.store.pim.dto.security.UserDTO;
import com.ust.retail.store.pim.dto.security.UserSearchResponseDTO;
import com.ust.retail.store.pim.model.security.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
	Optional<UserModel> findByExternalUserId(Long externalId);

	@Query("select new com.ust.retail.store.pim.dto.security.UserSearchResponseDTO("
			+ "u.userId,"
			+ "u.userName,"
			+ "u.nameDesc)"
			+ " from UserModel u "
			+ " where u.nameDesc like %?1%")
	List<UserSearchResponseDTO> findUserAutocomplete(String nameDesc);

	@Query(value = "select new com.ust.retail.store.pim.dto.security.UserDTO("
			+ "u.userId,"
			+ "u.externalUserId,"
			+ "u.userName,"
			+ "u.nameDesc,"
			+ "u.email,"
			+ "u.cellphone,"
			+ "u.phone,"
			+ "u.status.catalogId,"
			+ "u.status.catalogOptions,"
			+ "u.storeNumber.storeNumId,"
			+ "u.storeNumber.storeName,"
			+ "u.updatedAt,"
			+ "u.deleted"
			+ ") "
			+ " from UserModel u "
			+ " where (?1 = null or u.nameDesc like %?1%)"
			+ " and (?2=null or u.userName like %?2%)"
			+ " and (?4=null or u.storeNumber.storeNumId = ?4)"
			+ " and (?3=null or u.status.catalogId = ?3)")
	Page<UserDTO> getUsersByFilters(String nameDesc, String userName, Long statusId, Long storeNumberId, Pageable pageable);


	Optional<UserModel> findByUserName(String userName);

	Optional<UserModel> findByUserNameAndDeletedIsFalse(String userName);

	Optional<UserModel> findByEmail(String email);

	@Query(value = "SELECT u from UserModel u JOIN u.privileges p WHERE u.storeNumber.storeNumId = ?1 and p.role.roleId = ?2")
	List<UserModel> findUsersWithRoleByStoreNumId(Long storeNumberId, Long roleId);

	@Query(value = "SELECT u FROM UserModel u JOIN u.privileges p WHERE u.userId = ?1 AND p.role.roleId = ?2")
	List<UserModel> findUserWithRole(Long userId, Long role);

}
