package com.example.demoProject.Configuration;

import com.example.demoProject.Models.Users;
import com.example.demoProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class AdminConfig {

    @Autowired
    UserService userService;
    @Bean
    public void createAdmin() {
        String email = "shubham@gmail.com";

       Users users = userService.getUserByEmail(email);
        if(users == null) {
            String password = "1234";

            Users users1 = Users.builder()
                    .firstName("Shubham")
                    .lastName("Mishra")
                    .email(email)
                    .password(password)
                    .city("Mohali")
                    .phoneNo("9876543210")
                    .isEmailVerify(false)
                    .isDelete(false)
                    .isSuspend(false)
                    .imageUrl("/static-admin/assets/img/profile/admin.webp")
                    .build();
            userService.addAdmin(users1);
        }
    }




}
