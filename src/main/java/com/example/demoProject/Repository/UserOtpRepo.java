package com.example.demoProject.Repository;

import com.example.demoProject.Models.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOtpRepo extends JpaRepository<UserOtp, Integer> {
    UserOtp findByEmail(String email);
}
