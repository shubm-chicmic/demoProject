package com.example.demoProject.Controller.MevronController;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class LocationRestController {
    @Autowired
    RestTemplate restTemplate;
    @PostMapping("/locationFind")

    public String locationCalculate(HttpServletRequest request) {
        String pickUp = request.getParameter("pick_location");
        String drop = request.getParameter("drop_location");
//        String url = "https://www.google.com/maps/search/" + pickUp;
        String url = "https://www.google.com/maps/dir/" + pickUp + "/" + drop;
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class );

        int statusCode = response.getStatusCodeValue();
        String location = response.getHeaders().getLocation() == null ? "" : response.getHeaders().getLocation().toString();

        System.out.println("\u001B[42m" + statusCode+ "\u001B[0m");
        System.out.println("\u001B[42m" + location+ "\u001B[0m");

//        System.out.println("\u001B[42m" + response.toString()+ "\u001B[0m");
//        DefaultRedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();
//        defaultRedirectStrategy.;
//        return response.getBody();
        return response.toString();
    }
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double RADIUS_OF_EARTH = 6371; // Earth's radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))

                +Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = RADIUS_OF_EARTH * c;
        return distance;
    }
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }
    @PostMapping("/distanceCalculate")
    public double distanceCalculate(HttpServletRequest request) {
        System.out.println("\u001B[42m" + request.getServletPath()+ "\u001B[0m");

        Double pick_lat = Double.parseDouble(request.getParameter("pick_lat"));
        Double pick_log =  Double.parseDouble(request.getParameter("pick_log"));

        Double drop_lat =  Double.parseDouble(request.getParameter("drop_lat"));
        Double drop_log =  Double.parseDouble(request.getParameter("drop_log"));

        double distance = distance((double)pick_lat, drop_lat, pick_log, drop_log,0,0);
        System.out.println("\u001B[42m" + "distance = " + distance+ "\u001B[0m");






        return distance;
    }


}
