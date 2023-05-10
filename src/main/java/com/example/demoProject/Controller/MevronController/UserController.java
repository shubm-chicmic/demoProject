package com.example.demoProject.Controller.MevronController;

import com.example.demoProject.Dto.EmailDto;

import com.example.demoProject.Models.PasswordReset;
import com.example.demoProject.Models.Roles;
import com.example.demoProject.Models.UserUuid;
import com.example.demoProject.Models.Users;
import com.example.demoProject.Service.EmailService;
import com.example.demoProject.Service.PasswordService;
import com.example.demoProject.Service.RolesService;
import com.example.demoProject.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
//@RequestMapping("/public")
public class UserController {

    @Autowired
    EmailService emailService;
    @Autowired
    PasswordService passwordService;
    @Autowired
    RolesService rolesService;
    @Autowired
    EmailDto emailDto;

    @Autowired
    UserService userService;
    @Value("${myDomain}")
    String myDomain;

    @PostMapping("selectRoles")
    public String selectRole(@RequestParam("email") String email) {
        Users users = userService.getUserByEmail(email);
        List<Roles> rolesList = rolesService.findRolesByUsers(users);

        log.info("\u001B[31m" + rolesList + "\u001B[0m");
        return "user-role-selection";
    }
    @RequestMapping("userRegister/{role}")
    public String userRegister(@PathVariable("role") String role, HttpServletResponse response, Model  model, HttpServletRequest request) {
        Cookie cookie = new Cookie("role", role);
        cookie.setMaxAge(60*60);
        cookie.setPath("/");
        response.addCookie(cookie);
        model.addAttribute("error", request.getParameter("error"));
        if(role.equals("DRIVER")){
            return "signup-driver";
        }else if(role.equals("RIDER")){
            return "signup-rider";
        }
        return "";
    }

    @RequestMapping("/driverLogin")
    public String driverLogin(HttpServletRequest request, Model model) {
        model.addAttribute("email", request.getParameter("email"));
        return "driver-login";
    }
    @RequestMapping("/riderLogin")
    public String riderLogin(@ModelAttribute("emailDto") EmailDto emailDto1) {
        return "rider-login";
    }








    @RequestMapping("/getUserById/{id}")
    @ResponseBody
    public Users getUserById(@PathVariable("id") Integer id) {
        Users users = userService.getUserById(id);
        System.out.println("\u001B[31m" + "inside getuserById = "+ users + "\u001B[0m");
        return users;
    }
    @RequestMapping("/resetPassword")
    public String resetPassword(Model model, @RequestParam("email") String email) {
        model.addAttribute("email", email);
        return "forgot-password";

    }
    @PostMapping("/forgetPassword")

    public String forgetPassword(@RequestParam("email") String email, Model model) throws MessagingException {
        log.info("\u001B[31m" + "Inside Forget Password " + email + "\u001B[0m" );
        String url = myDomain + "checkLinkPasswordReset/";
        log.info("\u001B[31m"  + url + "\u001B[0m" );
        //Added Random uuid
        PasswordReset passwordReset = passwordService.addUuidByEmail(email);
        String userUuid = userService.getUserByEmail(email).getUuid();
        String token = passwordReset.getUuid();
        url += userUuid + "/" + token +  "?email=" + email;

        String message = "<p>Click Below To Reset Your Password</p>\n" +
                "\n" +
                "<a href=\""+ url + "\">" +
                "<img src='cid:clickLink' style=\"width:400px;height:42px;\"></a> ";

        emailService.sendPasswordResetLink(email, "Reset Your Password",  message);
        model.addAttribute("email", email);
        return "reset-email";
    }
    @RequestMapping("/checkLinkPasswordReset/{uniqueId}/{userUuid}")
    public String forgetPassword(@PathVariable("userUuid") String userUuid, @PathVariable("uniqueId") String userUniqueId,
                                 @RequestParam("email")String email,  Model model){
         String userUuidFinal = passwordService.getByEmail(email).getUuid();
         String userUniqueIdFinal = userService.getUserByEmail(email).getUuid();
        if((userUuidFinal != null && userUniqueIdFinal != null) &&
                (userUniqueIdFinal.equals(userUniqueId) && userUuidFinal.equals(userUuid))
        ){
            model.addAttribute("email", email);
            return "reset-password";
        }
        passwordService.deleteTokenByEmail(email);
        model.addAttribute("alert", "Link does Not Matched");
        model.addAttribute("error", true);
        model.addAttribute("email", email);
        return "forgot-password";
    }
    @PostMapping("/resetPassword/{email}")
    public String resetPassword(@PathVariable("email") String email,HttpServletRequest request, Model model) {
        String newPassword = request.getParameter("password");
        String cnfPassword = request.getParameter("cnfpassword");
        if(newPassword.equals(cnfPassword)) {
            Users users = userService.getUserByEmail(email);
            users.setPassword(newPassword);
            //userService.addUser(user);
            model.addAttribute("alert", "Password Reset Successfully !");
            model.addAttribute("email", email);
            //delete token
//            passwordService.deleteTokenByEmail(email);
            return "driver-password";
        }else {
            log.info("\u001B[31m" + "Inside Reset Password " + email + "\u001B[0m" );

            model.addAttribute("alert", "Password Does Not Matched");
            model.addAttribute("email", email);
            return "reset-password";
        }
    }

    @RequestMapping("/UserLogout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        session.invalidate();
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            cookies[i].setValue(null);
            cookies[i].setMaxAge(0);
            response.addCookie(cookies[i]);
        }
        return "redirect:/";
    }

}
