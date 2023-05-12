package com.example.demoProject.Controller.MevronController;

import com.example.demoProject.Models.Vehicle;
import com.example.demoProject.Service.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LocationController {
    @Autowired
    VehicleService vehicleService;
    @GetMapping("/pickDrop")
    public String pickDrop(HttpServletRequest request, Model model) {
        long vehicleId = Long.parseLong(request.getParameter("id"));
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
        model.addAttribute("vehicle", vehicle);
        System.out.println("\u001B[42m" + "vehicleid = " + vehicleId + "\u001B[0m");
        return "pick-drop";
    }

}
