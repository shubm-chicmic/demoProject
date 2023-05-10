package com.example.demoProject.Controller.MevronController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;

@RestController
public class LocationController {
    @Autowired
    RestTemplate restTemplate;
    @RequestMapping("/locationFind")
    public String locationCalculate(HttpServletRequest request) {
        String pickUp = request.getParameter("pick_location");
        String drop = request.getParameter("drop_location");
        String url = "https://www.google.com/maps/search/" + pickUp;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class );
        int statusCode = response.getStatusCodeValue();
        String location = response.getHeaders().getLocation() == null ? "" : response.getHeaders().getLocation().toString();

        System.out.println("\u001B[42m" + statusCode+ "\u001B[0m");
        System.out.println("\u001B[42m" + location+ "\u001B[0m");

        System.out.println("\u001B[42m" + response.toString()+ "\u001B[0m");
//        DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();
//        defaultRedirectStrategy.;
        return "";
    }

}
