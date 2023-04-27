package com.example.demoProject.Filters;


import com.example.demoProject.Dto.EmailDto;
import com.example.demoProject.Models.UserUuid;
import com.example.demoProject.Service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class Authentication extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final EmailDto emailDto;

    public Authentication(AuthenticationManager authenticationManager, UserService userService, EmailDto emailDto) {
        this.authenticationManager = authenticationManager;

        this.userService = userService;
        this.emailDto = emailDto;
    }

    @Override
    public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("Attempt authentication!!");
        String username = emailDto.getEmail();
        System.out.println(username+"]]]");
        String password = request.getParameter("lpassword");
        System.out.println(password+"]]]]");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        org.springframework.security.core.Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("autho==",authentication);
        return authentication;
        //  return authenticationToken;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
        //User user= (User) authentication.getPrincipal();
        System.out.println("Inside successfulAuthentication ");
        UUID uuid= UUID.randomUUID();
        UserUuid uuidEntity = new UserUuid();
        uuidEntity.setUuid(uuid.toString());
        String username = emailDto.getEmail();
        System.out.println("isnide suce " + username);
        uuidEntity.setEmail(username);
//        uuidEntity.setId(1155);
        userService.CreateToken(uuidEntity);

        // userService.saveUser(user1);
        System.out.println("login successfully!!!");
//        users2 users2 = UserService.getUserByEmail(request.getParameter("email"));
//
//        authority.setAuthority(rolesService.findRolesByUserId(users2.getId()));
       //  new ObjectMapper().writeValue(response.getOutputStream(),"Logged in " + uuid.toString());
      //  processController.processDriverPasswordLogin();
         //  new ObjectMapper().writeValue(response.getOutputStream(),uuid.toString());
        String redirectUrl = "/driverProfile";
//        response.addHeader("Authorization","Subham Kumar");

        request.getSession().setAttribute("Authorization", uuid.toString());

        new DefaultRedirectStrategy().sendRedirect(request,response,redirectUrl);

        System.out.println("FirstFiter " + "1");
        //return "Logged in " + uuid.toString();
    }
}

