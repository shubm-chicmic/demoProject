package com.example.demoProject.Repository;

import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UsersRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.List;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UsersRoles, Integer> {
//      @Query(
//              "Select id From UsersRoles ur Where ur.Roles = :roles"
//      )
//      Integer findIdByRoles(@Param("roles") String roles);
//      @Query(
//              "Select Roles From UsersRoles ur Where ur.id = :id"
//      )
//      String findRolesById(@Param("id") int id);
    Set<UsersRoles> findUsersRolesByUsers(Users users);
    @Query(
            "Select roles From UsersRoles ur where ur.users = :users"
    )
    public List<Roles> findRolesByUsers(@Param("users") Users users);
    long countByRoles(Roles roles);
    public List<UsersRoles> findUsersRolesByisSuspend(Boolean isSuspend);
    public List<UsersRoles> findUsersRolesByisDelete(Boolean isDelete);

}
