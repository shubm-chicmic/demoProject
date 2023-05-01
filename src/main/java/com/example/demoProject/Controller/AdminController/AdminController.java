package com.example.demoProject.Controller.AdminController;


import com.example.demoProject.Dto.Message;
import com.example.demoProject.Dto.UserDto;
import com.example.demoProject.Models.UserUuid;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UsersActivity;
import com.example.demoProject.Repository.RoleRepository;
import com.example.demoProject.Service.RolesService;
import com.example.demoProject.Service.UserActivityService;
import com.example.demoProject.Service.UserService;
import com.example.demoProject.Service.UsersRolesService;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    @Autowired
    UserService userService;
    @Autowired
    UserActivityService userActivityService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UsersRolesService usersRolesService;

    @Value("${image.path2}")
    String imagePath;
    @Value("${serverAddress}")
    String serverAdd;

    @PostMapping("/adminLogin")
    public String processAdminLogin(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Users users = userService.getUserByEmail(email);
        List<Integer> roleId = userService.findRoleIdByUserId(users.getId());
        for(Integer roleid : roleId) {
            String role = usersRolesService.findRoleById(roleid);
            if (role.equals("ADMIN") && password.equals(users.getPassword())) {
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
        String getUsersUrl = serverAdd + "/getTotalDrivers";
        int TotalDrivers =  restTemplate.getForObject(getUsersUrl, Integer.class);
        getUsersUrl = serverAdd + "/getTotalRiders";
        int TotalRiders =  restTemplate.getForObject(getUsersUrl, Integer.class);
        getUsersUrl = serverAdd + "/getTotalUsers";
        int TotalUsers =  restTemplate.getForObject(getUsersUrl, Integer.class);
        getUsersUrl = serverAdd + "/getTotalActiveUsers";
        int TotalActiveUsers =  restTemplate.getForObject(getUsersUrl, Integer.class);
        getUsersUrl = serverAdd + "/getTotalSoftDeletedUsers";
        int TotalSoftDeletedUsers =  restTemplate.getForObject(getUsersUrl, Integer.class);

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
        System.out.println(imagePath);
        byte[] bytes = multipartFile.getBytes();
        Path path = Paths.get(folder +  multipartFile.getOriginalFilename());
        System.out.println("\u001B[33m" + path + "\u001B[0m" );
        Files.write(path, bytes);

        System.out.println("\u001B[33m" + "updateProfile =  " + userDto + "\u001B[0m");
        String fileName = multipartFile.getOriginalFilename();
        Users users = userService.updateUserByEmail(userDto.getEmail(), userDto, fileName);
        System.out.println("\u001B[33m" + "updateProfile =  " + users + "\u001B[0m");

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
        String pageNumber = request.getParameter("pageNumber");
        String pageSize = request.getParameter("pageSize");
        String target = request.getParameter("search");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        if(pageSize == null) pageSize = "6";

        if(pageNumber == null) pageNumber = "0";
        if(sortBy == null) sortBy = "firstName";
        if(order ==  null) order = "1";



        System.out.println("\u001B[33m" + pageNumber + " " + pageSize + "\u001B[0m");
        String getUsersUrl = serverAdd + "/getAllUsers?pageNumber=" + pageNumber + "&pageSize=" + pageSize + "&search=" + target + "&sortBy=" + sortBy + "&order=" + order;
        Object[] drivers = restTemplate.getForObject(getUsersUrl, Object[].class);
        List<Object> driversList = Arrays.asList(drivers);

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
        String id = request.getParameter("id");
        System.out.println("id = " + id);
        String getUsersUrl = serverAdd +  "/getUser?id=" + id;
        System.out.println(getUsersUrl);
        UserDto user = restTemplate.getForObject(getUsersUrl, UserDto.class);
        System.out.println(user);

        model.addAttribute("User", user);
        return "all-user-profile";
    }

    @RequestMapping("/updateUserProfile")
    @Message("User Profile Updated by Admin ")

    public String updateUserProfile(UserDto userDto) {


        String getUsersUrl = "/updateUsers";
//         create request body
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("firstName", "123");
        jsonObject.put("password", "1234");

// set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);

// send request and parse result
        ResponseEntity<String> loginResponse = restTemplate
                .exchange(getUsersUrl, HttpMethod.POST, entity, String.class);
        if (loginResponse.getStatusCode() == HttpStatus.OK) {
            // JSONObject userJson = new JSONObject(loginResponse.getBody());
        } else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            // nono... bad credentials
        }
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

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getUsersUrl)
                // Add query parameter
                .queryParam("fname", userDto.getFirstName())
                .queryParam("lname", userDto.getLastName())
                .queryParam("phone", userDto.getPhone())
                .queryParam("email", userDto.getEmail())
                .queryParam("city", userDto.getCity());

        System.out.println("\u001B[33m" + builder.buildAndExpand(getUsersUrl).toUri() + "\u001B[0m");
        getUsersUrl = builder.buildAndExpand(getUsersUrl).toUri().toString();
        String response = restTemplate.getForObject(getUsersUrl, String.class);

        return "add-driver";
    }



}
