package com.example.demoProject.Controller.MevronController;

import com.example.demoProject.Models.Users;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
public class HomeController {

    @RequestMapping(value = {"", "/", "home"})
    public String homePage(Model model) {

        model.addAttribute("index", "Mevron - HomePage");
        model.addAttribute("isEmailNull", "false");
        return "index";
    }

    @PostMapping("/hello")
    @ResponseBody
    public String helloWorld(@RequestBody HashMap<String,String> mp) {
        return "Hello World";
    }

    @RequestMapping("/driverDocumentation")
    public String driverDocumentation(){
        return "driver-documentation";
    }


}
