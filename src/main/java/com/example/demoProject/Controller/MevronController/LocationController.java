package com.example.demoProject.Controller.MevronController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LocationController {
    @PostMapping("/pickDrop")
    public String pickDrop(HttpServletRequest request) {
        long vehicleId = Long.parseLong(request.getParameter("id"));
        System.out.println("\u001B[42m" + "vehicleid = " + vehicleId + "\u001B[0m");
        return "pick-drop";
    }

}
