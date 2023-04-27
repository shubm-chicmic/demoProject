package com.example.demoProject.Service;

import com.example.demoProject.Repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersRolesService {
    @Autowired
    UserRoleRepository userRoleRepository;

    public int findIdByRole(String role) {
        return userRoleRepository.findIdByRoles(role);
    }
    public String findRoleById(int id) {

        return userRoleRepository.findRolesById(id);
    }

}
