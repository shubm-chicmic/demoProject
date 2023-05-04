package com.example.demoProject.Controller.AdminController;

import com.example.demoProject.Dto.Message;
import com.example.demoProject.Service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminRestController {
    @Autowired
    RolesService rolesService;
    @Message("Admin Added a Role ")
    @PostMapping("/addRole")
    public void addRoles(@RequestParam String roleName) {
        rolesService.addRoles(roleName);
    }
}
