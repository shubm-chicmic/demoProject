package com.example.demoProject.Controller.MevronController;

import com.example.demoProject.Dto.EmailDto;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Service.EmailService;
import com.example.demoProject.Service.OtpService;
import com.example.demoProject.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.Principal;

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




    @RequestMapping("/processDriverRegister")
    public String processDriverRegister(HttpServletRequest request) {
        System.out.println("\u001B[33m" + "inside driver register" + "\u001B[0m");

        String fName = request.getParameter("fname");
        String lName = request.getParameter("lname");
        String email = request.getParameter("email");
        String phNo = (request.getParameter("phone"));
        String password = request.getParameter("password");
        String city = request.getParameter("city");
        String inviteCode = request.getParameter("inviteCode");

        String imageUrl = "assets/img/profile/driver.jpg";
        Users user = Users.builder()
                .firstName(fName)
                .lastName(lName)
                .email(email)
                .phoneNo(phNo)
                .password(password)
                .city(city)
                .isSuspend(false)
                .isDelete(false)
                .isEmailVerify(false)
                .imageUrl(imageUrl)
                .build();
        System.out.println("////////////" + user);
        userService.addDriver(user);
        return "redirect:/sendOtp/" + email;
//        return "driver-documentation";
    }
    @RequestMapping("/sendOtp/{email}")
    public String sendOtpToEmail(@PathVariable("email") String email) throws MessagingException {
        int otp = otpService.generateOTP(email);
        log.info("\u001B[33m" + otp + "\u001B[0m");
        log.info("\u001B[33m" + email + "\u001B[0m");

        emailService.sendOtpMessage(email, "Welcome to Mevron: ", "Your OTP For Verification is : " + Integer.toString(otp));
        return "redirect:/driverEmailVerify?email=" + email;
    }
    @RequestMapping("/driverEmailVerify")
    public String driverEmailVerify(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);

        System.out.println("inside driver Email Verify : " + email );
        return "driver-email-verification";
    }
    @PostMapping("/verifyEmail")
    public String emailVerification(@RequestParam("email") String email, @RequestParam("otp") Integer otpEntered) {
        int otp = otpService.getOtp(email);
        log.info("\u001B[33m" + "otp of email " + otp + "\u001B[0m");
        log.info("\u001B[33m" + "otp of entered " + otpEntered + "\u001B[0m");

        if(otp == otpEntered){
            userService.setUsersEmailVerifyTrue(email);
            otpService.clearOTP(email);
        }else {
            return "redirect:/driverEmailVerify?email=" + email;
        }
        log.info("\u001B[33m" + email + "\u001B[0m");

        return "redirect:/driverLogin?email=" + email;
    }
    @RequestMapping("/processRiderRegister")
    public String processRiderRegister(HttpServletRequest request) {
        System.out.println("\u001B[33m" + "inside rider register" + "\u001B[0m");
        String fName = request.getParameter("fname");
        String lName = request.getParameter("lname");
        String email = request.getParameter("email");
        String phNo = (request.getParameter("phone"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("cnfPassword");
        String city = request.getParameter("city");
        String inviteCode = request.getParameter("inviteCode");

        String imageUrl = "assets/img/profile/rider.jpg";

        if(!confirmPassword.equals(password)) {
            System.out.println("//////////////////" + "inside");
            return "error";
        }
        Users user = Users.builder()
                .firstName(fName)
                .lastName(lName)
                .email(email)
                .phoneNo(phNo)
                .password(password)
                .city(city)
                .isSuspend(false)
                .isDelete(false)
                .isEmailVerify(false)
                .imageUrl(imageUrl)
                .build();
        System.out.println("////////////" + user);
        userService.addRider(user);
        return "driver-documentation";
    }

    @RequestMapping("/processDriverLogin")
    public String processDriverLogin(HttpServletRequest request, Model model) {
        String value = request.getParameter("mob");
       // System.out.println("///////////////" + value);
       Users users =  userService.getUsersByEmailorPhoneNo(value, value);

       if(users == null){
           model.addAttribute("email", value);
           model.addAttribute("isEmailNull", "true");

           model.addAttribute("isEmailVerify", "false");
           return "driver-login";
       }
       if(!users.getIsEmailVerify()) {
           model.addAttribute("email", value);
           model.addAttribute("isEmailNull", "false");
           model.addAttribute("isEmailVerify", "false");
           return "driver-login";
       }
        model.addAttribute("email", value);
        model.addAttribute("isEmailNull", "false");
        model.addAttribute("isEmailVerify", "true");

        System.out.println("Model1 = " + model.getAttribute("email"));
        emailDto.setEmail(users.getEmail());

        System.out.println("CurUsers = "  + emailDto.getEmail());
        return "driver-password";
    }

    @RequestMapping("/driverProfile")
    public String processDriverPasswordLogin(HttpServletRequest request, Model model, Principal principal) {
       // String password = request.getParameter("lpassword");
        System.out.println("Inside processDriverLogin");
      //  System.out.println("Password = " + password);
//        String uri = "http://localhost:8080/hello";
//
        String email = principal.getName();
        Users users = userService.getUserByEmail(email);

        model.addAttribute("fName", users.getFirstName());
        model.addAttribute("lName", users.getLastName());
        model.addAttribute("email", users.getEmail());
        model.addAttribute("phNo", users.getPhoneNo());

        return "driver-profile";
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
                .phoneNo(phone)
                .build();
        userService.updateDriver(users);
        String redirectUrl = "http://localhost:8080/driverProfile";
        new DefaultRedirectStrategy().sendRedirect(request,response,redirectUrl);


    }


    @RequestMapping("/registerSuccess")
    public String registerDriverSuccess() {
        return "register-success";
    }

    public String getCurEmail() {
        System.out.println("//////////////////////////////////// " + emailDto.getEmail());
        return emailDto.getEmail();
    }


    @RequestMapping("/processRiderLogin")
    public String processRiderLogin(HttpServletRequest request, Model model) {
        emailDto.setEmail(request.getParameter("mob"));
        System.out.println("\u001B[33m" + " inside processriderlogin================ " + emailDto + "\u001B[0m");
        model.addAttribute("emailDto", request.getParameter("mob"));
//        ra.addFlashAttribute("savedEmail", emailDto1);
        return "rider-password";
    }
    @PostMapping("/processRiderPassword")
    public String processRiderPassword(HttpServletRequest request) {
        emailDto.setPassword(request.getParameter("password"));
        System.out.println("\u001B[33m" + " inside processriderpassword===== " + emailDto + "\u001B[0m");

        return "redirect:/login";
    }

}
