package com.example.demoProject.Controller.MevronController;

import com.example.demoProject.Models.Users;
import com.example.demoProject.Service.UserService;
import com.example.demoProject.Service.VehicleService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Controller
@Slf4j
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    VehicleService vehicleService;
    @RequestMapping(value = {"", "/", "home"})
    public String homePage(Model model, HttpServletRequest request) {

        model.addAttribute("index", "Mevron - HomePage");
        model.addAttribute("isEmailNull", "false");
        model.addAttribute("email", request.getParameter("email"));
        Object token = request.getSession().getAttribute("Authorization");
        System.out.println("\u001B[31m" + token + "\u001B[0m");
        String email = "";
        if(token != null) {
            email = userService.getToken(token.toString()).getEmail();
            System.out.println("\u001B[31m" + "email= " + email + "\u001B[0m");

        }
        model.addAttribute("user", userService.getUserByEmail(email));
        Cookie[] cookies = request.getCookies();
        String role = "role";
        if(cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (role.equals(cookie.getName())) {
                    role = cookie.getValue();
                    break;
                }
            }
        }
        System.out.println("\u001B[31m" + "ROLE = " + role + "\u001B[0m");
        model.addAttribute("error", request.getParameter("error"));
        model.addAttribute("role", role);
        return "index";
    }

    @PostMapping("/hello")
    @ResponseBody
    public String helloWorld(@RequestBody HashMap<String,String> mp) {
        return "Hello World";
    }

    @RequestMapping("/driverDocumentation")
    public String driverDocumentation(){
        return "driver-documentation";
    }


    @PreAuthorize("hasRole('RIDER')")
    @RequestMapping("/bookRide")
    public String bookRide(Principal principal, Model model, HttpServletRequest request ) {
        String role = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("role")) {
                    role = cookie.getValue();
                }
            }
        }
        log.info("\u001B[33m" + "role = " + role +  "\u001B[0m");
        log.info("\u001B[33m" + "principal = " + principal +  "\u001B[0m");

        if(principal == null || !role.equals("RIDER")) {
            log.info("\u001B[33m" + "role = " + role + "\u001B[0m");

            return "redirect:/?error=Please Login As Rider";
        }
        Users users = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", users);

        model.addAttribute("vehicleList", vehicleService.findAll());
        return "book-ride";
    }
    @ExceptionHandler(AccessDeniedException.class)
    public void handleError(HttpServletResponse response) throws IOException {
        response.sendRedirect("/");
    }


}
