package com.example.demoProject.Controller;

import com.example.demoProject.Dto.EmailDto;

import com.example.demoProject.Models.Users;
import com.example.demoProject.Service.RolesService;
import com.example.demoProject.Service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/public")
public class UserController {


    @Autowired
    RolesService rolesService;
    @Autowired
    EmailDto emailDto;

    @Autowired
    UserService userService;

    @RequestMapping("/driverRegister")
    public String driverRegister(User user) {
        System.out.println(user + "########################");
        return "signup-driver";
    }

    @RequestMapping("/riderRegister")
    public String riderRegister() {
        System.out.println("///////////////////");
        return "signup-rider";
    }
    @RequestMapping("/driverLogin")
    public String driverLogin() {
        return "driver-login";
    }
    @RequestMapping("/riderLogin")
    public String riderLogin(@ModelAttribute("emailDto") EmailDto emailDto1) {
        return "rider-login";
    }
    @PostMapping("/addRoles")
    @ResponseBody
    public void addRoles(@RequestParam String role) {
        System.out.println(role);
         rolesService.addRoles(role);
    }


    @RequestMapping("/getAllDrivers")
    @ResponseBody
    public List<Users> getAllDrivers() {
        List<Users> UsersList = userService.getAllDrivers();
        return UsersList;
    }


    @RequestMapping("/getAllUsers")
    @ResponseBody
    public List<Users> getAllUsers(
              @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
              @RequestParam(value = "pageSize", defaultValue = "10",required = false) Integer pageSize,
              @RequestParam(value = "search", defaultValue = "" ,required = false) String target,
              @RequestParam(value = "sortBy", defaultValue = "" ,required = false) String sortBy,
              @RequestParam(value = "order", defaultValue = "1" ,required = false) Integer order

    ) {
        System.out.println("\u001B[33m" + pageNumber + " " + pageSize + " " + target + "\u001B[0m");
        List<Users> UsersList = userService.getAllUsers(pageNumber, pageSize, target, sortBy, order);

        return UsersList;
    }

}
