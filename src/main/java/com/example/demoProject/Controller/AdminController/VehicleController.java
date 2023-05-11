package com.example.demoProject.Controller.AdminController;

import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UsersRoles;
import com.example.demoProject.Models.Vehicle;
import com.example.demoProject.Service.RolesService;
import com.example.demoProject.Service.UserService;
import com.example.demoProject.Service.UsersRolesService;
import com.example.demoProject.Service.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class VehicleController {
    @Autowired
    UserService userService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    RolesService rolesService;
    @GetMapping("/assignVehicle/{userId}/{vehicleId}")
    public String assignVehicle(@PathVariable("userId") Integer userId, @PathVariable("vehicleId") Long vehicleId){

        Users users = userService.getUserById(userId);
        Set<UsersRoles> usersRolesSet = rolesService.findUsersRolesByUsers(users);
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);

        for (UsersRoles usersRoles : usersRolesSet) {
            if(usersRoles.getRoles().getRoleName().equals("DRIVER")){
                usersRoles.setVehicle(vehicle);
            }
        }
        users.setUsersRoles(usersRolesSet);
        userService.addUser(users);
        return "Success";
    }
    @PostMapping("/addVehicle")
    public String addVehicle(HttpServletRequest request) {
        String name = request.getParameter("name");
        double rate = Double.parseDouble(request.getParameter("rate"));
        String imageUrl = request.getParameter("imageUrl");
        Vehicle vehicle = Vehicle.builder().rate(rate).name(name).imageUrl(imageUrl).build();
        vehicleService.addVehicle(vehicle);
        return "Success";
    }
}
