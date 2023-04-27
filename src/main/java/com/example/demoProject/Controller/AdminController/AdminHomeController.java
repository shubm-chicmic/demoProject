package com.example.demoProject.Controller.AdminController;


import com.example.demoProject.Dto.Message;
import com.example.demoProject.Service.RolesService;
import com.example.demoProject.Service.UserActivityService;
import com.example.demoProject.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;


@Controller
@RequestMapping("/admin")
public class AdminHomeController {
    @Autowired
    UserService userService;
    @Autowired
    UserActivityService userActivityService;
    @Autowired
    RolesService rolesService;
    @Autowired
    RestTemplate restTemplate;


    @Message("Visited Home Page ")
    @RequestMapping("")
    public String homePage(HttpSession session, Model model) {

//        String uuid = (String) session.getAttribute("Authorization");
        System.out.println("\u001B[31m" + "inside homePage  /////////////////"  + "\u001B[0m");
        userActivityService.deleteAllActivity();

        return "index-admin";
    }
    @Message("Viewed Driver's profile ")
    @RequestMapping("driverOverview")
    public String driverOverview() {
        return "driver-overview";
    }




}