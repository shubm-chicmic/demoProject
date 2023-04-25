package com.example.demoProject.Service;

import com.example.demoProject.Models.UsersRoles;
import com.example.demoProject.Repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.hibernate.boot.model.process.spi.MetadataBuildingProcess.build;

@Service
public class RolesService {
    @Autowired
    UserRoleRepository userRoleRepository;

    public void addRoles(String role) {
        UsersRoles usersRoles = UsersRoles.builder()
                .Roles(role).build();

        userRoleRepository.save(usersRoles);
    }
}
