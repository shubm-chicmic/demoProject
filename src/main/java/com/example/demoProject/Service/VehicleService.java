package com.example.demoProject.Service;

import com.example.demoProject.Models.Vehicle;
import com.example.demoProject.Repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    public Vehicle addVehicle(Vehicle vehicle){
        return vehicleRepository.save(vehicle);
    }
    public Vehicle findVehicleById(long vehicleId) {
        return  vehicleRepository.findVehicleById(vehicleId);
    }
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }
}
