package com.example.demoProject.Controller.AdminController;


import com.example.demoProject.Controller.MevronController.RestController;
import com.example.demoProject.Dto.Message;
import com.example.demoProject.Dto.UserDto;
import com.example.demoProject.Models.*;
import com.example.demoProject.Repository.RoleRepository;
import com.example.demoProject.Service.RolesService;
import com.example.demoProject.Service.UserActivityService;
import com.example.demoProject.Service.UserService;
import com.example.demoProject.Service.UsersRolesService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    RolesService rolesService;
    @Autowired
    UserActivityService userActivityService;
    @Autowired
    RestController restController;
//    @Autowired
//    RoleRepository roleRepository;
//    @Autowired
//    UsersRolesService usersRolesService;

    @Value("${image.path2}")
    String imagePath;
    @Value("${serverAddress}")
    String serverAdd;

    @PostMapping("/adminLogin")
    public String processAdminLogin(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Users users = userService.getUserByEmail(email);
        List<Roles> rolesList = rolesService.findRolesByUsers(users);

        log.info("\u001B[31m" + rolesList + "\u001B[0m");
        //List<Integer> roleId = userService.findRoleIdByUserId(users.getId());
        for(Roles roles : rolesList) {
            //String role = //usersRolesService.findRoleById(roleid);
            if (roles.getRoleName().equals("ADMIN") && password.equals(users.getPassword())) {
                UUID uuid = UUID.randomUUID();
                UserUuid uuidEntity = new UserUuid();
                uuidEntity.setUuid(uuid.toString());
                uuidEntity.setEmail(users.getEmail());
                userService.CreateToken(uuidEntity);

                String name = users.getFirstName() + "_" + users.getLastName();
                Cookie cookie1 = new Cookie("UserDetail", name);
                cookie1.setMaxAge(24 * 60 * 60);
                Cookie cookie2 = new Cookie("UserImageUrl", users.getImageUrl());
                cookie2.setMaxAge(24 * 60 * 60);
                Cookie cookie3 = new Cookie("UserEmail", users.getEmail());
                cookie3.setMaxAge(24 * 60 * 60);

                response.addCookie(cookie1);
                response.addCookie(cookie2);
                response.addCookie(cookie3);
                request.getSession().setAttribute("Authorization", uuid.toString());

                return "redirect:/admin/dashboard";
            }
        }
        return "/error";
    }
    @RequestMapping("/dashboard")
    @Message("Admin visited dashboard ")

    public String adminDashboard(HttpSession session, Model model) {
        long TotalDrivers =  rolesService.findUsersByRole("DRIVER");
        long TotalRiders =  rolesService.findUsersByRole("RIDER");
        long TotalUsers =  rolesService.findUsersByRole("");
        long TotalActiveUsers =  rolesService.findSuspend(false);
        long TotalSoftDeletedUsers =  rolesService.findDeletedUsers(true);

        System.out.println("\u001B[33m" + "adminDashboard " + "\u001B[0m");

        List<UsersActivity> usersActivities = userActivityService.getAllActivity(6);
        System.out.println(usersActivities);
        model.addAttribute("activities", usersActivities);
        model.addAttribute("TotalDrivers",TotalDrivers);
        model.addAttribute("TotalRiders",TotalRiders);
        model.addAttribute("TotalUsers",TotalUsers);
        model.addAttribute("TotalActiveUsers",TotalActiveUsers);
        model.addAttribute("TotalSoftDeletedUsers",TotalSoftDeletedUsers);


        return "dashboard";
    }

    @RequestMapping("/profile")
    @Message("Admin visited Profile Page ")

    public String profile(Model model, @CookieValue("UserEmail") String email) {
        Users users = userService.getUserByEmail(email);
        model.addAttribute("User", users);
        return "user-profile";
    }

    @PostMapping("/updateProfile")
    @Message("Profile updated By Admin ")

    public String updateProfile(
            @RequestParam("imageUrl") MultipartFile multipartFile,
            @ModelAttribute("userDto") UserDto userDto, Model model, HttpServletResponse response

    ) throws IOException {


        String folder = imagePath;
        byte[] bytes = multipartFile.getBytes();
        Path path = Paths.get(folder +  multipartFile.getOriginalFilename());
        Files.write(path, bytes);

        String fileName = multipartFile.getOriginalFilename();
        Users users = userService.updateUserByEmail(userDto.getEmail(), userDto, fileName);

        String name = users.getFirstName() +"_"+ users.getLastName();
        Cookie cookie1 = new Cookie("UserDetail", name);
        cookie1.setMaxAge(24*60*60);
        Cookie cookie2 = new Cookie("UserImageUrl", users.getImageUrl());
        cookie2.setMaxAge(24*60*60);

        response.addCookie(cookie1);
        response.addCookie(cookie2);

        model.addAttribute("User", users);
        return "user-profile";
    }
    @RequestMapping("/users")
    @Message("Admin viewed All users ")

    public String Users(Model model, HttpServletRequest request) {
        System.out.println("inside Users ");
        Integer pageNumber = null;
        Integer pageSize = null;
        String target = null;
        String sortBy = null;
        Integer order = null;


        try {
            pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
            target = request.getParameter("search");
            sortBy = request.getParameter("sortBy");
            order = Integer.parseInt(request.getParameter("order"));
        }catch (Exception e) {
            if (pageSize == null) pageSize = 6;

            if (pageNumber == null) pageNumber = 0;
            if (sortBy == null) sortBy = "firstName";
            if (order == null) order = 1;
        }


        System.out.println("\u001B[33m" + pageNumber + " " + pageSize + "\u001B[0m");
        List<Users> driversList = restController.getAllUsers(pageNumber,pageSize,target,sortBy,order);
        System.out.println(driversList);
        //Model

        int size = 10;
        int[] totalPages = new int[size];
        System.out.println("\u001B[33m" +pageSize + " " +  totalPages.length + "\u001B[0m");
        model.addAttribute("Drivers",driversList);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("maxPage", size);

        model.addAttribute("searchValue" , target);
        model.addAttribute("order", order);
        System.out.println("Page Number   =   " + model.getAttribute("currentPage"));
        System.out.println("target = " + target);
        System.out.println("Search   =   " + model.getAttribute("searchValue"));

        return "users";
    }
    //diver rider update
    @RequestMapping("/userProfile")
    @Message("Admin viewed User Profile of ")

    public String userProfile(HttpServletRequest request, Model model) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        System.out.println("id = " + id);
        String getUsersUrl = serverAdd +  "/getUser?id=" + id;
        System.out.println(getUsersUrl);
        Users users = userService.getUserById(id);


        model.addAttribute("User", users);
        return "all-user-profile";
    }

    @RequestMapping("/updateUserProfile")
    @Message("User Profile Updated by Admin ")

    public String updateUserProfile(UserDto userDto) {


        String getUsersUrl = "/updateUsers";
        return "all-user-profile";
    }

    //Add driver
    @RequestMapping("addDriver")
    @Message("Admin Visited the Page to add a Driver ")

    public String addDriver(){
        return "add-driver";
    }
    @RequestMapping("/addDriverByAdmin")
    @Message("New Driver Added Successfully")

    public String addDriverByAdmin(UserDto userDto) {
        String getUsersUrl = "/processDriverRegister";
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Users users = mapper.convertValue(userDto, Users.class);
        Roles roles = rolesService.findByRoleName("DRIVER");
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
        return "add-driver";
    }



}
