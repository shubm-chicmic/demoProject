package com.example.demoProject.Service;

import com.example.demoProject.Models.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class UserServiceImp implements UserDetailsService {
    UserService userService;

    public UserServiceImp(UserService service) {
        this.userService = service;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("email = " + email);
        Users userDetails= userService.getUserByEmail(email);

        System.out.println(userDetails+"////");
        if (email==null || userDetails == null) {
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
//
//        users2 user = service.getUserByEmail(email);
//       Roles role = rolesService.findRolesByUserId(user.getId());
//       authorites.add(new SimpleGrantedAuthority("ROLE_"+role.getRole().toUpperCase()));
        System.out.println("////////////////////////");
        System.out.println(authorites);
        return new User(userDetails.getEmail(), userDetails.getPassword(),  authorites);
    }
}
