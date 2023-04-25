package com.example.demoProject.Repository;

import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Integer> {
    Roles findRolesByUserId(@Param("userId")int userId);

    @Query(
            "Select userId From Roles role Where role.roleId = :roleId"

    )
    List<Integer> findUserIdByRoleId(@Param("roleId") int roleId);
}
