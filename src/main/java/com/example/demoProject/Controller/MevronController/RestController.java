package com.example.demoProject.Controller.MevronController;

import com.example.demoProject.Dto.UserDto;
import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Repository.RoleRepository;
import com.example.demoProject.Repository.UserRepository;
import com.example.demoProject.Repository.UserRoleRepository;
import com.example.demoProject.Service.EmailService;
import com.example.demoProject.Service.OtpService;
import com.example.demoProject.Service.UserService;
import com.example.demoProject.Service.UsersRolesService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@org.springframework.web.bind.annotation.RestController
@Slf4j
public class RestController {
    @Autowired
    UserService userService;
    @Autowired
    UsersRolesService usersRolesService;
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
    @GetMapping("/getUser")
    public UserDto updateUsers_Error_ho_gya_h(HttpServletRequest request) {
        Users users = userRepository.findUsersById(Integer.parseInt(request.getParameter("id")));
        //Roles roles = roleRepository.findRolesByUserId(users.getId());
        //String role = userRoleRepository.findRolesById(roles.getRoleId());
        UserDto userDto = UserDto.builder()
                .id(users.getId())
                .city(users.getCity())
                .email(users.getEmail())
                .role("")
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .imageUrl(users.getImageUrl())
                .build();

        System.out.println("userDto  =  " + userDto);
        return userDto;
    }

    @GetMapping("/suspendUser")
    public Boolean suspendUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        Boolean status2 = userService.getUserById(id).

        userService.suspendUserById(id);

        Boolean status = userService.getUserById(id).getIsSuspend();
        System.out.println("\u001B[33m" + status2 + " " + status + "\u001B[0m");
        return !status;

    }
    @GetMapping("/softDeleteUser")
    public Boolean softDeleteUser(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));

        userService.softDeleteUserById(id);

        Boolean status = userService.getUserById(id).getIsDelete();

        return !status;

    }
    @PostMapping("/addRandomUsers")
    public String addRandomUsers(HttpServletRequest request) {
        int size = Integer.parseInt(request.getParameter("size"));
        String[] role = {"Driver", "Rider"};
        List<Users> usersList = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= size; i++) {
            int index = random.nextInt(role.length);
            String imageUrl = (index == 0 ?  "assets/img/profile/driver.jpg" : "assets/img/profile/rider.jpg");
            String uuid= UUID.randomUUID().toString();

            Users users = Users.builder()
                    .firstName(role[index] + i)
                    .lastName("")
                    .email(role[index] + i + "@yopmail.com")
                    .uuid(uuid)
                    .password("123456")
                    .phoneNo("9876543210")
                    .city("Mohali")
                    .isSuspend(false)
                    .isDelete(false)
                    .isEmailVerify(false)
                    .imageUrl(imageUrl)

                    .build();
            if(index == 0) {
                userService.addDriver(users);
            }else {
                userService.addRider(users);
            }
        }


        return "Success";
    }

    @RequestMapping("/getTotalDrivers")
    public Integer getTotalDrivers(){
        String role = "DRIVER";
        int roleId = usersRolesService.findIdByRole(role);
        Integer count = (int) roleRepository.countByRoleId(roleId);
        return count;
    }
    @RequestMapping("/getTotalRiders")
    public Integer getTotalRiders(){
        String role = "RIDER";
        int roleId = usersRolesService.findIdByRole(role);
        Integer count = (int) roleRepository.countByRoleId(roleId);
        return count;
    }
    @RequestMapping("/getTotalUsers")
    public Integer getTotalUser(){

        Integer count = (int) roleRepository.count();
        return count;
    }
    @RequestMapping("/getTotalActiveUsers")
    public Integer getTotalActiveUsers(){

        Integer count = userService.getTotalActiveUsers();
        return count;
    }
    @RequestMapping("/getTotalSoftDeletedUsers")
    public Integer getTotalSoftDeletedUsers(){

        Integer count = userService.getTotalSoftDeletedUsers();
        return count;
    }






}
