package com.example.demoProject.Controller.MevronController;

import com.example.demoProject.Dto.UserDto;
import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UsersRoles;
import com.example.demoProject.Repository.RoleRepository;
import com.example.demoProject.Repository.UserRepository;
import com.example.demoProject.Repository.UserRoleRepository;
import com.example.demoProject.Service.*;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {
    @Autowired
    UserService userService;
    @Autowired
    UsersRolesService usersRolesService;
    @Autowired
    RolesService rolesService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    OtpService otpService;
    @Autowired
    EmailService emailService;
    @RequestMapping("/updateUsers")
    public String updateUsers(@RequestBody Users users) {
//          int id = Integer.parseInt(request.getParameter("id"));

//        Users users = userService.getUserById(id);
//
        users = userRepository.save(users);
                return "Success";
    }

    @GetMapping("/suspendUser")
    public Boolean suspendUser(HttpServletRequest request) {
//        int id = Integer.parseInt(request.getParameter("id"));
//        Boolean status2 = userService.getUserById(id).
//
//        userService.suspendUserById(id);
//
//        Boolean status = userService.getUserById(id).getIsSuspend();
//        System.out.println("\u001B[33m" + status2 + " " + status + "\u001B[0m");
//        return !status;
            return true;
    }
//    @GetMapping("/softDeleteUser")
//    public Boolean softDeleteUser(HttpServletRequest request) {
//        int id = Integer.parseInt(request.getParameter("id"));
//
//        userService.softDeleteUserById(id);
//
//        Boolean status = userService.getUserById(id).getIsDelete();
//
//        return !status;
//
//    }
    @PostMapping("/addRandomUsers")
    public String addRandomUsers(HttpServletRequest request) {
        int size = Integer.parseInt(request.getParameter("size"));
        String[] role = {"Driver", "Rider", "Admin"};
        List<Users> usersList = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= size; i++) {
            int index = random.nextInt(role.length);
            String imageUrl = (index == 0 ? "assets/img/profile/driver.jpg" : (index == 1 ? "assets/img/profile/rider.jpg" : "assets/img/profile/rider.jpg"));
            String uuid = UUID.randomUUID().toString();

            Users users = Users.builder()
                    .firstName(role[index] + i)
                    .lastName("")
                    .email(role[index] + i + "@yopmail.com")
                    .uuid(uuid)
                    .password("123456")
                    .phone("9876543210")
                    .city("Mohali")

                    .imageUrl(imageUrl)

                    .build();
            Roles roles = rolesService.findByRoleName(role[index].toUpperCase());
            log.info("\u001B[31m" + "role = " + roles + "\u001B[0m");
            UsersRoles usersRoles = UsersRoles.builder()
                    .roles(roles)
                    .users(users)
                    .isEmailVerify(false)
                    .isSuspend(false)
                    .isDelete(false)
                    .build();
            Set<UsersRoles> usersRolesSet = new HashSet<>();
            usersRolesSet.add(usersRoles);
            users.setUsersRoles(usersRolesSet);
            userService.addUser(users);
        }
            return "Success";
        }


    public List<Users> getAllUsers(
            @RequestParam(value = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10",required = false) Integer pageSize,
            @RequestParam(value = "search", defaultValue = "" ,required = false) String target,
            @RequestParam(value = "sortBy", defaultValue = "" ,required = false) String sortBy,
            @RequestParam(value = "order", defaultValue = "1" ,required = false) Integer order

    ) {
        System.out.println("\u001B[33m" + pageNumber + " " + pageSize + " " + target + "\u001B[0m");
        String role = "Admin";
        List<Users> UsersList = userService.getAllUsers(pageNumber, pageSize, target, sortBy, order, role);

        return UsersList;
    }
    @RequestMapping("/getAllDrivers")
    @ResponseBody
    public List<Users> getAllDrivers() {
        List<Users> UsersList = userService.getAllDrivers();
        return UsersList;
    }











}
