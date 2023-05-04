package com.example.demoProject.Configuration;

import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UsersRoles;
import com.example.demoProject.Service.RolesService;
import com.example.demoProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Configuration
@PropertySource("classpath:application.properties")
public class AdminConfig {

    @Autowired
    UserService userService;
    @Autowired
    RolesService rolesService;
    @Bean
    public void createAdmin() {

        String uuid = UUID.randomUUID().toString();
        String adminEmail = "shubham@gmail.com";
        if(userService.getUserByEmail(adminEmail) == null) {

            Users users = Users.builder()

                    .firstName("Shubham")
                    .lastName("Mishra")
                    .email("shubham@gmail.com")
                    .password("1234")
                    .uuid(uuid)
                    .city("Mohali")
                    .phoneNo("9876543210")

                    .imageUrl("/static-admin/assets/img/profile/admin.webp")
                    .build();
            Roles roles = rolesService.findByRoleName("ADMIN");
            UsersRoles usersRoles = UsersRoles.builder()
                    .roles(roles)
                    .users(users)
                    .isEmailVerify(false)
                    .isSuspend(false)
                    .isDelete(false)
                    .build();
            Set<UsersRoles> usersRolesSet = new HashSet<>();
            usersRolesSet.add(usersRoles);
            users.setUsersRoles(usersRolesSet);


            userService.addUser(users);
        }
    }




}
