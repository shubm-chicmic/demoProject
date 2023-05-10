package com.example.demoProject.Controller.MevronController;

import com.example.demoProject.Dto.EmailDto;
import com.example.demoProject.Dto.UserDto;
import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.UsersRoles;
import com.example.demoProject.Service.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@Slf4j
public class ProcessController {

//    @Autowired
//    EmailDto1 emailDto1;

    @Autowired
    UserService userService;
    @Autowired
    EmailDto emailDto;
    @Autowired
    OtpService otpService;
    @Autowired
    EmailService emailService;
    @Autowired
    UsersRolesService usersRolesService;
    @Autowired
    RolesService rolesService;

    public Boolean findUserAlreadyExistedWithRole(String email, String role) {
        Users users = userService.getUserByEmail(email);

        if(users != null){
            Set<UsersRoles> usersRolesSet = users.getUsersRoles();
            for (UsersRoles usersRoles : usersRolesSet) {
                if(usersRoles.getRoles().getRoleName().equals(role)){
                    return true;
                }
            }
        }
        return false;
    }
    Integer findUserAlreadyExist(String email) {
        Users users  = userService.getUserByEmail(email);
        if(users != null) {
            return users.getId();
        }else {
            return null;
        }
    }

    public void setCookie(String name, String value, int age, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
    @PostMapping("processUserRegister")
    public String processUserRegister(UserDto userDto, @CookieValue("role") String role) {

        Boolean isUserExistWithRole = findUserAlreadyExistedWithRole(userDto.getEmail(), role);
        Integer id = findUserAlreadyExist(userDto.getEmail());
        if(isUserExistWithRole) {
            return "redirect:/userRegister/" + role + "?error=" + role + " Already Register With Same Email";
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        log.info("\u001B[41m" + userDto + "\u001B[0m");

        Users users = mapper.convertValue(userDto, Users.class);
        if(id != null)
        users.setId(id);
        users.setUuid(UUID.randomUUID().toString());
        log.info("\u001B[41m" + users + "\u001B[0m");

        Roles roles = rolesService.findByRoleName(role);
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
        return "redirect:/sendOtp/" + users.getEmail();
    }


    @RequestMapping("/sendOtp/{email}")
    public String sendOtpToEmail(@PathVariable("email") String email) throws MessagingException {
        int otp = otpService.generateOTP(email);
        emailService.sendOtpMessage(email, "Welcome to Mevron: ", "Your OTP For Verification is : " + Integer.toString(otp));
        return "redirect:/UserEmailVerify?email=" + email;
    }
    @RequestMapping("/UserEmailVerify")
    public String UserEmailVerify(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);

        System.out.println("inside User Email Verify : " + email );
        return "user-email-verification";
    }
    @PostMapping("/verifyEmail")
    public String emailVerification(@RequestParam("email") String email, @RequestParam("otp") Integer otpEntered, @CookieValue("role") String role) {
        int otp = otpService.getOtp(email);
        log.info("\u001B[33m" + "otp of email " + otp + "\u001B[0m");
        log.info("\u001B[33m" + "otp of entered " + otpEntered + "\u001B[0m");

        if(otp == otpEntered){
            userService.setUsersEmailVerifyTrue(email, role);
            otpService.clearOTP(email);
        }else {
            return "redirect:/UserEmailVerify?email=" + email;
        }
        log.info("\u001B[33m" + email + "\u001B[0m");

        return "redirect:/home?email=" + email;
    }

    @PostMapping("/processUserEmail")
    public String processUserEmail(@RequestParam("email") String email, Model model) {
        System.out.println("\u001B[31m" + "processUser call ho gya" + "\u001B[0m" );
        Users users =  userService.getUsersByEmailorPhoneNo(email, email);

        if(users == null){
            System.out.println("\u001B[31m" + "ifcalled " + email + "\u001B[0m" );
            return "email-not-exist";

        }
        model.addAttribute("email", email);

        Set<UsersRoles> usersRolesSet = users.getUsersRoles();
        List<String> rolesList = new ArrayList<>();
        for (UsersRoles usersRoles : usersRolesSet) {
            rolesList.add(usersRoles.getRoles().getRoleName());
        }
        model.addAttribute("rolesList", rolesList);


        return "user-role-selection";
    }
    @RequestMapping("/processUserLogin/{role}")
    public String processUserLoginViaRole(@PathVariable String role, Model model,
         @RequestParam("email") String email, HttpServletResponse response

    ) {
        setCookie("role", role, 24*60*60, response);
        model.addAttribute("email", email);

        return "driver-password";
    }
    @PostMapping("/processUserLogin")
    public String processUserLogin(@RequestParam("password") String password, @CookieValue("role") String role
                               , @RequestParam("email") String email
    ) {
        return "redirect:/login?email=" + email + "&password=" + password;
    }

    @RequestMapping("/profile/{role}")
    public String driverProfile(@PathVariable("role") String role, Model model, Principal principal) {
        System.out.println("Inside profile " + role);
        String email = principal.getName();
        Users users = userService.getUserByEmail(email);
        model.addAttribute("user", users);
        if(role.equals("DRIVER"))
        return "driver-profile";
        return "rider-profile";
    }

    @PostMapping("/profileSetting")
    public void profileSetting(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("inside Profile setting /////////////");
        String fName = request.getParameter("fname");
        String lName = request.getParameter("lname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        System.out.println(fName + lName + email + phone);

        Users users = Users.builder()
                .firstName(fName)
                .lastName(lName)
                .email(email)
                .phone(phone)
                .build();
        userService.updateDriver(users);
        String redirectUrl = "http://localhost:8080/driverProfile";
        new DefaultRedirectStrategy().sendRedirect(request,response,redirectUrl);


    }


//    @RequestMapping("/registerSuccess")
//    public String registerDriverSuccess() {
//        return "register-success";
//    }
//
//    public String getCurEmail() {
//        System.out.println("//////////////////////////////////// " + emailDto.getEmail());
//        return emailDto.getEmail();
//    }


//    @RequestMapping("/processRiderLogin")
//    public String processRiderLogin(HttpServletRequest request, Model model) {
//        emailDto.setEmail(request.getParameter("mob"));
//        System.out.println("\u001B[33m" + " inside processriderlogin================ " + emailDto + "\u001B[0m");
//        model.addAttribute("emailDto", request.getParameter("mob"));
////        ra.addFlashAttribute("savedEmail", emailDto1);
//        return "rider-password";
//    }
//    @PostMapping("/processRiderPassword")
//    public String processRiderPassword(HttpServletRequest request) {
//        emailDto.setPassword(request.getParameter("password"));
//        System.out.println("\u001B[33m" + " inside processriderpassword===== " + emailDto + "\u001B[0m");
//
//        return "redirect:/login";
//    }

}
