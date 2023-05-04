package com.example.demoProject.Service;

import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UsersRoles;
import com.example.demoProject.Repository.RoleRepository;
import com.example.demoProject.Repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.hibernate.boot.model.process.spi.MetadataBuildingProcess.build;

@Service
public class RolesService {
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    RoleRepository roleRepository;

    public void addRoles(String role) {
        Roles roles = Roles.builder().roleName(role).build();

        roleRepository.save(roles);
    }

    public Roles findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);

    }
    public List<Roles> findRoleNameByUserId(Users users) {
//        List<UsersRoles> usersRoles = userRoleRepository.findByUserId(userId);
       return userRoleRepository.findByUsers(users);
    }

}
