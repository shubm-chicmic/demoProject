package com.example.demoProject.Service;

import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UsersRoles;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class UserServiceImp implements UserDetailsService {
    UserService userService;

    public UserServiceImp(UserService service) {
        this.userService = service;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("email = " + email);
        Users users= userService.getUserByEmail(email);

        System.out.println(users+"////");
        if (email==null || users == null) {
            //log.error("User can not be found in database");
            throw new UsernameNotFoundException("Username not found");
        }
//        }else if(userDetails.getIsSuspend()) {
//            throw new UsernameNotFoundException("User is Suspended");
//   }
//        else if(!userDetails.getIsEmailVerify()) {
//            throw new UsernameNotFoundException("User is Not Verified");
//        }

        Collection<GrantedAuthority> authorites=new ArrayList<>();

        return new User(users.getEmail(), users.getPassword(),  authorites);
    }
}
