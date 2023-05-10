package com.example.demoProject.Filters;


import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UserUuid;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    private final com.example.demoProject.Service.UserService UserService;

    public AuthorizationFilter(com.example.demoProject.Service.UserService userService) {
        UserService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //filterChain.doFilter(request,response);
         System.out.println("\u001B[33m" + "Authorization -----> " + request.getServletPath() +"\u001B[0m");

        if(request.getServletPath().equals("/profile/RIDER")
         || request.getServletPath().equals("/profile/DRIVER")
                || request.getServletPath().equals("/bookRide")

        ){
            log.info("Else Block {}",request.getServletPath());
//        String AuthorizationHeader = Arrays.stream(request.getCookies())
//                    .filter(c -> c.getName().equals("Authorization"))
//                    .findFirst()
//                    .map(Cookie::getValue)
//                    .orElse(null);
            String AuthorizationHeader = (String)request.getSession().getAttribute("Authorization");
            System.out.println("Authorization header = " + AuthorizationHeader);
            System.out.println(UserService.getToken(AuthorizationHeader));
            UserUuid userUuid = UserService.getToken(AuthorizationHeader);
            //if authorization header is invalid or null!!!
            if(AuthorizationHeader==null||userUuid==null){
                System.out.println("control inside@@@");
                Map<String,String> error=new HashMap<>();
                error.put("error_message","Please provide valid token");
//                token.put("refresh_token",refresh_token);

                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
            System.out.println("vnbsasvansvavsa");

            Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
            // authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));



//            Users user = UserService.getUserByEmail(userUuid.getEmail());
//            Roles role = UserService.findRolesByUserId(user.getId());
            String role = "";
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("role")) {
                        role = cookie.getValue();
                    }
                }
            }

            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.toUpperCase()));


//            System.out.println("\u001B[34m" + userUuid.getEmail() + "\u001B[0m");
            System.out.println("\u001B[42m" + authorities + "\u001B[0m");
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                    new UsernamePasswordAuthenticationToken(userUuid.getEmail(),null,authorities);
            System.out.println("HELLLOOOOOOOOO "+usernamePasswordAuthenticationToken.getName());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }
        filterChain.doFilter(request,response);
        //  System.out.println("AutorizationFiter " + "2");
    }
}
