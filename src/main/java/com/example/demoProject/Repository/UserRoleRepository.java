package com.example.demoProject.Repository;

import com.example.demoProject.Models.UsersRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UsersRoles, Integer> {
      @Query(
              "Select id From UsersRoles ur Where ur.Roles = :roles"
      )
      Integer findIdByRoles(@Param("roles") String roles);
      @Query(
              "Select Roles From UsersRoles ur Where ur.id = :id"
      )
      String findRolesById(@Param("id") int id);
}
