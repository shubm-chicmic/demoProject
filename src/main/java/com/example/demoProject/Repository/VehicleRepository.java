package com.example.demoProject.Repository;

import com.example.demoProject.Models.Users;
import com.example.demoProject.Models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle findVehicleById(long id);
}
